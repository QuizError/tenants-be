package to.co.divinesolutions.tenors.sms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SMSDto {
    private String sourceAddr;
    private String message;
    private List<Recipient> recipients;
}
