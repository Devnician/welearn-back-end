package uni.ruse.welearn.welearn.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uni.ruse.welearn.welearn.model.auth.ApiResponse;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ivelin Dimitrov
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler({TransactionSystemException.class})
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolation(
            Exception exception) {
        Throwable cause = ((TransactionSystemException) exception).getRootCause();
        if (cause instanceof ConstraintViolationException) {
            Map<String, List<String>> errors = new HashMap<>();
            ((ConstraintViolationException) cause).getConstraintViolations().forEach(error -> {
                String fieldName = error.getPropertyPath().toString();
                if (errors.get(fieldName) != null) {
                    List<String> stringList = errors.get(fieldName);
                    stringList.add(error.getMessage());
                    errors.put(fieldName, stringList);
                } else {
                    errors.put(fieldName, new ArrayList<>(Collections.singletonList(error.getMessage())));
                }
            });
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors), HttpStatus.BAD_REQUEST);
        } else {
            exception.printStackTrace();
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", ""), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            if (errors.get(fieldName) != null) {
                List<String> stringList = errors.get(fieldName);
                stringList.add(error.getDefaultMessage());
                errors.put(fieldName, stringList);
            } else {
                errors.put(fieldName, new ArrayList<>(Collections.singletonList(error.getDefaultMessage())));
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
