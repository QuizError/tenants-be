package to.co.divinesolutions.tenors.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.BillStatus;
import to.co.divinesolutions.tenors.enums.Currency;
import to.co.divinesolutions.tenors.enums.PaymentStatus;
import to.co.divinesolutions.tenors.enums.RentalStatus;
import to.co.divinesolutions.tenors.utils.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rentals")
public class Rental extends BaseEntity {

    @ManyToOne
    private Client client;

    @ManyToOne
    private UnitSection unitSection;

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal rentalAmount;
    private BigDecimal secureDeposit;
    private BigDecimal serviceCharge;
    private Long propertyId;
    private Long unitId;

    @Transient
    private String serviceChargeDescription;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private BillStatus billStatus;

    @Enumerated(EnumType.STRING)
    private RentalStatus rentalStatus;

    private Boolean endOfContractNotification = false;
    private Boolean checklistCreated = false;
    private Boolean renewalConfirmed = false;
}
