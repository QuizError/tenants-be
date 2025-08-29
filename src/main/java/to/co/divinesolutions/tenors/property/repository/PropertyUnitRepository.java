package to.co.divinesolutions.tenors.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.PropertyUnit;
import to.co.divinesolutions.tenors.entity.Unit;

import java.util.Optional;

@Repository
public interface PropertyUnitRepository extends JpaRepository<PropertyUnit,Long> {
    Optional<PropertyUnit> findFirstByUid(String uid);
}
