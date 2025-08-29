package to.co.divinesolutions.tenors.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseCode {

    public static final Integer SUCCESS = 200;
    public static final Integer NO_DATA_FOUND =404;
    public static final Integer DUPLICATE = 402;
    public static final Integer DATABASE_ERROR = 501;
    public static final Integer INTERNAL_SERVER_ERROR = 500;
    public static final Integer INVALID_INPUT = 405;
    public static final Integer UNAUTHORIZED = 403;
}
