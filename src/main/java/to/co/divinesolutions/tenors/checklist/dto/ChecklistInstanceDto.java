package to.co.divinesolutions.tenors.checklist.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChecklistInstanceDto {

    private String uid;
    private String rentalUid;
    private String clientUid;
    private String unitSectionUid;
    private String checklistTemplateUid;
    private String title;
}
