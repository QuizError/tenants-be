package to.co.divinesolutions.tenors.ownership.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import to.co.divinesolutions.tenors.entity.*;
import to.co.divinesolutions.tenors.ownership.dto.GroupOwnershipMemberDto;
import to.co.divinesolutions.tenors.ownership.repository.GroupOwnershipMemberRepository;
import to.co.divinesolutions.tenors.uaa.service.UserService;
import to.co.divinesolutions.tenors.utils.Response;
import to.co.divinesolutions.tenors.utils.ResponseCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupOwnershipMemberServiceImpl implements GroupOwnershipMemberService{

    private final GroupOwnershipMemberRepository groupOwnershipMemberRepository;
    private final GroupOwnershipService groupOwnershipService;
    private final UserService userService;

    @Override
    public Response<GroupOwnershipMember> save(GroupOwnershipMemberDto dto){
        try {
            Optional<User> optionalUser = userService.getOptionalByUid(dto.getUserUid());
            if (optionalUser.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "User could not be found or may have been deleted from the system", null);
            }
            User user = optionalUser.get();

            Optional<GroupOwnership> optionalGroup = groupOwnershipService.getOptionalByUid(dto.getGroupUid());
            if (optionalGroup.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Group could not be found or may have been deleted from the system", null);
            }
            GroupOwnership group = optionalGroup.get();

            Optional<GroupOwnershipMember> optionalGroupOwnershipMember = getOptionalByUid(dto.getUid());

            if (dto.getUid() != null && !dto.getUid().isEmpty() && optionalGroupOwnershipMember.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Group (owner) member unit could not be found or may have been deleted from the system", null);
            }

            GroupOwnershipMember groupOwnershipMember = optionalGroupOwnershipMember.orElse(new GroupOwnershipMember());
            groupOwnershipMember.setUser(user);
            groupOwnershipMember.setGroup(group);
            groupOwnershipMember.setMemberStatus(dto.getMemberStatus());
            GroupOwnershipMember saved = groupOwnershipMemberRepository.save(groupOwnershipMember);

            return new Response<>(true, ResponseCode.SUCCESS, "Group (owners) member saved successfully", saved);
        }
        catch (Exception e){
            log.error("***** Error on saving Group (owners) member: {}",e.getMessage());
            return new Response<>(false, ResponseCode.INVALID_INPUT, "Error when saving Group (owners) member", null);
        }
    }

    public Optional<GroupOwnershipMember> getOptionalByUid(String  uid){
        return uid != null && !uid.isEmpty() ? groupOwnershipMemberRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Response<GroupOwnershipMember> findByUid(String uid){
        try {
            Optional<GroupOwnershipMember> optionalProperty = getOptionalByUid(uid);
            return optionalProperty.map(property -> new Response<>(true, ResponseCode.SUCCESS, "Success", property)).orElseGet(() -> new Response<>(false, ResponseCode.DUPLICATE, "Group (owners) Member could not be found or may have been deleted from the system", null));
        }
        catch (Exception e){
            log.error("***** Error on fetching Group Ownership: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INTERNAL_SERVER_ERROR, "Error when fetching group (owners) member data", null);
        }
    }

    @Override
    public Response<GroupOwnershipMember> delete(String uid){
        try {
            Optional<GroupOwnershipMember> optionalGroupOwnershipMember = getOptionalByUid(uid);
            if (optionalGroupOwnershipMember.isEmpty()){
                return new Response<>(false, ResponseCode.DUPLICATE, "Group (owners) Member could not be found or may have been deleted from the system", null);
            }
            groupOwnershipMemberRepository.delete(optionalGroupOwnershipMember.get());
            return new Response<>(true, ResponseCode.SUCCESS, "Group (owners) Member deleted successfully", null);
        }
        catch (Exception e){
            log.error("***** Error on fetching Group (owners) Member: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INTERNAL_SERVER_ERROR, "Error when deleting Group (owners) Member data", null);
        }
    }

    @Override
    public List<GroupOwnershipMember> groupOwnershipMembers(){
        return groupOwnershipMemberRepository.findAll();
    }

    @Override
    public List<GroupOwnershipMember> listGroupMembers(String groupUid){
        try {
            Optional<GroupOwnership> optionalGroup = groupOwnershipService.getOptionalByUid(groupUid);
            if (optionalGroup.isEmpty()){
                return Collections.emptyList();
            }
            return groupOwnershipMemberRepository.findAllByGroup(optionalGroup.get());
        }
        catch (Exception e){
            log.error("Error when fetching group members");
            return Collections.emptyList();
        }
    }

    @Override
    public List<GroupOwnership> listMyGroups(String userUid){
        Optional<User> optionalGroup = userService.getOptionalByUid(userUid);
        if (optionalGroup.isPresent()){
            User user = optionalGroup.get();
            List<GroupOwnership> myGroups = new ArrayList<>();
            List<GroupOwnershipMember> myGroupMemberships = groupOwnershipMemberRepository.findAllByUser(user);
            for (GroupOwnershipMember membership : myGroupMemberships){
                myGroups.add(membership.getGroup());
            }
            return myGroups;
        }
        else {
            return Collections.emptyList();
        }
    }

}
