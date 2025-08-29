package to.co.divinesolutions.tenors.bill_and_payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.Currency;
import to.co.divinesolutions.tenors.enums.PaymentChannel;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PaymentDto {
    private String uid;
    private BigDecimal paidAmount;
    private String billReferenceNumber;
    private String thirdPartyReference;
    private String fspName;
    private String fspCode;
    private Currency currency;
    private String paymentDate;
    private PaymentChannel paymentChannel;
}
