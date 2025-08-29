package to.co.divinesolutions.tenors.uaa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import to.co.divinesolutions.tenors.uaa.dto.LoginDto;
import to.co.divinesolutions.tenors.uaa.dto.UserData;
import to.co.divinesolutions.tenors.uaa.service.UserService;
import to.co.divinesolutions.tenors.utils.Response;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public Response<UserData> userLogin(@RequestBody LoginDto dto){
        return userService.userLogin(dto);
    }
}
