package to.co.divinesolutions.tenors.bill_and_payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.bill_and_payment.projection.SoftwareCommissionUnpaid;
import to.co.divinesolutions.tenors.entity.Bill;
import to.co.divinesolutions.tenors.enums.BillStatus;
import to.co.divinesolutions.tenors.enums.BillType;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill,Long> {
    Optional<Bill> findFirstByUid(String uid);
    Optional<Bill> findFirstByBillableIdAndBillType(Long id, BillType billType);
    Optional<Bill> findFirstByBillReferenceNumber(String referenceNumber);
    List<Bill> findAllByBillStatusAndAffectedFalse(BillStatus billStatus);
    List<Bill> findAllByPropertyIdIn(List<Long> propertyIds);
    @Query(value = """
    	select * from get_user_group_bills(:timeLine)
    		""",nativeQuery = true)
    List<SoftwareCommissionUnpaid>getUnpaidSoftwareCommission(@Param("timeLine") String timeLine);
}
