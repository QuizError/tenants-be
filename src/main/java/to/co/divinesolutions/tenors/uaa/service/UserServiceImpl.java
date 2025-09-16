package to.co.divinesolutions.tenors.uaa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import to.co.divinesolutions.tenors.entity.User;
import to.co.divinesolutions.tenors.uaa.dto.LoginDto;
import to.co.divinesolutions.tenors.uaa.dto.UserData;
import to.co.divinesolutions.tenors.uaa.dto.UserDto;
import to.co.divinesolutions.tenors.uaa.repository.UserRepository;
import to.co.divinesolutions.tenors.utils.Response;
import to.co.divinesolutions.tenors.utils.ResponseCode;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public Response<User> save(UserDto dto){
        try {
            Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());

            if ((dto.getUid() == null || dto.getUid().isEmpty()) && optionalUser.isPresent()){
                return new Response<>(false, ResponseCode.DUPLICATE, "User with this email already exists", null);
            }
            Optional<User> userOptional = getOptionalByUid(dto.getUid());

            if (dto.getUid() != null && !dto.getUid().isEmpty() && userOptional.isEmpty()){
                return new Response<>(false, ResponseCode.DUPLICATE, "User could not be found or may have been deleted from the system", null);
            }

            String msisdn = dto.getMobileNo() != null ? dto.getMobileNo() : "0710203040";
            log.info("user msisdn: {}",msisdn);
            msisdn = "255"+msisdn.substring(msisdn.length() -9);
            User user = userOptional.orElse(new User());
            user.setLastname(dto.getLastname());
            user.setMiddleName(dto.getMiddleName());
            user.setFirstname(dto.getFirstname());
            user.setEmail(dto.getEmail());
            user.setMsisdn(msisdn);
            user.setGender(dto.getGender());
            user.setPassword(dto.getUid() == null  || dto.getUid().isEmpty() ? dto.getPassword() : user.getPassword());
            user.setIdNumber(dto.getIdNumber());
            user.setUserType(dto.getUserType());
            User saved = userRepository.save(user);

            return new Response<>(true, ResponseCode.SUCCESS, "User saved successfully", saved);
        }
        catch (Exception e){
            log.error("***** Error on saving user: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INVALID_INPUT, "Error when saving user", null);
        }
    }

    @Override
    public Optional<User> getOptionalByUid(String  uid){
        return uid != null && !uid.isEmpty() ? userRepository.findFirstByUid(uid) : Optional.empty();
    }
    @Override
    public void saveClientUser(User user){
        userRepository.save(user);
    }

    @Override
    public Response<User> findByUid(String uid){
        try {
            Optional<User> optionalUser = getOptionalByUid(uid);
            return optionalUser.map(user -> new Response<>(true, ResponseCode.SUCCESS, "Success", user)).orElseGet(() -> new Response<>(false, ResponseCode.NO_DATA_FOUND, "User could not be found or may have been deleted from the system", null));
        }
        catch (Exception e){
            log.error("***** Error on fetching user: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INTERNAL_SERVER_ERROR, "Error when fetching user data", null);
        }
    }

    @Override
    public Response<User> delete(String uid){
        try {
            Optional<User> optionalUnit = getOptionalByUid(uid);
            if (optionalUnit.isEmpty()){
                return new Response<>(false, ResponseCode.DUPLICATE, "User could not be found or may have been deleted from the system", null);
            }
            userRepository.delete(optionalUnit.get());
            return new Response<>(true, ResponseCode.SUCCESS, "User deleted successfully", null);
        }
        catch (Exception e){
            log.error("***** Error on fetching user: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INTERNAL_SERVER_ERROR, "Error when deleting user data", null);
        }
    }

    @Override
    public List<User> users(){
        return userRepository.findAll();
    }

    @Override
    public Page<User> usersPageable(Pageable pageable) {
        return userRepository.findActiveUsers(pageable);
    }

    @Override
    public Response<UserData> userLogin(LoginDto dto){
        try {
            Optional<User> optionalUser = userRepository.findFirstByEmailAndPassword(dto.getUsername(),dto.getPassword());
            if (optionalUser.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Invalid credentials please try again", null);
            }
            User user = optionalUser.get();
            UserData userData = new UserData();
            userData.setEmail(user.getEmail());
            userData.setPassword(user.getPassword());
            userData.setRole("Admin");
            userData.setUid(user.getUid());
            userData.setEmployeeId(user.getId());
            userData.setUserType(user.getUserType());
            userData.setMobileNo(userData.getMobileNo());
            userData.setFullName(user.getFirstname()+" "+user.getLastname());
            return new Response<>(true, ResponseCode.SUCCESS, "Success", userData);
        }
        catch (Exception e){
            log.error("***** Error on fetching user: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INTERNAL_SERVER_ERROR, "Error when deleting user data", null);
        }
    }


}
