package to.co.divinesolutions.tenors.property.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.PropertyType;

@Getter
@Setter
@NoArgsConstructor
public class UnitDto {
    private String uid;
    private String name;
    private String propertyUid;
    private String descriptions;
    private PropertyType propertyType;
}
