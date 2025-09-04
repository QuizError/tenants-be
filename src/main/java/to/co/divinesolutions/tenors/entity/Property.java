package to.co.divinesolutions.tenors.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.PropertyFunctionStatus;
import to.co.divinesolutions.tenors.enums.PropertyOwnershipType;
import to.co.divinesolutions.tenors.utils.BaseEntity;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "properties")
public class Property extends BaseEntity {

    private String name;
    private String senderName;
    private Long ownerId;
    private LocalDate startFunction;
    private LocalDate endFunction;
    private String location;

    @Enumerated(EnumType.STRING)
    private PropertyFunctionStatus functionStatus;

    @Enumerated(EnumType.STRING)
    private PropertyOwnershipType ownershipType;
}
