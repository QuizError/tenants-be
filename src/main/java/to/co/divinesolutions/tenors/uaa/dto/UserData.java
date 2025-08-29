package to.co.divinesolutions.tenors.uaa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserData {
    private String fullName;
    private Long employeeId;
    private String uid;
    private String mobileNo;
    private String email;
    private String role;
    private String password;
}
