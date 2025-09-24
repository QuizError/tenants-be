package to.co.divinesolutions.tenors.bill_and_payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.BillType;
import to.co.divinesolutions.tenors.enums.Currency;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class BillItemDto {
    private String uid;
    private BigDecimal amount;
    private String description;
    private BillType billType;
    private Currency currency;
}
