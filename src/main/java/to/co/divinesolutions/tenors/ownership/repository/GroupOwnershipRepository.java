package to.co.divinesolutions.tenors.ownership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.GroupOwnership;
import to.co.divinesolutions.tenors.entity.PropertyUnit;

import java.util.Optional;

@Repository
public interface GroupOwnershipRepository extends JpaRepository<GroupOwnership,Long> {
    Optional<GroupOwnership> findFirstByUid(String uid);
}
