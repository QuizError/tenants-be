package to.co.divinesolutions.tenors.checklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.ChecklistItemInstance;

import java.util.Optional;

@Repository
public interface ChecklistItemInstanceRepository extends JpaRepository<ChecklistItemInstance,Long> {
    Optional<ChecklistItemInstance> findFirstByUid(String uid);
}
