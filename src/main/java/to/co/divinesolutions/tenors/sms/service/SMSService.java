package to.co.divinesolutions.tenors.sms.service;

import to.co.divinesolutions.tenors.sms.dto.SMSDto;
import to.co.divinesolutions.tenors.sms.dto.SentSmsBody;

public interface SMSService {
    String sendSms(SMSDto smsDto);

    void saveSentSms(SentSmsBody body);
}
