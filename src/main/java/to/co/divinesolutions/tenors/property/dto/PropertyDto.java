package to.co.divinesolutions.tenors.property.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.Currency;
import to.co.divinesolutions.tenors.enums.PropertyFunctionStatus;
import to.co.divinesolutions.tenors.enums.PropertyOwnershipType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PropertyDto {
    private String uid;
    private String name;
    private Boolean hasServiceCharge = false;
    private BigDecimal serviceChargeAmount;
    private Currency serviceChargeCurrency;
    private String serviceChargeDescription;
    private String senderName;
    private String ownerUid;
    private String location;
    private LocalDate endFunction;
    private LocalDate startFunction;
    private PropertyOwnershipType ownershipType;
    private PropertyFunctionStatus functionStatus;
}
