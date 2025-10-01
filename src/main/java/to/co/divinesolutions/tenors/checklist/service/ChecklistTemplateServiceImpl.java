package to.co.divinesolutions.tenors.checklist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import to.co.divinesolutions.tenors.checklist.dto.ChecklistTemplateDto;
import to.co.divinesolutions.tenors.checklist.repository.ChecklistTemplateRepository;
import to.co.divinesolutions.tenors.entity.*;
import to.co.divinesolutions.tenors.property.service.PropertyService;
import to.co.divinesolutions.tenors.utils.Response;
import to.co.divinesolutions.tenors.utils.ResponseCode;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChecklistTemplateServiceImpl implements ChecklistTemplateService{

    private final ChecklistTemplateRepository checklistTemplateRepository;
    private final PropertyService propertyService;
    @Override
    public Response<ChecklistTemplate> save(ChecklistTemplateDto dto){
        try {
            Optional<Property> optionalProperty = propertyService.getOptionalByUid(dto.getPropertyUid());
            if (optionalProperty.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND,"Property could not be found or may have been deleted from the system", null);
            }
            Property property = optionalProperty.get();

            Optional<ChecklistTemplate> optionalChecklistTemplate = getOptionalByUid(dto.getUid());
            if (dto.getUid() != null && !dto.getUid().isEmpty() && optionalChecklistTemplate.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND,"Checklist template could not be found or may have been deleted from the system", null);
            }

            ChecklistTemplate checklistTemplate = optionalChecklistTemplate.orElse(new ChecklistTemplate());
            checklistTemplate.setName(dto.getName());
            checklistTemplate.setProperty(property);
            checklistTemplate.setDescription(dto.getDescription());
            checklistTemplate.setChecklistType(dto.getChecklistType());
            ChecklistTemplate saved = checklistTemplateRepository.save(checklistTemplate);
            return new Response<>(true,ResponseCode.SUCCESS,"Checklist Template saved successfully", saved);
        }
        catch (Exception e){
            return new Response<>(true,ResponseCode.INTERNAL_SERVER_ERROR,"Error deleting checklist Template", null);
        }
    }

    @Override
    public Optional<ChecklistTemplate> getOptionalByUid(String uid){
        return uid != null && !uid.isEmpty() ? checklistTemplateRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Response<ChecklistTemplate> findByUid(String uid){
        try {
            Optional<ChecklistTemplate> optionalChecklistTemplate = getOptionalByUid(uid);
            return optionalChecklistTemplate.map(checklistTemplate -> new Response<>(true, ResponseCode.SUCCESS, "Success", checklistTemplate)).orElseGet(() -> new Response<>(false, ResponseCode.NO_DATA_FOUND, "Checklist Template could not be found or may have been deleted from the system", null));
        }
        catch (Exception e){
            return new Response<>(false,ResponseCode.INTERNAL_SERVER_ERROR,"Error occurred when fetching checklist template", null);

        }
    }

    @Override
    @Transactional
    public Response<ChecklistTemplate> delete(String uid){
        try {
            Optional<ChecklistTemplate> optionalChecklistTemplate = getOptionalByUid(uid);

            if (optionalChecklistTemplate.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND,"Checklist template could not be found or may have been deleted from the system", null);
            }
            ChecklistTemplate checklistTemplate = optionalChecklistTemplate.get();

            checklistTemplateRepository.delete(checklistTemplate);
            return new Response<>(true,ResponseCode.SUCCESS,"Checklist template deleted successfully", null);
        }
        catch (Exception e){
            log.error("Error {} when deleting client",e.getMessage());
            e.printStackTrace();
            return new Response<>(false,ResponseCode.INTERNAL_SERVER_ERROR,"Error occurred when deleting Checklist template", null);
        }
    }

    @Override
    public List<ChecklistTemplate> checklistTemplates(){
        return checklistTemplateRepository.findAll();
    }

    @Override
    public List<ChecklistTemplate> listAllMyClients(String propertyUid){
        Optional<Property> optionalProperty = propertyService.getOptionalByUid(propertyUid);
        if (optionalProperty.isEmpty()){
            return Collections.emptyList();
        }
        Property property = optionalProperty.get();
        return checklistTemplateRepository.findAllByProperty(property);
    }
}
