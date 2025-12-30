package org.waterwood.shipmanagerment.infrastructure.exception;

import lombok.Getter;
import org.waterwood.shipmanagerment.api.enums.ResponseCode;

/**
 * An exception to handle authentication related exceptions.
 * usually will return 401 code to client
 */
@Getter
public class AuthException extends RuntimeException {
    private final ResponseCode code;
    private final Object[] params;
    public AuthException(ResponseCode code, Object[] params) {
        super(code.getCode());
        this.code = code;
        this.params = params;
    }

    public AuthException(ResponseCode code) {
        super(code.getCode());
        this.code = code;
        this.params = null;
    }
}
