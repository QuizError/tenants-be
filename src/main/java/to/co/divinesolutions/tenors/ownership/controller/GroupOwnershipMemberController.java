package to.co.divinesolutions.tenors.ownership.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import to.co.divinesolutions.tenors.entity.GroupOwnership;
import to.co.divinesolutions.tenors.entity.GroupOwnershipMember;
import to.co.divinesolutions.tenors.ownership.dto.GroupOwnershipMemberDto;
import to.co.divinesolutions.tenors.ownership.service.GroupOwnershipMemberService;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;

@RestController
@RequestMapping("group-ownership-members")
public class GroupOwnershipMemberController {
    @Autowired
    private GroupOwnershipMemberService groupOwnershipMemberService;

    @PostMapping
    public Response<GroupOwnershipMember> save(@RequestBody GroupOwnershipMemberDto dto){
        return groupOwnershipMemberService.save(dto);
    }

    @GetMapping("{uid}")
    public Response<GroupOwnershipMember> save(@PathVariable String uid){
        return groupOwnershipMemberService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<GroupOwnershipMember> delete(@PathVariable String uid){
        return groupOwnershipMemberService.delete(uid);
    }

    @GetMapping
    public List<GroupOwnershipMember> groupOwnershipMemberList(){
        return groupOwnershipMemberService.groupOwnershipMembers();
    }

    @GetMapping("/members/{uid}")
    public List<GroupOwnershipMember> getGroupMembers(@PathVariable String uid){
        return groupOwnershipMemberService.listGroupMembers(uid);
    }

    @GetMapping("/groups/{uid}")
    public List<GroupOwnership> getMyGroups(@PathVariable String uid){
        return groupOwnershipMemberService.listMyGroups(uid);
    }
}
