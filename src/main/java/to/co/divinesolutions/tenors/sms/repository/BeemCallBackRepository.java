package to.co.divinesolutions.tenors.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.SMSCallBackHistory;

@Repository
public interface BeemCallBackRepository extends JpaRepository<SMSCallBackHistory,Long> {
}