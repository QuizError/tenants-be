package to.co.divinesolutions.tenors.property.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.Currency;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UnitSectionDto {
    private String uid;
    private String name;
    private String unitUid;
    private String unitName;
    private String waterMeter;
    private String gasMeter;
    private String squareMeters;
    private String electricityMeter;
    private BigDecimal price;
    private Currency currency;
    private Boolean available;
    private List<RoomDto> roomDtos;
}
