package to.co.divinesolutions.tenors.notice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class NoticeResponse {
    private String uid;
    private String details;
    private String message;
    private String clientName;
    private String rentalUid;
    private String propertyName;
    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;
}
