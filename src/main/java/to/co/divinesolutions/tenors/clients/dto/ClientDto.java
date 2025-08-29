package to.co.divinesolutions.tenors.clients.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.Gender;

@Getter
@Setter
@NoArgsConstructor
public class ClientDto {
    private String uid;
    private String ownerUid;
    private String userUid;
    private String firstname;
    private String lastname;
    private String middleName;
    private String msisdn;
    private String email;
    private String password;
    private String idNumber;
    private Gender gender;
    private String occupation;
    private String dob;
}
