package to.co.divinesolutions.tenors.rentals.schedular;

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
import to.co.divinesolutions.tenors.property.repository.UnitSectionRepository;
import to.co.divinesolutions.tenors.rentals.repository.RentalRepository;
import to.co.divinesolutions.tenors.sms.dto.Recipient;
import to.co.divinesolutions.tenors.sms.dto.SMSDto;
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
    public void getRentalsEndingWithin30Days() {
        List<Rental> rentals = rentalRepository.findRentalsEndingWithin30Days();
        for (Rental rental : rentals){
            log.info("******** Rent of {} house {} place of TZS {} ending at {} will be sent sms",rental.getUnitSection().getUnit().getName() ,rental.getUnitSection().getName(),rental.getRentalAmount(), rental.getEndDate());
            sendEndOfContractSMSNotification(rental);
            rental.setEndOfContractNotification(true);
            rentalRepository.save(rental);
        }
    }

    void sendEndOfContractSMSNotification(Rental rental){
        SMSDto smsDto = new SMSDto();
            User user = rental.getClient().getUser();
            String clientName = user.getFirstname()+" "+user.getLastname();
            String clientMobile = user.getMsisdn();
            String formattedDate = rental.getEndDate()
                .format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH));
            Property property = rental.getUnitSection().getUnit().getProperty();
            smsDto.setMessage("Ndugu "+clientName+" tunapenda kukufahamisha kuwa Mkataba wako wa pango kwa nyumba "+rental.getUnitSection().getName()+" iliyopo "+rental.getUnitSection().getUnit().getName()+" utaisha muda wake "+formattedDate+". Tunakuhimiza ku thibitisha kuanza mkataba mwingine na kuulipia ndani ya wiki tatu kuanzia leo au kuachilia nafasi tarehe ya mwisho wa Mkataba wako. Asante kwa kuwa mpangaji wetu");
            smsDto.setSourceAddr(property.getSenderName() != null && !property.getSenderName().isEmpty() ? property.getSenderName() : "HOMES APP");
            Recipient recipient = new Recipient();
            recipient.setRecipient_id(1);
            recipient.setDest_addr(clientMobile);
            List<Recipient> recipients = Collections.singletonList(recipient);
            smsDto.setRecipients(recipients);
            smsService.sendSms(smsDto);
    }

    @Scheduled(fixedDelay = 30000)
    public void endRentalsOnEndDate() {
        log.info("***** scheduler to find expired rentals and update section availability ******");
        List<Rental> rentals = rentalRepository.findAllByEndDateLessThanEqualAndRentalStatus(LocalDate.now(),RentalStatus.ACTIVE);
        log.info("***** List count {}",rentals.size());
        for (Rental rental : rentals){
            log.info("******** Rent of {} house {} place of TZS {} ending at {} will be set expired and section will be made available",rental.getUnitSection().getUnit().getName() ,rental.getUnitSection().getName(),rental.getRentalAmount(), rental.getEndDate());
            rental.setRentalStatus(RentalStatus.EXPIRED);
            rentalRepository.save(rental);
            //unit section setting availability
            UnitSection unitSection = rental.getUnitSection();
            unitSection.setAvailable(true);
            unitSectionRepository.save(unitSection);
        }
    }
}