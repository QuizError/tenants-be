package to.co.divinesolutions.tenors.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.UnitSection;
import to.co.divinesolutions.tenors.entity.UnitSectionDefinition;

import java.util.Optional;

@Repository
public interface UnitSectionDefinitionRepository extends JpaRepository<UnitSectionDefinition,Long> {
    Optional<UnitSectionDefinition> findFirstByUid(String uid);

    boolean existsByUnitSection(UnitSection unitSection);
    void  deleteAllByUnitSection(UnitSection unitSection);
}
