package to.co.divinesolutions.tenors.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.utils.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "checklist_instances")
public class ChecklistInstance extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;

    private Long propertyId;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private ChecklistTemplate template;

    private String title;
}
