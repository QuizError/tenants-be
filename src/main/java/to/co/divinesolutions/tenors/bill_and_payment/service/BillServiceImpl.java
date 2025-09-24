package to.co.divinesolutions.tenors.bill_and_payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import to.co.divinesolutions.tenors.bill_and_payment.dto.BillDetails;
import to.co.divinesolutions.tenors.bill_and_payment.dto.BillDto;
import to.co.divinesolutions.tenors.bill_and_payment.dto.BillItemDto;
import to.co.divinesolutions.tenors.bill_and_payment.projection.SoftwareCommissionUnpaid;
import to.co.divinesolutions.tenors.bill_and_payment.repository.BillRepository;
import to.co.divinesolutions.tenors.entity.*;
import to.co.divinesolutions.tenors.enums.BillStatus;
import to.co.divinesolutions.tenors.property.service.PropertyService;
import to.co.divinesolutions.tenors.rentals.repository.RentalRepository;
import to.co.divinesolutions.tenors.utils.Response;
import to.co.divinesolutions.tenors.utils.ResponseCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillServiceImpl implements BillService{
    private final BillRepository billRepository;
    private final PropertyService propertyService;
    private final RentalRepository rentalRepository;

    @Override
    public Response<Bill> saveBill(BillDto dto){
        try {
            Optional<Bill> optionalBill = getOptionalByUid(dto.getUid());

            if (dto.getUid() != null && !dto.getUid().isEmpty() && optionalBill.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Bill could not be found or may have been deleted from the system", null);
            }

            Bill bill = optionalBill.orElse(new Bill());
            //saving bill and its items
            bill.setBillType(dto.getBillType());
            bill.setBillableId(dto.getBillableId());
            bill.setTotalAMount(dto.getTotalAMount());
            bill.setExchangeRate(dto.getExchangeRate());
            bill.setCommission(dto.getCommission());
            bill.setAgentFee(dto.getAgentFee());
            bill.setCurrency(dto.getCurrency());
            bill.setBillDescription(dto.getBillDescription());
            bill.setBillStatus(BillStatus.WaitingForPayment);
            bill.setPropertyId(dto.getPropertyId());
            bill.setBillReferenceNumber(dto.getBillReferenceNumber());
            bill.setTotalEquivalentAmount(dto.getTotalEquivalentAmount());
            Bill saved = billRepository.save(bill);
            //saving bill Item
            for (BillItemDto billItemDto : dto.getBillItemDtoList()){
                BillItem billItem = new BillItem();
                billItem.setDescription(billItem.getDescription());
                billItem.setAmount(billItem.getAmount());
                billItem.setBillType(billItem.getBillType());
                billItem.setCurrency(billItem.getCurrency());
                billItem.setBill(saved);
            }

            return new Response<>(true, ResponseCode.SUCCESS, "Bill saved successfully", saved);
        }
        catch (Exception e){
            log.error("***** Error on saving bill: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INVALID_INPUT, "Error when saving bill", null);
        }
    }
    @Override
    public Optional<Bill> getOptionalByUid(String uid){
        return uid != null && !uid.isEmpty() ? billRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Optional<Bill> getOptionalByBillReference(String billReference){
        return billReference != null && !billReference.isEmpty() ? billRepository.findFirstByBillReferenceNumber(billReference) : Optional.empty();
    }

    @Override
    public List<BillDetails> billDetailsList(String userUid){

        List<Long> propertyIds = propertyService.getMyPropertyIds(userUid);
        List<BillDetails> billDetails = new ArrayList<>();
        for (Bill bill: billRepository.findAllByPropertyIdIn(propertyIds)){
            BillDetails billDetail = new BillDetails();
            billDetail.setBillDescription(bill.getBillDescription());
            billDetail.setBillType(bill.getBillType());
            billDetail.setAgentFee(bill.getAgentFee());
            billDetail.setThirdPartyReference(bill.getThirdPartyReference());
            billDetail.setBillReferenceNumber(bill.getBillReferenceNumber());
            billDetail.setCurrency(bill.getCurrency());
            billDetail.setPaidAmount(bill.getPaidAmount());
            billDetail.setBillStatus(bill.getBillStatus());
            billDetail.setAmountDue(bill.getAmountDue());
            billDetail.setTotalAMount(bill.getTotalAMount());
            billDetail.setCommission(bill.getCommission());
            billDetail.setCreatedAt(bill.getCreatedAt().toString());
            //get billed customer
            Optional<Rental> optionalRental = rentalRepository.findById(bill.getBillableId());
            if (optionalRental.isPresent()){
                Rental rental = optionalRental.get();
                User user = rental.getClient().getUser();
                billDetail.setCustomerName(user.getFirstname()+" "+user.getMiddleName()+" "+user.getLastname());
                billDetail.setCustomerUid(user.getUid());
            }
            billDetail.setUid(bill.getUid());
            billDetails.add(billDetail);
        }
        return billDetails;
    }

    @Override
    public List<Bill>  getPropertyBills(String userUid){
        List<Long> propertyIds = propertyService.getMyPropertyIds(userUid);
        return billRepository.findAllByPropertyIdIn(propertyIds);
    }

    @Override
    public Response<BillDetails> findByUid(String uid) {
        try {
            Optional<Bill> optionalBill = getOptionalByUid(uid);

            if (optionalBill.isEmpty()) {
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Bill could not be found or may have been deleted from the system", null);
            }
            Bill bill = optionalBill.get();
            BillDetails billDetail = new BillDetails();
            billDetail.setBillDescription(bill.getBillDescription());
            billDetail.setBillType(bill.getBillType());
            billDetail.setAgentFee(bill.getAgentFee());
            billDetail.setThirdPartyReference(bill.getThirdPartyReference());
            billDetail.setBillReferenceNumber(bill.getBillReferenceNumber());
            billDetail.setCurrency(bill.getCurrency());
            billDetail.setPaidAmount(bill.getPaidAmount());
            billDetail.setBillStatus(bill.getBillStatus());
            billDetail.setAmountDue(bill.getAmountDue());
            billDetail.setTotalAMount(bill.getTotalAMount());
            billDetail.setCommission(bill.getCommission());
            billDetail.setCreatedAt(bill.getCreatedAt().toString());
            //get billed customer
            Optional<Rental> optionalRental = rentalRepository.findById(bill.getBillableId());
            if (optionalRental.isPresent()){
                Rental rental = optionalRental.get();
                User user = rental.getClient().getUser();
                billDetail.setCustomerName(user.getFirstname()+" "+user.getMiddleName()+" "+user.getLastname());
                billDetail.setCustomerUid(user.getUid());
            }
            billDetail.setUid(bill.getUid());
            return new Response<>(true, ResponseCode.SUCCESS, "Success", billDetail);

        } catch (Exception e) {
            return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Bill could not be found or may have been deleted from the system", null);
        }
    }

    @Override
    public Response<Bill> delete(String uid) {
        try {
            Optional<Bill> optionalBill = getOptionalByUid(uid);

            if (optionalBill.isEmpty()) {
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Bill could not be found or may have been deleted from the system", null);
            }
            Bill bill = optionalBill.get();
            //to do: Delete Rental record and make section unit available before deleting bill
            billRepository.delete(bill);
            return new Response<>(true, ResponseCode.SUCCESS, "Success", optionalBill.get());
        }
        catch (Exception e) {
            return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Bill could not be found or may have been deleted from the system", null);
        }
    }

    @Override
    public void affectBill(Payment payment){
        Bill bill = payment.getBill();
        bill.setPaidAmount(payment.getPaidAmount());
        BigDecimal dueAmount = payment.getPaidAmount().subtract(bill.getTotalAMount());
        bill.setAmountDue(dueAmount);
        bill.setThirdPartyReference(payment.getThirdPartyReference());
        bill.setBillStatus(dueAmount.compareTo(BigDecimal.ZERO) == 0 ? BillStatus.Paid : BillStatus.PartyPaid);
        billRepository.save(bill);
    }

    @Override
    public List<SoftwareCommissionUnpaid> unpaidList(String uid){
        return uid!=null && !uid.isEmpty() ? billRepository.getUnpaidSoftwareCommission(uid) : Collections.emptyList();
    }
}
