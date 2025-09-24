package to.co.divinesolutions.tenors.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.BillType;
import to.co.divinesolutions.tenors.enums.Currency;
import to.co.divinesolutions.tenors.utils.BaseEntity;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bill_items")
public class BillItem extends BaseEntity {

    private BigDecimal amount;
    private String description;
    private BillType billType;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "bill_id")
    private Bill bill;
}
