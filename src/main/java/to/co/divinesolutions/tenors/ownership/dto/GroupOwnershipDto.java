package to.co.divinesolutions.tenors.ownership.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.PropertyOwnershipType;

@Getter
@Setter
@NoArgsConstructor
public class GroupOwnershipDto {
    private String uid;
    private String name;
    private PropertyOwnershipType ownershipType;
}
