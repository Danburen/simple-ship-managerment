package org.waterwood.shipmanagerment.service;

import org.waterwood.shipmanagerment.api.dto.request.PwdLoginReq;
import org.waterwood.shipmanagerment.api.dto.request.RegisterReq;
import org.waterwood.shipmanagerment.api.dto.request.RegisterV2Req;
import org.waterwood.shipmanagerment.api.dto.response.LoginRes;
import org.waterwood.shipmanagerment.api.dto.response.PwdLoginV2Req;

public interface AuthService {
    /**
     *  Register user
     * @param dto tryRegister request
     * @param key captcha key
     */
    void tryRegister(RegisterReq dto, String key);

    void tryRegister(RegisterV2Req dto, String remoteIp );
    /**
     * Login a user by password
     * @param dto dto
     * @param key key
     */
    LoginRes tryLoginByPwd(PwdLoginReq dto, String key);


    LoginRes tryLoginByPwd(PwdLoginV2Req dto, String remoteIp);
    /**
     * Logout a user
     * @param userId user id
     */
    void logout(Long userId);
}
