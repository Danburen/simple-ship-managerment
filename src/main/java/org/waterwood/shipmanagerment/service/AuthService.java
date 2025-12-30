package org.waterwood.shipmanagerment.service;

import org.waterwood.shipmanagerment.api.dto.request.PwdLoginReq;
import org.waterwood.shipmanagerment.api.dto.request.RegisterReq;
import org.waterwood.shipmanagerment.api.dto.response.LoginRes;

public interface AuthService {
    /**
     *  Register user
     * @param dto register request
     * @param key captcha key
     */
    void register(RegisterReq dto, String key);

    /**
     * Login a user by password
     * @param dto dto
     * @param key key
     */
    LoginRes loginByPwd(PwdLoginReq dto, String key);

    /**
     * Logout a user
     * @param userId user id
     */
    void logout(Long userId);
}
