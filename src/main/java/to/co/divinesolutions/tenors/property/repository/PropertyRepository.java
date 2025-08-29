package to.co.divinesolutions.tenors.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.Property;
import to.co.divinesolutions.tenors.enums.PropertyOwnershipType;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Long> {

    Optional<Property> findFirstByUid(String uid);
    List<Property> findAllByOwnerIdAndOwnershipType(Long id, PropertyOwnershipType ownershipType);
}
