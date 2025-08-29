package to.co.divinesolutions.tenors.bill_and_payment.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.BillStatus;
import to.co.divinesolutions.tenors.enums.BillType;
import to.co.divinesolutions.tenors.enums.Currency;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class BillDetails {
    private String uid;
    private String createdAt;
    private String customerUid;
    private BigDecimal totalAMount;
    private BigDecimal paidAmount;
    private BigDecimal amountDue;
    private BigDecimal agentFee;
    private BigDecimal commission;
    private String billReferenceNumber;
    private String thirdPartyReference;
    private String billDescription;
    private BillType billType;
    private String customerName;
    private Currency currency;
    private BillStatus billStatus;
}
