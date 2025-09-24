package to.co.divinesolutions.tenors.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.ItemCondition;
import to.co.divinesolutions.tenors.utils.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "checklist_item_instances")
public class ChecklistItemInstance extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "checklist_instance_id")
    private ChecklistInstance checklistInstance;

    @ManyToOne
    @JoinColumn(name = "unit_section_definition_id")
    private UnitSectionDefinition unitSectionDefinition;

    private String itemName;
    private ItemCondition condition;
    private Integer count;
    private String notes;
}
