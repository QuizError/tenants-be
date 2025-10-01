package to.co.divinesolutions.tenors.checklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.ChecklistSignOff;

import java.util.Optional;

@Repository
public interface ChecklistSignOffRepository extends JpaRepository<ChecklistSignOff,Long> {
    Optional<ChecklistSignOff> findFirstByUid(String uid);
}
