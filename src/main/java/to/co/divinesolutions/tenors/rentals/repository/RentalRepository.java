package to.co.divinesolutions.tenors.rentals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.Client;
import to.co.divinesolutions.tenors.entity.Rental;
import to.co.divinesolutions.tenors.enums.RentalStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<Rental,Long> {
    Optional<Rental> findFirstByUid(String uid);
    List<Rental> findAllByClient(Client client);
    void deleteAllByClient(Client client);
    List<Rental> findAllByPropertyIdIn(List<Long> propertyIds);
    List<Rental> findAllByClientAndRentalStatusNot(Client client, RentalStatus rentalStatus);
    List<Rental> findAllByChecklistCreatedFalseAndRentalStatus(RentalStatus rentalStatus);
    @Query(value = "SELECT * FROM rentals WHERE end_of_contract_notification = false AND end_date BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '30 days'",
            nativeQuery = true)
    List<Rental> findRentalsEndingWithin30Days();

    @Query(value = "SELECT * FROM rentals WHERE property_id in (:propertyIds) AND end_date BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '30 days'",
            nativeQuery = true)
    List<Rental> findMyPropertiesRentalsEndingWithin30Days(List<Long> propertyIds);
//    List<Rental> findAllByEndDateAndRentalStatus(LocalDate endDate,RentalStatus rentalStatus);
    @Query(value = "SELECT * FROM rentals WHERE rental_status='EXPIRED' AND renewal_confirmed=true",
            nativeQuery = true)
    List<Rental> findMyExpiredPropertiesRentalsContracts(List<Long> propertyIds);
    List<Rental> findAllByEndDateLessThanEqualAndRentalStatus(LocalDate endDate, RentalStatus rentalStatus);

}
