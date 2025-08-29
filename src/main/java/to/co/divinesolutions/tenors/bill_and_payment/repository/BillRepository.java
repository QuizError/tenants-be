package to.co.divinesolutions.tenors.bill_and_payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.Bill;
import to.co.divinesolutions.tenors.enums.BillStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill,Long> {
    Optional<Bill> findFirstByUid(String uid);
    Optional<Bill> findFirstByBillReferenceNumber(String referenceNumber);
    List<Bill> findAllByBillStatusAndAffectedFalse(BillStatus billStatus);
}
