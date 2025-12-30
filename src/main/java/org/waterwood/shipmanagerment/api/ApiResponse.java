package org.waterwood.shipmanagerment.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(){
        return new ApiResponse<>("200", "操作成功", null);
    }

    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>("200", "操作成功", data);
    }
}
