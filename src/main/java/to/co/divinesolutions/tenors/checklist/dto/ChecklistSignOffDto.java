package to.co.divinesolutions.tenors.checklist.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChecklistSignOffDto {
    private String uid;
    private String checklistInstanceUid;
    private String clientUid;
    private String managerUid;
    private Boolean clientSigned;
    private Boolean managerSigned ;
}
