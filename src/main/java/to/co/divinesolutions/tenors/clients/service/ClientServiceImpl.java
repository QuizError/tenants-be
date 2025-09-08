package to.co.divinesolutions.tenors.clients.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import to.co.divinesolutions.tenors.bill_and_payment.repository.BillRepository;
import to.co.divinesolutions.tenors.bill_and_payment.repository.PaymentRepository;
import to.co.divinesolutions.tenors.clients.dto.ClientDto;
import to.co.divinesolutions.tenors.clients.repository.ClientRepository;
import to.co.divinesolutions.tenors.entity.*;
import to.co.divinesolutions.tenors.enums.BillType;
import to.co.divinesolutions.tenors.enums.UserType;
import to.co.divinesolutions.tenors.rentals.repository.RentalRepository;
import to.co.divinesolutions.tenors.uaa.dto.UserDto;
import to.co.divinesolutions.tenors.uaa.service.UserService;
import to.co.divinesolutions.tenors.utils.Response;
import to.co.divinesolutions.tenors.utils.ResponseCode;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;
    private final RentalRepository rentalRepository;
    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final UserService userService;

    @Override
    public Response<Client> save(ClientDto dto){

        Optional<Client> optionalClient = getOptionalByUid(dto.getUid());


        if (dto.getUid() != null && !dto.getUid().isEmpty() && optionalClient.isEmpty()){
            return new Response<>(false, ResponseCode.NO_DATA_FOUND,"Client could not be found or may have been deleted from the system", null);
        }
        Client client = optionalClient.orElse(new Client());
        client.setDob(dto.getDob() != null && !dto.getDob().isEmpty() ? LocalDate.parse(dto.getDob()) : null);
        client.setOccupation(dto.getOccupation());

        Optional<User> optionalUser = userService.getOptionalByUid(dto.getOwnerUid());
        optionalUser.ifPresent(user -> client.setCreatedBy(user.getId()));

        if (dto.getUserUid() != null && !dto.getUserUid().isEmpty() && userService.getOptionalByUid(dto.getUserUid()).isPresent()){
            User user = userService.getOptionalByUid(dto.getUserUid()).get();
            user.setEmail(dto.getEmail());
            user.setFirstname(dto.getFirstname());
            user.setLastname(dto.getLastname());
            user.setMiddleName(dto.getMiddleName());
            user.setPassword(dto.getPassword());
            user.setUserType(UserType.TENANT);
            user.setMsisdn("255"+dto.getMsisdn().substring(dto.getMsisdn().length() -9));
            user.setGender(dto.getGender());
            user.setIdNumber(dto.getIdNumber());
            userService.saveClientUser(user);
            client.setUser(user);
        }else {
            UserDto userDto = new UserDto();
            userDto.setEmail(dto.getEmail());
            userDto.setFirstname(dto.getFirstname());
            userDto.setLastname(dto.getLastname());
            userDto.setMiddleName(dto.getMiddleName());
            userDto.setPassword(dto.getPassword());
            userDto.setUserType(UserType.TENANT);
            userDto.setMobileNo(dto.getMsisdn());
            userDto.setGender(dto.getGender());
            userDto.setIdNumber(dto.getIdNumber());

            Response<User>  savedUSerResponse =  userService.save(userDto);
            User user = savedUSerResponse.getData();
            client.setUser(user);
        }

        Client savedClient = clientRepository.save(client);

        return new Response<>(true,ResponseCode.SUCCESS,"Client saved successfully", savedClient);
    }

    @Override
    public Optional<Client> getOptionalByUid(String uid){
        return uid != null && !uid.isEmpty() ? clientRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Response<Client> findByUid(String uid){
        try {
            Optional<Client> optionalClient = getOptionalByUid(uid);

            return optionalClient.map(client -> new Response<>(true, ResponseCode.SUCCESS, "Success", client)).orElseGet(() -> new Response<>(false, ResponseCode.NO_DATA_FOUND, "Client could not be found or may have been deleted from the system", null));
        }
        catch (Exception e){
            return new Response<>(false,ResponseCode.INTERNAL_SERVER_ERROR,"Error occurred when fetching client", null);

        }
    }

    @Override
    @Transactional
    public Response<Client> delete(String uid){
        try {
            Optional<Client> optionalClient = getOptionalByUid(uid);

            if (optionalClient.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND,"Client could not be found or may have been deleted from the system", null);
            }
            Client client = optionalClient.get();
            List<Rental> clientRentals = rentalRepository.findAllByClient(client);
            for (Rental rental: clientRentals){
                Optional<Bill> optionalBill = billRepository.findFirstByBillableIdAndBillType(rental.getId(), BillType.RENTALS);
                if (optionalBill.isPresent()){
                    Bill bill = optionalBill.orElse(null);
                    paymentRepository.deleteAllByBill(bill);
                    billRepository.delete(bill);
                    rentalRepository.deleteAllByClient(client);
                }
            }
            clientRepository.delete(client);
            return new Response<>(true,ResponseCode.SUCCESS,"Client deleted successfully", null);
        }
        catch (Exception e){
            log.error("Error {} when deleting client",e.getMessage());
            e.printStackTrace();
            return new Response<>(false,ResponseCode.INTERNAL_SERVER_ERROR,"Error occurred when deleting client", null);
        }
    }

    @Override
    public List<Client> listAllClients(){
        return clientRepository.findAll();
    }

    @Override
    public List<Client> listAllMyClients(String userUid){

        Optional<User> optionalUser = userService.getOptionalByUid(userUid);
        if (optionalUser.isEmpty()){
            return Collections.emptyList();
        }
        User user = optionalUser.get();
        return clientRepository.findAllByCreatedBy(user.getId());
    }
}
