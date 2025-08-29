package to.co.divinesolutions.tenors.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.Currency;
import to.co.divinesolutions.tenors.enums.PaymentChannel;
import to.co.divinesolutions.tenors.enums.PaymentStatus;
import to.co.divinesolutions.tenors.utils.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    private BigDecimal paidAmount;
    private String billReferenceNumber;
    private String thirdPartyReference;
    private String fspName;
    private String fspCode;
    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentChannel paymentChannel;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToOne
    private Bill bill;
}
