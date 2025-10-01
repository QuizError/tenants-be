package to.co.divinesolutions.tenors.checklist.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.ChecklistType;

@Getter
@Setter
@NoArgsConstructor
public class ChecklistTemplateDto {
    private String uid;
    private String propertyUid;
    private ChecklistType checklistType;
    private String name;
    private String description;
}
