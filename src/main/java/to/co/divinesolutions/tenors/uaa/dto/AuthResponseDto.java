package to.co.divinesolutions.tenors.uaa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponseDto {
    private String message;
    private boolean result;
    private UserData data;
}
