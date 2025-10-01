package to.co.divinesolutions.tenors.checklist.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.ItemCondition;

@Getter
@Setter
@NoArgsConstructor
public class ChecklistItemInstanceDto {

    private String uid;
    private String checklistInstanceUid;
    private String unitSectionDefinitionUid;
    private String itemName;
    private ItemCondition condition;
    private Integer count;
    private String notes;
}
