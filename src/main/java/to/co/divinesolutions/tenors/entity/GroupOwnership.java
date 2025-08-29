package to.co.divinesolutions.tenors.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.PropertyOwnershipType;
import to.co.divinesolutions.tenors.utils.BaseEntity;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_ownerships")
public class GroupOwnership extends BaseEntity {

    private String name;

    @Enumerated(EnumType.STRING)
    private PropertyOwnershipType ownershipType;

    @OneToMany(mappedBy = "group")
    @JsonIgnore
    private List<GroupOwnershipMember> members;
}
