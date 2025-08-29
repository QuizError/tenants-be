package to.co.divinesolutions.tenors.ownership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.GroupOwnership;
import to.co.divinesolutions.tenors.entity.GroupOwnershipMember;
import to.co.divinesolutions.tenors.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupOwnershipMemberRepository extends JpaRepository<GroupOwnershipMember,Long> {
    Optional<GroupOwnershipMember> findFirstByUid(String uid);
    List<GroupOwnershipMember> findAllByGroup(GroupOwnership groupOwnership);
    List<GroupOwnershipMember> findAllByUser(User user);
}
