package org.waterwood.shipmanagerment.service.storage;

public enum CloudResType {
    AVATAR,
    COVER,;

    public String getLocalCase() {
        return name().toLowerCase();
    }
}
