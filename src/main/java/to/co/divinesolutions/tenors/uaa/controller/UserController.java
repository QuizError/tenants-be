package to.co.divinesolutions.tenors.uaa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import to.co.divinesolutions.tenors.entity.User;
import to.co.divinesolutions.tenors.uaa.dto.UserDto;
import to.co.divinesolutions.tenors.uaa.service.UserService;
import to.co.divinesolutions.tenors.utils.PageRequestDto;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public Response<User> save(@RequestBody UserDto dto){
        return userService.save(dto);
    }

    @GetMapping("{uid}")
    public Response<User> save(@PathVariable String uid){
        return userService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<User> delete(@PathVariable String uid){
        return userService.delete(uid);
    }

    @GetMapping
    public List<User> userList(){
        return userService.users();
    }
    @PostMapping("search")
    public Page<User> usersList(@RequestBody PageRequestDto pageRequest) {
        return userService.usersPageable(pageRequest.toPageable());
    }
}
