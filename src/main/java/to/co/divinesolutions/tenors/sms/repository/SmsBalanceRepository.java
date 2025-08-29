package to.co.divinesolutions.tenors.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.SMSBalance;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SmsBalanceRepository extends JpaRepository<SMSBalance,Long> {
    Optional<SMSBalance> findFirstByCreatedAt(LocalDate localDate);
}