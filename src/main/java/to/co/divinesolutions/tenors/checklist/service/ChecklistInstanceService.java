package to.co.divinesolutions.tenors.checklist.service;

import to.co.divinesolutions.tenors.entity.ChecklistInstance;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;
import java.util.Optional;

public interface ChecklistInstanceService {
    List<ChecklistInstance> getChecklistByTemplate(String templateUid);

    Response<ChecklistInstance> findByUid(String uid);

    Optional<ChecklistInstance> getOptionalByUid(String uid);

    Response<ChecklistInstance> delete(String uid);
}
