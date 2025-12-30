package org.waterwood.shipmanagerment.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL) // only no-null value will be serialized
    private List<String> errors;
    private Instant timestamp;

    public ErrorResponse(String code, String message){
        this.code = code;
        this.message = message;
        timestamp = Instant.now();
    }

    public ErrorResponse(String code, String message, List<String> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
        timestamp = Instant.now();
    }
}
