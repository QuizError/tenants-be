package to.co.divinesolutions.tenors.rentals.service;

import to.co.divinesolutions.tenors.entity.Rental;
import to.co.divinesolutions.tenors.rentals.dto.RentalDto;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;
import java.util.Optional;

public interface RentalService {
    Response<Rental> save(RentalDto dto);

    Optional<Rental> getOptionalByUid(String uid);

    Response<RentalDto> findByUid(String uid);

    Response<Rental> delete(String uid);

    List<RentalDto> rentals();

    List<RentalDto> myRentals(String userUid);

    List<RentalDto> myPropertyRentalsEndingThisMonth(String userUid);

    List<RentalDto> clientRentals(String clientUid);
}
