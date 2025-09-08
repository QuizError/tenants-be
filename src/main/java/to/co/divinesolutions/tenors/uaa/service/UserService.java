package to.co.divinesolutions.tenors.uaa.service;

import to.co.divinesolutions.tenors.entity.User;
import to.co.divinesolutions.tenors.uaa.dto.LoginDto;
import to.co.divinesolutions.tenors.uaa.dto.UserData;
import to.co.divinesolutions.tenors.uaa.dto.UserDto;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Response<User> save(UserDto dto);

    Optional<User> getOptionalByUid(String uid);

    void saveClientUser(User user);

    Response<User> findByUid(String uid);

    Response<User> delete(String uid);

    List<User> users();

    Response<UserData> userLogin(LoginDto dto);
}
