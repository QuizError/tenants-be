package to.co.divinesolutions.tenors.ownership.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import to.co.divinesolutions.tenors.entity.GroupOwnership;
import to.co.divinesolutions.tenors.ownership.dto.GroupOwnershipDto;
import to.co.divinesolutions.tenors.ownership.repository.GroupOwnershipRepository;
import to.co.divinesolutions.tenors.utils.Response;
import to.co.divinesolutions.tenors.utils.ResponseCode;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupOwnershipServiceImpl implements GroupOwnershipService {

    private final GroupOwnershipRepository groupOwnershipRepository;

    @Override
    public Response<GroupOwnership> save(GroupOwnershipDto dto){
        try {

            Optional<GroupOwnership> optionalGroupOwnership = getOptionalByUid(dto.getUid());

            if (dto.getUid() != null && !dto.getUid().isEmpty() && optionalGroupOwnership.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Group ownership could not be found or may have been deleted from the system", null);
            }

            GroupOwnership groupOwnership = optionalGroupOwnership.orElse(new GroupOwnership());
            groupOwnership.setOwnershipType(dto.getOwnershipType());
            groupOwnership.setName(dto.getName());
            GroupOwnership saved = groupOwnershipRepository.save(groupOwnership);

            return new Response<>(true, ResponseCode.SUCCESS, "Group ownership type saved successfully", saved);
        }
        catch (Exception e){
            log.error("***** Error on saving group ownership type: {}",e.getMessage());
            return new Response<>(false, ResponseCode.INVALID_INPUT, "Error when saving group ownership type", null);
        }
    }
    @Override
    public Optional<GroupOwnership> getOptionalByUid(String  uid){
        return uid != null && !uid.isEmpty() ? groupOwnershipRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Response<GroupOwnership> findByUid(String uid){
        try {
            Optional<GroupOwnership> optionalProperty = getOptionalByUid(uid);
            return optionalProperty.map(property -> new Response<>(true, ResponseCode.SUCCESS, "Success", property)).orElseGet(() -> new Response<>(false, ResponseCode.DUPLICATE, "Group Ownership could not be found or may have been deleted from the system", null));
        }
        catch (Exception e){
            log.error("***** Error on fetching Group Ownership: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INTERNAL_SERVER_ERROR, "Error when fetching group ownership data", null);
        }
    }

    @Override
    public Response<GroupOwnership> delete(String uid){
        try {
            Optional<GroupOwnership> optionalGroupOwnership = getOptionalByUid(uid);
            if (optionalGroupOwnership.isEmpty()){
                return new Response<>(false, ResponseCode.DUPLICATE, "Group Ownership could not be found or may have been deleted from the system", null);
            }
            groupOwnershipRepository.delete(optionalGroupOwnership.get());
            return new Response<>(true, ResponseCode.SUCCESS, "Group Ownership deleted successfully", null);
        }
        catch (Exception e){
            log.error("***** Error on fetching Group Ownership: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INTERNAL_SERVER_ERROR, "Error when deleting Group Ownership data", null);
        }
    }
    @Override
    public List<GroupOwnership> groupOwnerships(){
        return groupOwnershipRepository.findAll();
    }
}
