package to.co.divinesolutions.tenors.property.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.Currency;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AvailableSectionDto {
    private String uid;
    private String name;
    private String unitName;
    private BigDecimal price;
    private Currency currency;
    private Boolean availability;
}
