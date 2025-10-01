package to.co.divinesolutions.tenors.checklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.ChecklistInstance;

import java.util.Optional;
@Repository
public interface ChecklistInstanceRepository extends JpaRepository<ChecklistInstance,Long> {
    Optional<ChecklistInstance> findFirstByUid(String uid);
}
