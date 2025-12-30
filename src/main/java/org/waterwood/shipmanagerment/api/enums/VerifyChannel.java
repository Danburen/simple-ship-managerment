package org.waterwood.shipmanagerment.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * An enum to represent the different verification scenes
 */
public enum VerifyChannel {
    PWD,;

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }

    @JsonCreator
    public VerifyChannel fromValue(String value) {
        return valueOf(value.toUpperCase());
    }
}
