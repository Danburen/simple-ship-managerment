package org.waterwood.shipmanagerment.infrastructure.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.waterwood.shipmanagerment.api.ErrorResponse;
import org.waterwood.shipmanagerment.api.enums.ResponseCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRunTimeException(RuntimeException ex) {
        log.info("unHandled Runtime Exception: ", ex);
        ErrorResponse res = new ErrorResponse(
                ResponseCode.UNKNOWN_ERROR.getCode(),
                ex.getMessage()
        );
        return ResponseEntity.internalServerError().body(res);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex) {
        log.info("Auth Exception: {}", ex.getCode());
        ErrorResponse res = new ErrorResponse(
                ex.getCode().getCode(),
                ex.getCode().getMessage()
        );
        if(ex.getCode() == ResponseCode.FORBIDDEN){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
        }
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorResponse> handleBizException(BizException ex) {
        log.info("Business Exception: {}", ex.getCode());
        ErrorResponse res = new ErrorResponse(
                ex.getCode().getCode(),
                ex.getCode().getMessage()
        );
        return ResponseEntity.badRequest().body(res);
    }

    /**
     * Handle request body validation.
     * e.g. {@link RequestBody}
     * @param ex validation exception
     * @return the {@link ResponseEntity} of {@link ErrorResponse} body with {@link HttpStatus} 400.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.toList());
        ErrorResponse response = new ErrorResponse(
                ResponseCode.VALIDATION_ERROR.getCode(),
                ResponseCode.VALIDATION_ERROR.getMessage(),
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Validate the parameter constraint e.g.{@link NotBlank} in {@link RequestParam}
     * @param ex constraint violation exception
     * @return the {@link ResponseEntity} of {@link ErrorResponse} body with {@link HttpStatus} 400.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex){
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toList());

        ErrorResponse response = new ErrorResponse(
                ResponseCode.VALIDATION_ERROR.getCode(),
                ResponseCode.VALIDATION_ERROR.getMessage(),
                errors
        );
        log.info(String.valueOf(errors));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex){
        Throwable cause = ex.getCause();
        if(cause instanceof InvalidFormatException ife){
            // ensure enum type cause IFE. If not, ignore
            if(ife.getTargetType() != null && ife.getTargetType().isEnum()){
                String field = ife.getPath().get(0).getFieldName();
                String value = ife.getValue().toString();
                List<String> availableValues = Arrays.stream(ife.getTargetType().getEnumConstants())
                        .map(Object::toString)
                        .toList();
                String msg = ResponseCode.VALIDATION_ENUM_NOT_SUPPORT.getMessage()
                        .replace("{field}", field)
                        .replace("{value}", value)
                        .replace("{availableValues}", availableValues.toString());
                ErrorResponse res = new ErrorResponse(ResponseCode.VALIDATION_ERROR.getCode(), msg);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }
        }
        throw ex;
    }

}
