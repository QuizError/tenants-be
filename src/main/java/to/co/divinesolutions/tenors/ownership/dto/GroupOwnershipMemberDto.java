package to.co.divinesolutions.tenors.ownership.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.OwnershipMemberStatus;

@Getter
@Setter
@NoArgsConstructor
public class GroupOwnershipMemberDto {
    private String uid;
    private OwnershipMemberStatus memberStatus;
    private String userUid;
    private String groupUid;
}
