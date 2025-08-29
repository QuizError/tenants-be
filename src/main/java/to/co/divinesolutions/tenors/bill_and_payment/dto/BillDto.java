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
public class BillDto {

    private String uid;
    private BigDecimal totalAMount;
    private BigDecimal exchangeRate;
    private BigDecimal totalEquivalentAmount;
    private BigDecimal agentFee;
    private BigDecimal commission;
    private String billDescription;
    private String billReferenceNumber;
    private Currency currency;
    private BillType billType;
    private Long billableId;
    private Long propertyId;
}
