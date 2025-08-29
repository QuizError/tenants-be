package to.co.divinesolutions.tenors.rentals.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.BillStatus;
import to.co.divinesolutions.tenors.enums.Currency;
import to.co.divinesolutions.tenors.enums.PaymentStatus;
import to.co.divinesolutions.tenors.enums.RentalStatus;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class RentalDto {
    private String uid;
    private String clientUid;
    private String clientName;
    private String unitSectionUid;
    private String unitSectionName;
    private String unitName;
    private String startDate;
    private String endDate;
    private BigDecimal price;
    private Currency currency;
    private BillStatus billStatus;
    private RentalStatus rentalStatus;
}
