package org.waterwood.shipmanagerment.service;

import lombok.Data;

import java.util.List;

@Data
public class TurnstileResponse {
    boolean success;
    List<String> errorCodes;
}
