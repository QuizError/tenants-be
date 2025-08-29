package to.co.divinesolutions.tenors.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.Property;
import to.co.divinesolutions.tenors.entity.Unit;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit,Long> {
    Optional<Unit> findFirstByUid(String uid);
    List<Unit> findAllByProperty(Property property);
}
