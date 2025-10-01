package to.co.divinesolutions.tenors.checklist.service;

import org.springframework.transaction.annotation.Transactional;
import to.co.divinesolutions.tenors.checklist.dto.ChecklistItemInstanceDto;
import to.co.divinesolutions.tenors.entity.ChecklistItemInstance;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;
import java.util.Optional;

public interface ChecklistItemInstanceService {
    Response<ChecklistItemInstance> save(ChecklistItemInstanceDto dto);

    Optional<ChecklistItemInstance> getOptionalByUid(String uid);

    Response<ChecklistItemInstance> findByUid(String uid);

    @Transactional
    Response<ChecklistItemInstance> delete(String uid);

    List<ChecklistItemInstance> checklistItemInstanceList();

    List<ChecklistItemInstance> checklistItemInstanceList(String instanceUid);
}
