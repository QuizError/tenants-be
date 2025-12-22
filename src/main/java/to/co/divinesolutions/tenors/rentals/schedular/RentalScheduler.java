package to.co.divinesolutions.tenors.rentals.schedular;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import to.co.divinesolutions.tenors.entity.Property;
import to.co.divinesolutions.tenors.entity.Rental;
import to.co.divinesolutions.tenors.entity.UnitSection;
import to.co.divinesolutions.tenors.entity.User;
import to.co.divinesolutions.tenors.enums.RentalStatus;
import to.co.divinesolutions.tenors.enums.SmsType;
import to.co.divinesolutions.tenors.property.repository.UnitSectionRepository;
import to.co.divinesolutions.tenors.rentals.repository.RentalRepository;
import to.co.divinesolutions.tenors.sms.dto.BeemSMSCallback;
import to.co.divinesolutions.tenors.sms.dto.Recipient;
import to.co.divinesolutions.tenors.sms.dto.SMSDto;
import to.co.divinesolutions.tenors.sms.dto.SentSmsBody;
import to.co.divinesolutions.tenors.sms.service.SMSService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class RentalScheduler {

    private final RentalRepository rentalRepository;
    private final UnitSectionRepository unitSectionRepository;
    private final SMSService smsService;

    @Async
    @Scheduled(fixedDelay = 30000)
    public void getRentalsEndingWithin30Days() throws JsonProcessingException {
        List<Rental> rentals = rentalRepository.findRentalsEndingWithin30Days();
        for (Rental rental : rentals){
            log.info("******** Rent of {} house {} place of TZS {} ending at {} will be sent sms",rental.getUnitSection().getUnit().getName() ,rental.getUnitSection().getName(),rental.getRentalAmount(), rental.getEndDate());
            sendEndOfContractSMSNotification(rental);
            rental.setEndOfContractNotification(true);
            rentalRepository.save(rental);
        }
    }

    void sendEndOfContractSMSNotification(Rental rental) throws JsonProcessingException {
            SMSDto smsDto = new SMSDto();
            User user = rental.getClient().getUser();
            String clientName = user.getFirstname()+" "+user.getLastname();
            String clientMobile = user.getMsisdn();
            String formattedDate = rental.getEndDate()
                .format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH));
            Property property = rental.getUnitSection().getUnit().getProperty();
            String message = "Ndugu "+clientName+" tunapenda kukufahamisha kuwa Mkataba wako wa pango kwa "+property.getName()+" kwenye nyumba "+rental.getUnitSection().getName()+" utaisha muda wake "+formattedDate+". Tunakuhimiza ku thibitisha kuanza mkataba mwingine na kuulipia ndani ya wiki tatu kuanzia leo au kuachilia nafasi tarehe ya mwisho wa Mkataba wako. Asante kwa kuwa mpangaji wetu";
            smsDto.setMessage(message);
            smsDto.setSourceAddr(property.getSenderName() != null && !property.getSenderName().isEmpty() ? property.getSenderName() : "HOMES APP");
            Recipient recipient = new Recipient();
            recipient.setRecipient_id(1);
            recipient.setDest_addr(clientMobile);
            List<Recipient> recipients = Collections.singletonList(recipient);
            smsDto.setRecipients(recipients);
            String sentSmsResponse =  smsService.sendSms(smsDto);

            //search if property has notify me to owners contact person
            if(property.getNotifyMeEndOfContract()){
                String messageToOwner = "Ndugu "+clientName+" ametumiwa ujumbe wa kukumbushwa kuwa mkataba wake wa pango kwa "+property.getName()+" kwenye nyumba "+rental.getUnitSection().getName()+" utaisha muda wake "+formattedDate+". na kuhimizwa ku thibitisha kuanza mkataba mwingine na kuulipia ndani ya wiki tatu kuanzia leo au kuachilia nafasi tarehe ya mwisho wa Mkataba";
                smsDto.setMessage(messageToOwner);
                smsDto.setSourceAddr(property.getSenderName() != null && !property.getSenderName().isEmpty() ? property.getSenderName() : "HOMES APP");
                Recipient contactPerson = new Recipient();
                contactPerson.setRecipient_id(1);
                contactPerson.setDest_addr(property.getContactPersonMobile());
                List<Recipient> contactPersons = Collections.singletonList(contactPerson);
                smsDto.setRecipients(contactPersons);
                smsService.sendSms(smsDto);
            }
            //end of check

            //saving the message sent to Client
            ObjectMapper mapper = new ObjectMapper();
            BeemSMSCallback response = mapper.readValue(sentSmsResponse, BeemSMSCallback.class);

            SentSmsBody sentSmsBody = new SentSmsBody();
            sentSmsBody.setSmsType(SmsType.CONTRACT_END_REMINDER);
            sentSmsBody.setInvalid(response.getInvalid());
            sentSmsBody.setDuplicates(response.getDuplicates());
            sentSmsBody.setSuccessful(response.isSuccessful());
            sentSmsBody.setValid(response.getValid());
            sentSmsBody.setRequestId(response.getRequest_id());
            sentSmsBody.setClientId(rental.getClient().getId());
            sentSmsBody.setMessage(message);
            sentSmsBody.setRentalId(rental.getId());
            sentSmsBody.setPropertyId(property.getId());
            sentSmsBody.setCode(response.getCode());
            smsService.saveSentSms(sentSmsBody);
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void endRentalsOnEndDate() {
        List<Rental> rentals = rentalRepository.findAllByEndDateLessThanEqualAndRentalStatus(LocalDate.now(),RentalStatus.ACTIVE);
        for (Rental rental : rentals){
            log.info("******** Rent of property {} unit {} section {} place of TZS {} ending at {} will be set expired and section will be made available",rental.getUnitSection().getUnit().getProperty().getName(),rental.getUnitSection().getUnit().getName(),rental.getUnitSection().getName(),rental.getRentalAmount(), rental.getEndDate());
            rental.setRentalStatus(RentalStatus.EXPIRED);
            rental.setRenewalConfirmed(true);
            rentalRepository.save(rental);
            //unit section setting availability
            UnitSection unitSection = rental.getUnitSection();
            unitSection.setAvailable(true);
            unitSectionRepository.save(unitSection);
        }
    }
}