package uni.ruse.welearn.welearn.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that hold's result data from processing after REST call. This result
 * can contains status, message and List with objects as result.
 *
 * @param <T> App entity
 * @author petar ivanov
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private Object result;
}
