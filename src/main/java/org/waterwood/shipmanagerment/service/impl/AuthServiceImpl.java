package org.waterwood.shipmanagerment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.waterwood.shipmanagerment.api.TokenResult;
import org.waterwood.shipmanagerment.api.dto.request.PwdLoginReq;
import org.waterwood.shipmanagerment.api.dto.response.LoginRes;
import org.waterwood.shipmanagerment.api.enums.ResponseCode;
import org.waterwood.shipmanagerment.api.dto.request.RegisterReq;
import org.waterwood.shipmanagerment.entity.User;
import org.waterwood.shipmanagerment.infrastructure.component.RedisHelper;
import org.waterwood.shipmanagerment.infrastructure.exception.AuthException;
import org.waterwood.shipmanagerment.infrastructure.exception.BizException;
import org.waterwood.shipmanagerment.infrastructure.repository.UserRepository;
import org.waterwood.shipmanagerment.service.AuthService;
import org.waterwood.shipmanagerment.service.CaptchaService;
import org.waterwood.shipmanagerment.service.TokenService;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CaptchaService captchaService;
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final TokenService tokenService;
    private final RedisHelper redisHelper;

    @Override
    public void register(RegisterReq dto, String key) {
        captchaService.VerifyCaptcha(dto.getVerifyCode(), key);
        if( ! dto.getPassword().equals(dto.getConfirmPwd())){
            throw new BizException(ResponseCode.PASSWORD_NOT_MATCH);
        }
        userRepository.findByUsername((dto.getUsername()))
                .ifPresent(_ -> {
                    throw new BizException(ResponseCode.USER_ALREADY_EXISTS);
                });
        User u = new User();
        u.setUsername(dto.getUsername());
        u.setPassword(encoder.encode(dto.getPassword()));
        u.setNickname(dto.getNickname());
        userRepository.save(u);
    }

    @Override
    public LoginRes loginByPwd(PwdLoginReq dto, String key) {
        captchaService.VerifyCaptcha(dto.getVerifyCode(), key);
        User u = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new AuthException(ResponseCode.USERNAME_OR_PASSWORD_INCORRECT));

        if(! encoder.matches(dto.getPassword(), u.getPassword())){
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
