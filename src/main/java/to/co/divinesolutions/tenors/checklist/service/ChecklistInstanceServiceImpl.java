package to.co.divinesolutions.tenors.checklist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import to.co.divinesolutions.tenors.checklist.repository.ChecklistInstanceRepository;
import to.co.divinesolutions.tenors.entity.ChecklistInstance;
import to.co.divinesolutions.tenors.entity.ChecklistTemplate;
import to.co.divinesolutions.tenors.utils.Response;
import to.co.divinesolutions.tenors.utils.ResponseCode;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChecklistInstanceServiceImpl implements ChecklistInstanceService{

    private final ChecklistInstanceRepository checklistInstanceRepository;
    private final ChecklistTemplateService checklistTemplateService;

    @Override
    public List<ChecklistInstance> getChecklistByTemplate(String templateUid){
        try{
            Optional<ChecklistTemplate> optionalChecklistInstance = checklistTemplateService.getOptionalByUid(templateUid);
            if (optionalChecklistInstance.isEmpty()){
                return Collections.emptyList();
            }
            ChecklistTemplate checklistTemplate = optionalChecklistInstance.get();
            return checklistInstanceRepository.findAllByTemplate(checklistTemplate);
        }
        catch (Exception e){
            log.info("Error fetching Checklist Instance List: {}",e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Response<ChecklistInstance> findByUid(String uid){
        try {
            Optional<ChecklistInstance> optionalChecklistInstance = getOptionalByUid(uid);
            return optionalChecklistInstance.map(checklistInstance -> new Response<>(true, ResponseCode.SUCCESS, "Success", checklistInstance)).orElseGet(() -> new Response<>(false, ResponseCode.NO_DATA_FOUND, "Checklist Instance could not be found or may have been deleted from the system", null));
        }
        catch (Exception e){
            return new Response<>(true,ResponseCode.SUCCESS,"Failure to fetch Checklist Instance",null);
        }
    }

    @Override
    public Optional<ChecklistInstance> getOptionalByUid(String uid){
        return uid != null && !uid.isEmpty() ? checklistInstanceRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Response<ChecklistInstance> delete(String uid){
        try {
            Optional<ChecklistInstance> optionalChecklistInstance = getOptionalByUid(uid);
            if (optionalChecklistInstance.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND,"Checklist Instance could not be found or may have been deleted from the system",null);
            }
            checklistInstanceRepository.delete(optionalChecklistInstance.get());
            return new Response<>(true,ResponseCode.SUCCESS,"Checklist Instance deleted Successfully",null);
        }
        catch (Exception e){
            return new Response<>(true,ResponseCode.SUCCESS,"Failure to delete Checklist Instance",null);
        }
    }

}
