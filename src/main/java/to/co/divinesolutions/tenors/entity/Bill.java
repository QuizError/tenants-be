package to.co.divinesolutions.tenors.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.BillStatus;
import to.co.divinesolutions.tenors.enums.BillType;
import to.co.divinesolutions.tenors.enums.Currency;
import to.co.divinesolutions.tenors.utils.BaseEntity;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bills")
public class Bill extends BaseEntity {

    private BigDecimal totalAMount;
    private BigDecimal exchangeRate;
    private BigDecimal paidAmount;
    private BigDecimal amountDue;
    private BigDecimal totalEquivalentAmount;
    private BigDecimal agentFee;
    private BigDecimal commission;
    private Boolean commissionPaid = false;
    private String billReferenceNumber;
    private String thirdPartyReference;
    private String billDescription;
    private Boolean affected = false;

    @Enumerated(EnumType.STRING)
    private BillType billType;

    private Long billableId;
    private Long propertyId;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private BillStatus billStatus;
}
