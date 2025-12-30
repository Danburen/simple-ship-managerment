package org.waterwood.shipmanagerment.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.waterwood.shipmanagerment.api.enums.HttpMethod;

import java.io.Serializable;

/**
 * File upload response dto for file upload sign to cloud temporary storage.
 */
@Data
@AllArgsConstructor
public class PostPolicyDto implements Serializable {
    private String key;
    private String url;
    private HttpMethod method;
}
