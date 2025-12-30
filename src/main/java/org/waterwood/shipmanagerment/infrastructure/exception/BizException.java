package org.waterwood.shipmanagerment.infrastructure.exception;

import lombok.Getter;
import org.waterwood.shipmanagerment.api.enums.ResponseCode;

@Getter
public class BizException extends RuntimeException {
    private final ResponseCode code;
    private final Object[] params;
    public BizException(ResponseCode code, Object[] params) {
        super(code.getMessage());
        this.code = code;
        this.params = params;
    }

    public BizException(ResponseCode code) {
        super(code.getMessage());
        this.code = code;
        this.params = null;
    }
}
