package to.co.divinesolutions.tenors.checklist.service;

import org.springframework.transaction.annotation.Transactional;
import to.co.divinesolutions.tenors.checklist.dto.ChecklistTemplateDto;
import to.co.divinesolutions.tenors.entity.ChecklistTemplate;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;
import java.util.Optional;

public interface ChecklistTemplateService {
    Response<ChecklistTemplate> save(ChecklistTemplateDto dto);

    Optional<ChecklistTemplate> getOptionalByUid(String uid);

    Response<ChecklistTemplate> findByUid(String uid);

    @Transactional
    Response<ChecklistTemplate> delete(String uid);

    List<ChecklistTemplate> checklistTemplates();

    List<ChecklistTemplate> listAllMyClients(String propertyUid);
}
