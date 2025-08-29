package to.co.divinesolutions.tenors.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.OwnershipMemberStatus;
import to.co.divinesolutions.tenors.utils.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_ownership_members")
public class GroupOwnershipMember extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private OwnershipMemberStatus memberStatus;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupOwnership group;

}
