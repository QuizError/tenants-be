package to.co.divinesolutions.tenors.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.Unit;
import to.co.divinesolutions.tenors.entity.UnitSection;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitSectionRepository extends JpaRepository<UnitSection,Long> {
    Optional<UnitSection> findFirstByUid(String uid);
    List<UnitSection> findAllByUnit(Unit unit);
    List<UnitSection> findAllByAvailableTrueAndPropertyIdIn(List<Long> propertyIds);
}
