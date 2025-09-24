package to.co.divinesolutions.tenors.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.ChecklistType;
import to.co.divinesolutions.tenors.utils.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "checklist_templates")
public class ChecklistTemplate extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @Enumerated(EnumType.STRING)
    private ChecklistType checklistType;

    private String name;
    private String description;
}
