package to.co.divinesolutions.tenors.ownership.service;

import to.co.divinesolutions.tenors.entity.GroupOwnership;
import to.co.divinesolutions.tenors.ownership.dto.GroupOwnershipDto;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;
import java.util.Optional;

public interface GroupOwnershipService {
    Response<GroupOwnership> save(GroupOwnershipDto dto);

    Optional<GroupOwnership> getOptionalByUid(String uid);

    Response<GroupOwnership> findByUid(String uid);

    Response<GroupOwnership> delete(String uid);

    List<GroupOwnership> groupOwnerships();
}
