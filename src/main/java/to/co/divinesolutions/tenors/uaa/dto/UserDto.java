package to.co.divinesolutions.tenors.uaa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.Gender;
import to.co.divinesolutions.tenors.enums.UserType;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String uid;
    private String firstname;
    private String middleName;
    private String lastname;
    private String email;
    private String mobileNo;
    private String password;
    private UserType userType;
    private Gender gender;
    private String idNumber;
}
