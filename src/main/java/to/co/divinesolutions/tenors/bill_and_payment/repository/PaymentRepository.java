package to.co.divinesolutions.tenors.bill_and_payment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.Bill;
import to.co.divinesolutions.tenors.entity.Payment;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findFirstByUid(String uid);
    void deleteAllByBill(Bill bill);
    Page<Payment> findAllByBillIn(List<Bill> bills, Pageable pageable);
}
