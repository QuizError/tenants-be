package to.co.divinesolutions.tenors.notice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.NoticeType;

@Getter
@Setter
@NoArgsConstructor
public class NoticeDto {
    private String uid;
    private String message;
    private String details;
    private String filePath;
    private NoticeType noticeType;
    private String rentalUid;
}
