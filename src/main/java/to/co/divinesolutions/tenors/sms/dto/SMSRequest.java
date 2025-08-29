package to.co.divinesolutions.tenors.sms.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SMSRequest {
    private String source_addr;
    private String schedule_time;
    private String encoding;
    private String message;
    private List<Recipient> recipients;
}