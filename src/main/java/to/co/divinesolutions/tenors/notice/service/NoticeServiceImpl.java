package to.co.divinesolutions.tenors.notice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import to.co.divinesolutions.tenors.entity.*;
import to.co.divinesolutions.tenors.notice.dto.NoticeDto;
import to.co.divinesolutions.tenors.notice.dto.NoticeResponse;
import to.co.divinesolutions.tenors.notice.repository.NoticeRepository;
import to.co.divinesolutions.tenors.property.service.PropertyService;
import to.co.divinesolutions.tenors.rentals.service.RentalService;
import to.co.divinesolutions.tenors.sms.dto.Recipient;
import to.co.divinesolutions.tenors.sms.dto.SMSDto;
import to.co.divinesolutions.tenors.sms.service.SMSService;
import to.co.divinesolutions.tenors.utils.Response;
import to.co.divinesolutions.tenors.utils.ResponseCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeServiceImpl implements NoticeService{
    private final RentalService rentalService;
    private final NoticeRepository noticeRepository;
    private final PropertyService propertyService;
    private final SMSService smsService;

    @Override
    public Response<Notice> save(NoticeDto dto){
        try {

            Optional<Rental> optionalRental = rentalService.getOptionalByUid(dto.getRentalUid());
            if (optionalRental.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Rental record could not be found or may have been deleted from the system", null);
            }
            Rental rental = optionalRental.get();

            Optional<Notice> optionalNotice = getOptionalByUid(dto.getUid());

            if (dto.getUid() != null && !dto.getUid().isEmpty() && optionalNotice.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Notice could not be found or may have been deleted from the system", null);
            }

            Notice notice = optionalNotice.orElse(new Notice());
            notice.setRental(rental);
            notice.setDetails(dto.getDetails());
            notice.setMessage(dto.getMessage());
            notice.setNoticeType(dto.getNoticeType());
            notice.setPropertyId(rental.getPropertyId());
            notice.setClientId(rental.getClient().getId());
            notice.setSentTo(rental.getClient().getUser().getMsisdn());
            notice.setUnitSectionId(rental.getUnitSection().getId());
            Notice saved = noticeRepository.save(notice);

            sendNoticeSMS(rental,dto.getMessage());

            return new Response<>(true, ResponseCode.SUCCESS, "Notice saved successfully", saved);
        }
        catch (Exception e){
            log.error("***** Error on saving notice: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INVALID_INPUT, "Error when saving notice", null);
        }
    }
    @Override
    public Optional<Notice> getOptionalByUid(String uid){
        return uid != null && !uid.isEmpty() ? noticeRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Response<NoticeResponse> findByUid(String uid){
        try {
            Optional<Notice> optionalNotice = getOptionalByUid(uid);
            if (optionalNotice.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Notice could not be found or may have been deleted from the system", null);
            }
            Notice notice = optionalNotice.get();
            String clientName = notice.getRental().getClient().getUser().getFirstname()+" "+notice.getRental().getClient().getUser().getLastname();
            String propertyName = notice.getRental().getUnitSection().getUnit().getProperty().getName()+" "+notice.getRental().getUnitSection().getUnit().getName()+" of" + notice.getRental().getUnitSection().getName();

            // make new response
            NoticeResponse response = new NoticeResponse();
            response.setClientName(clientName);
            response.setMessage(notice.getMessage());
            response.setDetails(notice.getDetails());
            response.setPropertyName(propertyName);
            response.setUid(notice.getUid());
            response.setRentalUid(notice.getRental().getUid());
            response.setRentalStartDate(notice.getRental().getStartDate());
            response.setRentalEndDate(notice.getRental().getEndDate());
            return new Response<>(true, ResponseCode.SUCCESS, "Success", response);
        }
        catch (Exception e){
            log.error("***** Error on fetching notice: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INTERNAL_SERVER_ERROR, "Error when fetching notice data", null);
        }
    }

    @Override
    public Response<Notice> delete(String uid){
        try {
            Optional<Notice> optionalNotice = getOptionalByUid(uid);
            if (optionalNotice.isEmpty()){
                return new Response<>(false, ResponseCode.DUPLICATE, "Notice could not be found or may have been deleted from the system", null);
            }
            noticeRepository.delete(optionalNotice.get());
            return new Response<>(true, ResponseCode.SUCCESS, "Notice deleted successfully", null);
        }
        catch (Exception e){
            log.error("***** Error on fetching unit: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INTERNAL_SERVER_ERROR, "Error when deleting unit data", null);
        }
    }

    @Override
    public List<NoticeResponse> notices(){
        try {
            List<NoticeResponse> notices = new ArrayList<>();
            for (Notice notice : noticeRepository.findAll()){
                NoticeResponse response = new NoticeResponse();
                String clientName = notice.getRental().getClient().getUser().getFirstname() + " "+ notice.getRental().getClient().getUser().getLastname();
                String propertyName = notice.getRental().getUnitSection().getUnit().getProperty().getName()+" "+notice.getRental().getUnitSection().getUnit().getName()+" of " + notice.getRental().getUnitSection().getName();
                response.setUid(notice.getUid());
                response.setClientName(clientName);
                response.setMessage(notice.getMessage());
                response.setDetails(notice.getDetails());
                response.setPropertyName(propertyName);
                response.setRentalUid(notice.getRental().getUid());
                response.setRentalStartDate(notice.getRental().getStartDate());
                response.setRentalEndDate(notice.getRental().getEndDate());
                notices.add(response);
            }
            return notices;
        }
        catch (Exception e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Notice> getNoticeByRental(String rentalUid){
        try {
            Optional<Rental> optionalRental = rentalService.getOptionalByUid(rentalUid);
            if (optionalRental.isEmpty()){
                log.error("Rental with uid: {} could not be found", rentalUid);
                return Collections.emptyList();
            }
            Rental rental = optionalRental.get();
            return noticeRepository.findAllByRental(rental);
        }
        catch (Exception e){
            return Collections.emptyList();
        }
    }

    @Override
    public List<Notice> getNoticeByProperty(String propertyUid){
        try {
            Optional<Property> optionalProperty = propertyService.getOptionalByUid(propertyUid);
            if (optionalProperty.isEmpty()){
                log.error("Property with uid: {} could not be found", propertyUid);
                return Collections.emptyList();
            }
            Property property = optionalProperty.get();
            return noticeRepository.findAllByPropertyId(property.getId());
        }
        catch (Exception e){
            return Collections.emptyList();
        }
    }

    void sendNoticeSMS(Rental rental,String content){
        SMSDto smsDto = new SMSDto();
            User user = rental.getClient().getUser();
            String clientName = user.getFirstname()+" "+user.getLastname();
            String ownerName = rental.getUnitSection().getUnit().getProperty().getName();
            String clientMobile = user.getMsisdn();
            Property property = rental.getUnitSection().getUnit().getProperty();
            String senderName = property.getSenderName() != null && !property.getSenderName().isEmpty() ? property.getSenderName() : "HOMES APP";
            String message = "Ndugu "+ clientName+" uongozi wa "+ ownerName+ " "+ content;
            smsDto.setMessage(message);
            smsDto.setSourceAddr(senderName);
            log.info("Sender Name: {}", senderName);
            Recipient recipient = new Recipient();
            recipient.setRecipient_id(1);
            recipient.setDest_addr(clientMobile);
            List<Recipient> recipients = Collections.singletonList(recipient);
            smsDto.setRecipients(recipients);
            smsService.sendSms(smsDto);
    }

}
