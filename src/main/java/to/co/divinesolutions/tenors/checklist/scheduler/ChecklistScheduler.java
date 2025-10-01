package to.co.divinesolutions.tenors.checklist.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import to.co.divinesolutions.tenors.checklist.repository.ChecklistInstanceRepository;
import to.co.divinesolutions.tenors.checklist.repository.ChecklistTemplateRepository;
import to.co.divinesolutions.tenors.entity.*;
import to.co.divinesolutions.tenors.enums.ChecklistType;
import to.co.divinesolutions.tenors.enums.RentalStatus;
import to.co.divinesolutions.tenors.rentals.repository.RentalRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ChecklistScheduler {

    private final ChecklistTemplateRepository checklistTemplateRepository;
    private final ChecklistInstanceRepository checklistInstanceRepository;
    private final RentalRepository rentalRepository;

    @Async
    @Scheduled(fixedDelay = 30000)
    public void listOfLPNotBilledProformas() {
        try {
            List<Rental> rentalList = rentalRepository.findAllByChecklistCreatedFalseAndRentalStatus(RentalStatus.ACTIVE);
            for (Rental rental : rentalList){
//            log.info("Creating checklist Instance for Rental with uid: {}",rental.getUid());
                Optional<ChecklistTemplate> optionalChecklistTemplate = checklistTemplateRepository.findFirstByPropertyAndChecklistTypeAndActiveTrue(rental.getUnitSection().getUnit().getProperty(), ChecklistType.MOVE_IN);
                if (optionalChecklistTemplate.isPresent()){
                    ChecklistInstance checklistInstance = new ChecklistInstance();
                    //prepare variable for Title
                    String titleVariables = rental.getUnitSection().getName()+" of "+ rental.getUnitSection().getUnit().getProperty().getName()+" from "+rental.getStartDate()+" to "+rental.getEndDate();
                    log.info("Checklist of Rent Contract for {}" ,titleVariables);
                    checklistInstance.setTitle("Checklist of Rent Contract for "+ titleVariables);
                    checklistInstance.setPropertyId(rental.getPropertyId());
                    checklistInstance.setRental(rental);
                    checklistInstance.setTemplate(optionalChecklistTemplate.get());
                    checklistInstanceRepository.save(checklistInstance);
                    rental.setChecklistCreated(true);
                    rentalRepository.save(rental);
                }
            }
        }
        catch (Exception e){
            log.info("Error when crating checklist instance {}" ,e.getMessage());
        }
    }
}