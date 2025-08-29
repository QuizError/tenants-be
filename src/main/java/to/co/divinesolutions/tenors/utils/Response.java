package to.co.divinesolutions.tenors.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Response<T> {
    private boolean status;
    private Integer responseCode;
    private String message;
    private T data;
    private List<T> dataList;

    public Response(boolean status, Integer responseCode, String message, T data) {
        this.status = status;
        this.responseCode = responseCode;
        this.message = message;
        this.data = data;
    }

    public Response(boolean status, Integer responseCode, List<T> dataList, String message) {
        this.status = status;
        this.responseCode = responseCode;
        this.dataList = dataList;
        this.message = message;
    }
}
