package to.co.divinesolutions.tenors.ownership.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import to.co.divinesolutions.tenors.entity.GroupOwnership;
import to.co.divinesolutions.tenors.ownership.dto.GroupOwnershipDto;
import to.co.divinesolutions.tenors.ownership.service.GroupOwnershipService;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;

@RestController
@RequestMapping("group-ownerships")
public class GroupOwnershipController {

    @Autowired
    private GroupOwnershipService groupOwnershipService;

    @PostMapping
    public Response<GroupOwnership> save(@RequestBody GroupOwnershipDto dto){
        return groupOwnershipService.save(dto);
    }

    @GetMapping("{uid}")
    public Response<GroupOwnership> save(@PathVariable String uid){
        return groupOwnershipService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<GroupOwnership> delete(@PathVariable String uid){
        return groupOwnershipService.delete(uid);
    }

    @GetMapping
    public List<GroupOwnership> groupOwnerships(){
        return groupOwnershipService.groupOwnerships();
    }
}
