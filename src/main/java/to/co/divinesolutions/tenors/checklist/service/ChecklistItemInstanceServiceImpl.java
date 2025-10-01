package to.co.divinesolutions.tenors.checklist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import to.co.divinesolutions.tenors.checklist.dto.ChecklistItemInstanceDto;
import to.co.divinesolutions.tenors.checklist.repository.ChecklistItemInstanceRepository;
import to.co.divinesolutions.tenors.entity.*;
import to.co.divinesolutions.tenors.property.service.UnitSectionDefinitionService;
import to.co.divinesolutions.tenors.utils.Response;
import to.co.divinesolutions.tenors.utils.ResponseCode;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChecklistItemInstanceServiceImpl implements ChecklistItemInstanceService{
    private final ChecklistItemInstanceRepository checklistItemInstanceRepository;
    private final UnitSectionDefinitionService unitSectionDefinitionService;
    private final ChecklistInstanceService checklistInstanceService;
    @Override
    public Response<ChecklistItemInstance> save(ChecklistItemInstanceDto dto){
        try {
            Optional<ChecklistInstance> optionalChecklistInstance = checklistInstanceService.getOptionalByUid(dto.getChecklistInstanceUid());
            if (optionalChecklistInstance.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND,"Checklist Instance could not be found or may have been deleted from the system", null);
            }
            ChecklistInstance checklistInstance = optionalChecklistInstance.get();

            Optional<UnitSectionDefinition> optionalUnitSectionDefinition = unitSectionDefinitionService.getOptionalByUid(dto.getUnitSectionDefinitionUid());
            if (optionalUnitSectionDefinition.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND,"Room(s) could not be found or may have been deleted from the system", null);
            }
            UnitSectionDefinition unitSectionDefinition = optionalUnitSectionDefinition.get();

            Optional<ChecklistItemInstance> optionalChecklistItemInstance = getOptionalByUid(dto.getUid());
            if (dto.getUid() != null && !dto.getUid().isEmpty() && optionalChecklistItemInstance.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND,"Checklist Item Instance could not be found or may have been deleted from the system", null);
            }

            ChecklistItemInstance checklistItemInstance = optionalChecklistItemInstance.orElse(new ChecklistItemInstance());
            checklistItemInstance.setChecklistInstance(checklistInstance);
            checklistItemInstance.setUnitSectionDefinition(unitSectionDefinition);
            checklistItemInstance.setItemName(dto.getItemName());
            checklistItemInstance.setCondition(dto.getCondition());
            checklistItemInstance.setCount(dto.getCount());
            checklistItemInstance.setNotes(dto.getNotes());
            ChecklistItemInstance saved = checklistItemInstanceRepository.save(checklistItemInstance);
            return new Response<>(true,ResponseCode.SUCCESS,"Checklist Item for Instance saved successfully", saved);
        }
        catch (Exception e){
            return new Response<>(true,ResponseCode.INTERNAL_SERVER_ERROR,"Error deleting checklist item instance", null);
        }
    }

    @Override
    public Optional<ChecklistItemInstance> getOptionalByUid(String uid){
        return uid != null && !uid.isEmpty() ? checklistItemInstanceRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Response<ChecklistItemInstance> findByUid(String uid){
        try {
            Optional<ChecklistItemInstance> optionalChecklistItemInstance = getOptionalByUid(uid);
            return optionalChecklistItemInstance.map(checklistItemInstance -> new Response<>(true, ResponseCode.SUCCESS, "Success", checklistItemInstance)).orElseGet(() -> new Response<>(false, ResponseCode.NO_DATA_FOUND, "Checklist Item of Instance could not be found or may have been deleted from the system", null));
        }
        catch (Exception e){
            return new Response<>(false,ResponseCode.INTERNAL_SERVER_ERROR,"Error occurred when fetching checklist item instance", null);

        }
    }

    @Override
    @Transactional
    public Response<ChecklistItemInstance> delete(String uid){
        try {
            Optional<ChecklistItemInstance> optionalChecklistItemInstance = getOptionalByUid(uid);

            if (optionalChecklistItemInstance.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND,"Checklist Item Instance could not be found or may have been deleted from the system", null);
            }
            ChecklistItemInstance checklistTemplate = optionalChecklistItemInstance.get();

            checklistItemInstanceRepository.delete(checklistTemplate);
            return new Response<>(true,ResponseCode.SUCCESS,"Checklist item Instance deleted successfully", null);
        }
        catch (Exception e){
            log.error("Error {} when deleting client",e.getMessage());
            e.printStackTrace();
            return new Response<>(false,ResponseCode.INTERNAL_SERVER_ERROR,"Error occurred when deleting Checklist template", null);
        }
    }

    @Override
    public List<ChecklistItemInstance> checklistItemInstanceList(){
        return checklistItemInstanceRepository.findAll();
    }

    @Override
    public List<ChecklistItemInstance> checklistItemInstanceList(String instanceUid){
        Optional<ChecklistInstance> optionalChecklistInstance = checklistInstanceService.getOptionalByUid(instanceUid);
        if (optionalChecklistInstance.isEmpty()){
            return Collections.emptyList();
        }
        ChecklistInstance checklistInstance = optionalChecklistInstance.get();
        return checklistItemInstanceRepository.findAllByChecklistInstance(checklistInstance);
    }
}
