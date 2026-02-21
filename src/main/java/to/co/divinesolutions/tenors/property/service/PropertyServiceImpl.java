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
import to.co.divinesolutions.tenors.property.dto.BroadcastDto;
import to.co.divinesolutions.tenors.property.dto.PropertyDto;
import to.co.divinesolutions.tenors.property.repository.PropertyRepository;
import to.co.divinesolutions.tenors.sms.dto.Recipient;
import to.co.divinesolutions.tenors.sms.dto.SMSDto;
import to.co.divinesolutions.tenors.sms.service.SMSService;
import to.co.divinesolutions.tenors.uaa.service.UserService;
import to.co.divinesolutions.tenors.utils.BaseEntity;
import to.co.divinesolutions.tenors.utils.Response;
import to.co.divinesolutions.tenors.utils.ResponseCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService{

    private final GroupOwnershipMemberService groupOwnershipMemberService;
    private final GroupOwnershipService groupOwnershipService;
    private final PropertyRepository propertyRepository;
    private final SMSService smsService;
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
                Long ownerId = optionalUser.map(BaseEntity::getId).orElse(null);
                property.setOwnerId(ownerId);
            }else {
                Optional<GroupOwnership> optionalGroupOwnership = groupOwnershipService.getOptionalByUid(dto.getOwnerUid());
                Long ownerId = optionalGroupOwnership.map(BaseEntity::getId).orElse(null);
                property.setOwnerId(ownerId);
            }
            property.setName(dto.getName());
            property.setHasServiceCharge(dto.getHasServiceCharge());
            property.setServiceChargeAmount(dto.getServiceChargeAmount());
            property.setServiceChargeCurrency(dto.getServiceChargeCurrency());
            property.setServiceChargeDescription(dto.getServiceChargeDescription());
            property.setContactPersonEmail(dto.getContactPersonEmail());
            property.setContactPersonMobile(dto.getContactPersonMobile());
            property.setNotifyMeEndOfContract(dto.getNotifyMeEndOfContract());
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
        List<GroupOwnership> myGroups = groupOwnershipMemberService.listMyGroups(userUid);
        for (GroupOwnership groupOwnership: myGroups){
            return propertyRepository.findAllByOwnerIdAndOwnershipType(groupOwnership.getId(),groupOwnership.getOwnershipType());
        }
        return Collections.emptyList();
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

    @Override
    public Response<SMSDto> sendBroadcastSms(BroadcastDto dto){
        Optional<Property> optionalProperty = getOptionalByUid(dto.getPropertyUid());
        if (optionalProperty.isPresent()){
            //Prepare an empty list of Recipients
            List<Recipient> recipients = new ArrayList<>();

            Property property = optionalProperty.get();
            String senderName = property.getSenderName() != null && !property.getSenderName().isEmpty() ?  property.getSenderName() : "HOMES APP";
            List<String> msisdnList = userService.userMsisdnList(property.getId());

            for (int i = 0; i < msisdnList.size(); i++) {
                Recipient recipient = new Recipient();
                recipient.setRecipient_id(i + 1);
                recipient.setDest_addr(msisdnList.get(i));
                recipients.add(recipient);
            }

            SMSDto smsDto = new SMSDto();
            smsDto.setMessage(dto.getMessage());
            smsDto.setSourceAddr(senderName);
            smsDto.setRecipients(recipients);
//            smsService.sendSms(smsDto);
            log.info("The send list is: {}",  smsDto);
            return new Response<>(true, ResponseCode.SUCCESS,"SMS request sent successfully", smsDto);
        }
        else {
            return new Response<>(false, ResponseCode.NO_DATA_FOUND,Collections.emptyList(),"Property could not be found or may have been deleted from the system");
        }
    }
}
