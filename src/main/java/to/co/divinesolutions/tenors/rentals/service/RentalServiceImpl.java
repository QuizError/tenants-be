package to.co.divinesolutions.tenors.rentals.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import to.co.divinesolutions.tenors.bill_and_payment.dto.BillDto;
import to.co.divinesolutions.tenors.bill_and_payment.dto.BillItemDto;
import to.co.divinesolutions.tenors.bill_and_payment.service.BillService;
import to.co.divinesolutions.tenors.clients.service.ClientService;
import to.co.divinesolutions.tenors.entity.*;
import to.co.divinesolutions.tenors.enums.BillStatus;
import to.co.divinesolutions.tenors.enums.BillType;
import to.co.divinesolutions.tenors.enums.Currency;
import to.co.divinesolutions.tenors.enums.RentalStatus;
import to.co.divinesolutions.tenors.property.service.PropertyService;
import to.co.divinesolutions.tenors.property.service.UnitSectionService;
import to.co.divinesolutions.tenors.rentals.dto.RentalDto;
import to.co.divinesolutions.tenors.rentals.repository.RentalRepository;
import to.co.divinesolutions.tenors.utils.Response;
import to.co.divinesolutions.tenors.utils.ResponseCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService{

    private final UnitSectionService unitSectionService;
    private final ClientService clientService;
    private final RentalRepository rentalRepository;
    private final PropertyService propertyService;
    private final BillService billService;

    @Override
    public Response<Rental> save(RentalDto dto){
        try {

            Optional<UnitSection> optionalUnitSection = unitSectionService.getOptionalByUid(dto.getUnitSectionUid());
            if (optionalUnitSection.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Section to be billed could not be found or may have been deleted from the system", null);
            }
            UnitSection unitSection = optionalUnitSection.get();

            Property property = unitSection.getUnit().getProperty();

            Optional<Client> optionalClient = clientService.getOptionalByUid(dto.getClientUid());
            if (optionalClient.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Client could not be found or may have been deleted from the system", null);
            }
            Client client = optionalClient.get();

            Optional<Rental> optionalRental = getOptionalByUid(dto.getUid());
            if (dto.getUid() != null && !dto.getUid().isEmpty() && optionalRental.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Rental record could not be found or may have been deleted from the system", null);
            }
            LocalDate startDate = dto.getStartDate() != null && !dto.getStartDate().isEmpty() ? LocalDate.parse(dto.getStartDate()) : LocalDate.now() ;
            LocalDate endDate = dto.getEndDate() != null && !dto.getEndDate().isEmpty() ? LocalDate.parse(dto.getEndDate()) : startDate.plusMonths(6);

            long monthsBetween = ChronoUnit.MONTHS.between(startDate, endDate.plusDays(1));
            BigDecimal totalBillAmount = BigDecimal.valueOf(monthsBetween)
                    .multiply(unitSection.getPrice());

            BigDecimal totalServiceCharge =  property.getHasServiceCharge() !=null && property.getHasServiceCharge() ? BigDecimal.valueOf(monthsBetween)
                    .multiply(property.getServiceChargeAmount()) : BigDecimal.ZERO;

            Rental rental = optionalRental.orElse(new Rental());
            rental.setClient(client);
            rental.setUnitId(unitSection.getUnit().getId());
            rental.setPropertyId(property.getId());
            rental.setUnitSection(unitSection);
            rental.setBillStatus(dto.getBillStatus() != null ? dto.getBillStatus() : BillStatus.WaitingForPayment);
            rental.setRentalStatus(dto.getRentalStatus() != null ? dto.getRentalStatus() : RentalStatus.NOT_ACTIVE);
            rental.setStartDate(startDate);
            rental.setEndDate(endDate);
            rental.setServiceChargeDescription(property.getServiceChargeDescription());
            rental.setServiceCharge(totalServiceCharge);
            rental.setSecureDeposit(rental.getUnitSection().getPrice());
            rental.setRentalAmount(totalBillAmount);
            rental.setCurrency(unitSection.getCurrency());
            Rental saved = rentalRepository.save(rental);

            //create bill
            createBill(saved);
            //end
            unitSection.setAvailable(false);
            unitSectionService.changeAvailability(unitSection);

            return new Response<>(true, ResponseCode.SUCCESS, "Rental record saved successfully now waiting for payment", saved);
        }
        catch (Exception e){
            log.error("***** Error on saving rental record: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INVALID_INPUT, "Error when saving rental record", null);
        }
    }

    void createBill(Rental rental) {
        BillDto billDto = new BillDto();
        billDto.setCurrency(rental.getCurrency());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String billRef = rental.getPropertyId() + rental.getUnitId() + rental.getUnitSection().getId() + LocalDateTime.now().format(formatter);
        billDto.setBillReferenceNumber(billRef);
        billDto.setBillDescription("Bill for renting " + rental.getUnitSection().getUnit().getProperty().getName() + " on unit "
                + rental.getUnitSection().getName() + " from "
                + rental.getStartDate() + " to " + rental.getEndDate());

        List<BillItemDto> items = new ArrayList<>();

        // Rental amount (always present)
        items.add(createBillItem("Rental amount", rental.getRentalAmount(),rental.getCurrency() ,BillType.RENTALS));

        // Service charge (optional)
        if (rental.getServiceCharge() != null && rental.getServiceCharge().compareTo(BigDecimal.ZERO) > 0) {
            items.add(createBillItem(rental.getServiceChargeDescription(), rental.getServiceCharge(),rental.getCurrency(), BillType.SERVICE_CHARGE));
        }

        // Secure deposit (optional)
        if (rental.getSecureDeposit() != null && rental.getSecureDeposit().compareTo(BigDecimal.ZERO) > 0) {
            items.add(createBillItem("Secure deposit", rental.getSecureDeposit(),rental.getCurrency(), BillType.SECURE_DEPOSIT));
        }

        // Set items and calculate total
//        bill.setBillItems(items);
        billDto.setTotalAMount(items.stream()
                .map(BillItemDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        BigDecimal exchangeRate = BigDecimal.valueOf(rental.getCurrency().equals(Currency.TZS) ? 1 : 2450);
        BigDecimal totalEquivalentAmount = rental.getRentalAmount().multiply(exchangeRate);
        billDto.setExchangeRate(exchangeRate);
        billDto.setTotalEquivalentAmount(totalEquivalentAmount);
        billDto.setCommission(rental.getRentalAmount().multiply(BigDecimal.valueOf(0.005)));
        billDto.setAgentFee(rental.getUnitSection().getPrice());
        billDto.setBillType(BillType.RENTALS);
        billDto.setPropertyId(rental.getPropertyId());
        billDto.setBillableId(rental.getId());

        // Save via your service
        billService.saveBill(billDto);
    }

    private BillItemDto createBillItem(String description, BigDecimal amount, Currency currency, BillType type) {
        BillItemDto item = new BillItemDto();
        item.setDescription(description);
        item.setAmount(amount);
        item.setCurrency(currency);
        item.setBillType(type);
        return item;
    }

    @Override
    public  Optional<Rental> getOptionalByUid(String uid) {
        return uid != null && !uid.isEmpty() ? rentalRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Response<RentalDto> findByUid(String uid){
        Optional<Rental> optionalRental = getOptionalByUid(uid);
        if (optionalRental.isEmpty()){
            return  new Response<>(false, ResponseCode.NO_DATA_FOUND, "Rental record could not be found or may have been deleted from the system", null);
        }
        Rental rental = optionalRental.get();
        RentalDto rentalDto = new RentalDto();
        rentalDto.setUid(rental.getUid());
        rentalDto.setUnitName(rental.getUnitSection().getUnit().getName());
        rentalDto.setUnitSectionUid(rental.getUnitSection().getUid());
        rentalDto.setUnitSectionName(rental.getUnitSection().getName());
        rentalDto.setClientUid(rental.getClient().getUid());
        String clientName = rental.getClient().getUser().getFirstname() +" "+rental.getClient().getUser().getMiddleName()+" "+rental.getClient().getUser().getLastname();
        rentalDto.setClientName(clientName);
        rentalDto.setCurrency(rental.getCurrency());
        rentalDto.setRentalStatus(rental.getRentalStatus());
        rentalDto.setPrice(rental.getRentalAmount());
        rentalDto.setBillStatus(rental.getBillStatus());
        rentalDto.setEndDate(rental.getEndDate().toString());
        rentalDto.setStartDate(rental.getStartDate().toString());
        return new Response<>(true, ResponseCode.SUCCESS, "Success", rentalDto);
    }

    @Override
    public Response<Rental> delete(String uid){
        try {
            Optional<Rental> optionalRental = getOptionalByUid(uid);
            if (optionalRental.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Rental record could not be found or may have been deleted from the system", null);
            }
            rentalRepository.delete(optionalRental.get());
            return new Response<>(true, ResponseCode.SUCCESS, "Rental record deleted successfully", null);
        }
        catch (Exception e){
            log.error("***** Error on deleting rental record: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INVALID_INPUT, "Error when saving rental record", null);
        }
    }

    @Override
    public List<RentalDto> rentals(){
        if (!rentalRepository.findAll().isEmpty()){
            List<RentalDto> rentalDtoList = new ArrayList<>();
            for (Rental rental : rentalRepository.findAll()){
                RentalDto rentalDto = new RentalDto();
                String clientName = rental.getClient().getUser().getFirstname()+" "+rental.getClient().getUser().getMiddleName()+" "+rental.getClient().getUser().getLastname();
                rentalDto.setClientName(clientName);
                rentalDto.setUid(rental.getUid());
                rentalDto.setClientUid(rental.getClient().getUid());
                rentalDto.setRentalStatus(rental.getRentalStatus());
                rentalDto.setStartDate(rental.getStartDate().toString());
                rentalDto.setEndDate(rental.getEndDate().toString());
                rentalDto.setBillStatus(rental.getBillStatus());
                rentalDto.setPrice(rental.getRentalAmount());
                rentalDto.setCurrency(rental.getCurrency());
                rentalDto.setUnitSectionName(rental.getUnitSection().getName());
                rentalDto.setUnitSectionUid(rental.getUnitSection().getUid());
                rentalDtoList.add(rentalDto);
            }
            return rentalDtoList;
        }
        return Collections.emptyList();
    }

    @Override
    public List<RentalDto> myRentals(String userUid){
        List<Property> properties = propertyService.getMyProperties(userUid);
        if (!properties.isEmpty()){
            List<Long> propertyIds = new ArrayList<>();
            for (Property property: properties){
                propertyIds.add(property.getId());
            }
            if (!rentalRepository.findAllByPropertyIdIn(propertyIds).isEmpty()){
                List<RentalDto> rentalDtoList = new ArrayList<>();
                for (Rental rental : rentalRepository.findAllByPropertyIdIn(propertyIds)){
                    RentalDto rentalDto = new RentalDto();
                    log.info("Rental list size is: {}",rentalDtoList.size());
                    String clientName = rental.getClient().getUser().getFirstname()+" "+rental.getClient().getUser().getMiddleName()+" "+rental.getClient().getUser().getLastname();
                    rentalDto.setClientName(clientName);
                    rentalDto.setStartDate(rental.getStartDate().toString());
                    rentalDto.setUid(rental.getUid());
                    rentalDto.setClientUid(rental.getClient().getUid());
                    rentalDto.setRentalStatus(rental.getRentalStatus());
                    rentalDto.setPrice(rental.getRentalAmount());
                    rentalDto.setCurrency(rental.getCurrency());
                    rentalDto.setBillStatus(rental.getBillStatus());
                    rentalDto.setEndDate(rental.getEndDate().toString());
                    rentalDto.setUnitSectionName(rental.getUnitSection().getName());
                    rentalDto.setUnitSectionUid(rental.getUnitSection().getUid());
                    rentalDtoList.add(rentalDto);
                }
                return rentalDtoList;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<RentalDto> myPropertyRentalsEndingThisMonth(String userUid){
        List<Long> propertyIds = propertyService.getMyPropertyIds(userUid);
        if (!propertyIds.isEmpty()){
//            log.info("Property Ids: {}",propertyIds);
            List<RentalDto> rentalList = new ArrayList<>();
            for (Rental rental : rentalRepository.findMyPropertiesRentalsEndingWithin30Days(propertyIds)){
                RentalDto rentalDto = new RentalDto();
//                log.info("rentals list {}", rentalList.size());
                String clientName = rental.getClient().getUser().getFirstname()+" "+rental.getClient().getUser().getMiddleName()+" "+rental.getClient().getUser().getLastname();
                String clientMobile = rental.getClient().getUser().getMsisdn();
                clientMobile = ("0"+clientMobile.substring(clientMobile.length() -9));
                rentalDto.setClientName(clientName);
                rentalDto.setClientMobile(clientMobile);
                rentalDto.setClientUid(rental.getClient().getUid());
                rentalDto.setUnitName(rental.getUnitSection().getUnit().getName());
                rentalDto.setPropertyName(rental.getUnitSection().getUnit().getProperty().getName());
                rentalDto.setUid(rental.getUid());
                rentalDto.setStartDate(rental.getStartDate().toString());
                rentalDto.setEndDate(rental.getEndDate().toString());
                rentalDto.setRentalStatus(rental.getRentalStatus());
                rentalDto.setPrice(rental.getRentalAmount());
                rentalDto.setCurrency(rental.getCurrency());
                rentalDto.setBillStatus(rental.getBillStatus());
                rentalDto.setUnitSectionName(rental.getUnitSection().getName());
                rentalDto.setUnitSectionUid(rental.getUnitSection().getUid());
                rentalList.add(rentalDto);
            }
            return rentalList;
        }
        return Collections.emptyList();
    }

    @Override
    public List<RentalDto> clientRentals(String clientUid){

        Optional<Client> optionalClient = clientService.getOptionalByUid(clientUid);
        if (optionalClient.isEmpty()){
            return Collections.emptyList();
        }
        Client client = optionalClient.get();
        List<Rental> rentals = rentalRepository.findAllByClientAndRentalStatusNot(client,RentalStatus.EXPIRED);
        if (!rentals.isEmpty()){
            List<RentalDto> rentalDtoList = new ArrayList<>();
            for (Rental rental : rentals){
                RentalDto rentalDto = new RentalDto();
                rentalDto.setBillStatus(rental.getBillStatus());
                rentalDto.setUnitSectionName(rental.getUnitSection().getName());
                rentalDto.setUnitSectionUid(rental.getUnitSection().getUid());
                rentalDto.setUid(rental.getUid());
                rentalDto.setStartDate(rental.getStartDate().toString());
                rentalDto.setEndDate(rental.getEndDate().toString());
                rentalDto.setRentalStatus(rental.getRentalStatus());
                rentalDto.setPrice(rental.getRentalAmount());
                rentalDto.setCurrency(rental.getCurrency());
                rentalDto.setUnitName(rental.getUnitSection().getUnit().getName());
                rentalDtoList.add(rentalDto);
            }
            return rentalDtoList;
        }
        return Collections.emptyList();
    }

    @Override
    public List<RentalDto> myExpiredPropertyRentals(String userUid){
        List<Long> propertyIds = propertyService.getMyPropertyIds(userUid);
        if (!propertyIds.isEmpty()){
//            log.info("Property Ids: {}",propertyIds);
            List<RentalDto> rentalList = new ArrayList<>();
            for (Rental rental : rentalRepository.findMyExpiredPropertiesRentalsContracts(propertyIds)){
                RentalDto rentalDto = new RentalDto();
//                log.info("rentals list {}", rentalList.size());
                String clientName = rental.getClient().getUser().getFirstname()+" "+rental.getClient().getUser().getMiddleName()+" "+rental.getClient().getUser().getLastname();
                String clientMobile = rental.getClient().getUser().getMsisdn();
                clientMobile = ("0"+clientMobile.substring(clientMobile.length() -9));
                rentalDto.setClientName(clientName);
                rentalDto.setClientMobile(clientMobile);
                rentalDto.setClientUid(rental.getClient().getUid());
                rentalDto.setUnitName(rental.getUnitSection().getUnit().getName());
                rentalDto.setPropertyName(rental.getUnitSection().getUnit().getProperty().getName());
                rentalDto.setUid(rental.getUid());
                rentalDto.setStartDate(rental.getStartDate().toString());
                rentalDto.setEndDate(rental.getEndDate().toString());
                rentalDto.setRentalStatus(rental.getRentalStatus());
                rentalDto.setPrice(rental.getRentalAmount());
                rentalDto.setCurrency(rental.getCurrency());
                rentalDto.setBillStatus(rental.getBillStatus());
                rentalDto.setUnitSectionName(rental.getUnitSection().getName());
                rentalDto.setUnitSectionUid(rental.getUnitSection().getUid());
                rentalList.add(rentalDto);
            }
            return rentalList;
        }
        return Collections.emptyList();
    }

}
