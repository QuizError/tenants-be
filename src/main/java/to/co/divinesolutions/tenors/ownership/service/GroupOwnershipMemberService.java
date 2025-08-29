package to.co.divinesolutions.tenors.ownership.service;

import to.co.divinesolutions.tenors.entity.GroupOwnership;
import to.co.divinesolutions.tenors.entity.GroupOwnershipMember;
import to.co.divinesolutions.tenors.ownership.dto.GroupOwnershipMemberDto;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;

public interface GroupOwnershipMemberService {
    Response<GroupOwnershipMember> save(GroupOwnershipMemberDto dto);

    Response<GroupOwnershipMember> findByUid(String uid);

    Response<GroupOwnershipMember> delete(String uid);

    List<GroupOwnershipMember> groupOwnershipMembers();

    List<GroupOwnershipMember> listGroupMembers(String groupUid);

    List<GroupOwnership> listMyGroups(String userUid);
}
