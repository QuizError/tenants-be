package to.co.divinesolutions.tenors.rentals.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import to.co.divinesolutions.tenors.entity.Rental;
import to.co.divinesolutions.tenors.rentals.dto.RentalDto;
import to.co.divinesolutions.tenors.rentals.service.RentalService;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;

@RestController
@RequestMapping("rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping
    public Response<Rental> save(@RequestBody RentalDto dto){
        return rentalService.save(dto);
    }

    @GetMapping
    public List<RentalDto> allClients(){
        return rentalService.rentals();
    }

    @GetMapping("/owner/{uid}")
    public List<RentalDto> allMyClients(@PathVariable String uid){
        return rentalService.myRentals(uid);
    }

    @GetMapping("/contract-end/owner/{uid}")
    public List<RentalDto> myPropertyRentalsEndingThisMonth(@PathVariable String uid){
        return rentalService.myPropertyRentalsEndingThisMonth(uid);
    }

    @GetMapping("/contract-expired/owner/{uid}")
    public List<RentalDto> myExpiredPropertyRentals(@PathVariable String uid){
        return rentalService.myExpiredPropertyRentals(uid);
    }

    @GetMapping("/client/{uid}")
    public List<RentalDto> allClientRentals(@PathVariable String uid){
        return rentalService.clientRentals(uid);
    }

    @GetMapping("{uid}")
    public Response<RentalDto> findByUid(@PathVariable String uid){
        return rentalService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<Rental> delete(@PathVariable String uid){
        return rentalService.delete(uid);
    }
}
