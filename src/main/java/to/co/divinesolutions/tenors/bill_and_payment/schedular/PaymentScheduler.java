package to.co.divinesolutions.tenors.bill_and_payment.schedular;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import to.co.divinesolutions.tenors.bill_and_payment.repository.BillRepository;
import to.co.divinesolutions.tenors.entity.Bill;
import to.co.divinesolutions.tenors.entity.Rental;
import to.co.divinesolutions.tenors.enums.BillStatus;
import to.co.divinesolutions.tenors.enums.RentalStatus;
import to.co.divinesolutions.tenors.rentals.repository.RentalRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class PaymentScheduler {

    private final BillRepository billRepository;
    private final RentalRepository rentalRepository;

    @Async
    @Scheduled(fixedDelay = 30000)
    public void listOfLPNotBilledProformas() {
        List<Bill> billList = billRepository.findAllByBillStatusAndAffectedFalse(BillStatus.Paid);
        for (Bill bill : billList){
            log.info("Affecting bill with reference: {}",bill.getBillReferenceNumber());
            Optional<Rental> optionalRental = rentalRepository.findById(bill.getBillableId());
            if (optionalRental.isPresent()){
                Rental rental = optionalRental.get();
                rental.setRentalStatus(RentalStatus.ACTIVE);
                rental.setBillStatus(BillStatus.Paid);
                rentalRepository.save(rental);
                bill.setAffected(true);
                billRepository.save(bill);
            }
        }
    }
}
