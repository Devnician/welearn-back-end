package uni.ruse.welearn.welearn.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uni.ruse.welearn.welearn.model.auth.ApiResponse;

/**
 * @author Ivelin Dimitrov
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            if (errors.get(fieldName) != null) {
                List<String> stringList = errors.get(fieldName);
                stringList.add(error.getDefaultMessage());
                errors.put(fieldName, stringList);
            } else {
                errors.put(fieldName, new ArrayList<>(Arrays.asList(error.getDefaultMessage())));
            }
        });
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WeLearnException.class)
    public ResponseEntity<ApiResponse<String>> handleWeLearnException(
            WeLearnException ex
    ) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "General exception", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
