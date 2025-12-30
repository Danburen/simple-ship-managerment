package org.waterwood.shipmanagerment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.waterwood.shipmanagerment.api.TokenResult;
import org.waterwood.shipmanagerment.api.dto.request.PwdLoginReq;
import org.waterwood.shipmanagerment.api.dto.request.RegisterV2Req;
import org.waterwood.shipmanagerment.api.dto.response.LoginRes;
import org.waterwood.shipmanagerment.api.dto.response.PwdLoginV2Req;
import org.waterwood.shipmanagerment.api.enums.ResponseCode;
import org.waterwood.shipmanagerment.api.dto.request.RegisterReq;
import org.waterwood.shipmanagerment.entity.User;
import org.waterwood.shipmanagerment.infrastructure.component.RedisHelper;
import org.waterwood.shipmanagerment.infrastructure.exception.AuthException;
import org.waterwood.shipmanagerment.infrastructure.exception.BizException;
import org.waterwood.shipmanagerment.infrastructure.repository.UserRepository;
import org.waterwood.shipmanagerment.service.*;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CaptchaService captchaService;
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final TokenService tokenService;
    private final RedisHelper redisHelper;
    private final TurnstileService turnstileService;

    @Override
    public void tryRegister(RegisterReq dto, String key) {
        captchaService.VerifyCaptcha(dto.getVerifyCode(), key);
        register(dto.getPassword(), dto.getConfirmPwd(), dto.getUsername(), dto.getNickname());
    }

    @Override
    public void tryRegister(RegisterV2Req dto, String remoteIp) {
        TurnstileResponse res = turnstileService.validateToken(dto.getCfTurnstileToken(), remoteIp);
        if(res.isSuccess()){
            register(dto.getPassword(), dto.getConfirmPwd(), dto.getUsername(), dto.getNickname());
        }else{
            throw new AuthException(ResponseCode.VERIFICATION_ERROR);
        }
    }

    private void register(String password, String confirmPwd, String username, String nickname) {
        if( ! password.equals(confirmPwd)){
            throw new BizException(ResponseCode.PASSWORD_NOT_MATCH);
        }
        userRepository.findByUsername((username))
                .ifPresent(_ -> {
                    throw new BizException(ResponseCode.USER_ALREADY_EXISTS);
                });
        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(password));
        u.setNickname(nickname);
        userRepository.save(u);
    }

    @Override
    public LoginRes tryLoginByPwd(PwdLoginReq dto, String key) {
        captchaService.VerifyCaptcha(dto.getVerifyCode(), key);
        return login(dto.getUsername(), dto.getPassword());
    }

    @Override
    public LoginRes tryLoginByPwd(PwdLoginV2Req dto, String remoteIp) {
        TurnstileResponse res = turnstileService.validateToken(dto.getCfTurnstileToken(), remoteIp);
        if(res.isSuccess()){
            return login(dto.getUsername(), dto.getPassword());
        }else{
            throw new AuthException(ResponseCode.VERIFICATION_ERROR);
        }
    }

    @NotNull
    private LoginRes login(String username, String password) {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException(ResponseCode.USERNAME_OR_PASSWORD_INCORRECT));

        if(! encoder.matches(password, u.getPassword())){
            throw new AuthException(ResponseCode.USERNAME_OR_PASSWORD_INCORRECT);
        }
        TokenResult res = tokenService.generateToken(u.getId());
        return new LoginRes(
                res.token(),
                Instant.now().plusSeconds(res.expire())
        );
    }

    @Override
    public void logout(Long userId) {
        tokenService.revokeToken(userId);
    }
}
