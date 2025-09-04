package to.co.divinesolutions.tenors.property.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import to.co.divinesolutions.tenors.entity.GroupOwnership;
import to.co.divinesolutions.tenors.entity.Property;
import to.co.divinesolutions.tenors.entity.User;
import to.co.divinesolutions.tenors.enums.PropertyFunctionStatus;
import to.co.divinesolutions.tenors.enums.PropertyOwnershipType;
import to.co.divinesolutions.tenors.ownership.service.GroupOwnershipMemberService;
import to.co.divinesolutions.tenors.ownership.service.GroupOwnershipService;
import to.co.divinesolutions.tenors.property.dto.PropertyDto;
import to.co.divinesolutions.tenors.property.repository.PropertyRepository;
import to.co.divinesolutions.tenors.uaa.service.UserService;
import to.co.divinesolutions.tenors.utils.Response;
import to.co.divinesolutions.tenors.utils.ResponseCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService{

    private final GroupOwnershipMemberService groupOwnershipMemberService;
    private final GroupOwnershipService groupOwnershipService;
    private final PropertyRepository propertyRepository;
    private final UserService userService;

    @Override
    public Response<Property> save(PropertyDto dto){
        try {

            Optional<Property> optionalProperty = getOptionalByUid(dto.getUid());
            if (dto.getUid() != null && !dto.getUid().isEmpty() && optionalProperty.isEmpty()){
                return new Response<>(false, ResponseCode.DUPLICATE, "Property could not be found or may have been deleted from the system", null);
            }

            Property property = optionalProperty.orElse(new Property());

            if (dto.getOwnershipType().equals(PropertyOwnershipType.PRIVATE)){
                Optional<User> optionalUser = userService.getOptionalByUid(dto.getOwnerUid());
                Long ownerId = optionalUser.isPresent() ? optionalUser.get().getId() : 1L;
                property.setOwnerId(ownerId);
            }else {
                Optional<GroupOwnership> optionalGroupOwnership = groupOwnershipService.getOptionalByUid(dto.getOwnerUid());
                Long ownerId = optionalGroupOwnership.isPresent() ? optionalGroupOwnership.get().getId() : 1L;
                property.setOwnerId(ownerId);
            }
            property.setName(dto.getName());
            property.setSenderName(dto.getSenderName());
            property.setStartFunction(LocalDate.now());
            property.setLocation(dto.getLocation());
            property.setOwnershipType(dto.getOwnershipType());
            property.setFunctionStatus(PropertyFunctionStatus.ACTIVE);
            Property saved = propertyRepository.save(property);

            return new Response<>(true, ResponseCode.SUCCESS, "Property saved successfully", saved);
        }
        catch (Exception e){
            log.error("***** Error on saving property: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INVALID_INPUT, "Error when saving property", null);
        }
    }
    @Override
    public Optional<Property> getOptionalByUid(String  uid){
        return uid != null && !uid.isEmpty() ? propertyRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Optional<Property> getOptionalById(Long  id){
        return id != null  ? propertyRepository.findById(id) : Optional.empty();
    }
    @Override
    public List<Property> properties(){
        return propertyRepository.findAll();
    }

    @Override
    public Response<Property> findByUid(String uid){
        try {
            Optional<Property> optionalProperty = getOptionalByUid(uid);
            return optionalProperty.map(property -> new Response<>(true, ResponseCode.SUCCESS, "Success", property)).orElseGet(() -> new Response<>(false, ResponseCode.NO_DATA_FOUND, "Property could not be found or may have been deleted from the system", null));
        }
        catch (Exception e){
            log.error("***** Error on fetching Property: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INTERNAL_SERVER_ERROR, "Error when fetching property data", null);
        }
    }

    @Override
    public Response<Property> delete(String uid){
        try {
            Optional<Property> optionalUnit = getOptionalByUid(uid);
            if (optionalUnit.isEmpty()){
                return new Response<>(false, ResponseCode.DUPLICATE, "Property could not be found or may have been deleted from the system", null);
            }
            propertyRepository.delete(optionalUnit.get());
            return new Response<>(true, ResponseCode.SUCCESS, "Property deleted successfully", null);
        }
        catch (Exception e){
            log.error("***** Error on fetching property: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INTERNAL_SERVER_ERROR, "Error when deleting property data", null);
        }
    }

    @Override
    public List<Property> getMyProperties(String userUid){
        List<GroupOwnership> groupOwnerships = groupOwnershipMemberService.listMyGroups(userUid);
        List<Property> properties = new ArrayList<>();
        for (GroupOwnership groupOwnership: groupOwnerships){
            List<Property> optionalProperties = propertyRepository.findAllByOwnerIdAndOwnershipType(groupOwnership.getId(),groupOwnership.getOwnershipType());
            properties.addAll(optionalProperties);
        }
        return properties;
    }

    @Override
    public List<Long> getMyPropertyIds(String userUid){
        List<GroupOwnership> groupOwnerships = groupOwnershipMemberService.listMyGroups(userUid);
        List<Property> properties = new ArrayList<>();
        for (GroupOwnership groupOwnership: groupOwnerships){
            List<Property> optionalProperties = propertyRepository.findAllByOwnerIdAndOwnershipType(groupOwnership.getId(),groupOwnership.getOwnershipType());
            properties.addAll(optionalProperties);
        }
        List<Long> propertyIds = new ArrayList<>();
        for (Property property: properties){
            propertyIds.add(property.getId());
        }
        return propertyIds;
    }
}
