package to.co.divinesolutions.tenors.rentals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.Client;
import to.co.divinesolutions.tenors.entity.Rental;
import to.co.divinesolutions.tenors.enums.RentalStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<Rental,Long> {
    Optional<Rental> findFirstByUid(String uid);
    List<Rental> findAllByPropertyIdIn(List<Long> propertyIds);
    List<Rental> findAllByClientAndRentalStatusNot(Client client, RentalStatus rentalStatus);
}
