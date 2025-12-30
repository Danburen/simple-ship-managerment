package org.waterwood.shipmanagerment.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * An enum to represent the different verification scenes
 */
public enum VerifyScene {
    REGISTER,;

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }

    @JsonCreator
    public VerifyScene fromValue(String value) {
        return valueOf(value.toUpperCase());
    }
}
