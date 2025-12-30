package org.waterwood.shipmanagerment.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.waterwood.shipmanagerment.api.KeyConstants;
import org.waterwood.shipmanagerment.api.PostPolicyDto;
import org.waterwood.shipmanagerment.api.dto.request.ChangePwdReq;
import org.waterwood.shipmanagerment.api.dto.request.UserUpdateReq;
import org.waterwood.shipmanagerment.api.dto.response.CloudResPresignedUrlResp;
import org.waterwood.shipmanagerment.api.enums.ResponseCode;
import org.waterwood.shipmanagerment.entity.User;
import org.waterwood.shipmanagerment.infrastructure.component.RedisHelper;
import org.waterwood.shipmanagerment.infrastructure.exception.BizException;
import org.waterwood.shipmanagerment.infrastructure.mapper.UserMapper;
import org.waterwood.shipmanagerment.infrastructure.repository.UserRepository;
import org.waterwood.shipmanagerment.infrastructure.utils.PathUtil;
import org.waterwood.shipmanagerment.service.UserService;
import org.waterwood.shipmanagerment.service.storage.CloudFileService;
import org.waterwood.shipmanagerment.service.storage.CloudResOperationType;
import org.waterwood.shipmanagerment.service.storage.CloudResType;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final CloudFileService cloudFileService;
    private final UserMapper userMapper;
    private final RedisHelper redisHelper;

    public UserServiceImpl(UserRepository userRepository, CloudFileService cloudFileService, UserMapper userMapper, RedisHelper redisHelper) {
        this.userRepository = userRepository;
        this.cloudFileService = cloudFileService;
        this.userMapper = userMapper;
        this.redisHelper = redisHelper;
    }

    @Override
    public void changePwd(long userId, ChangePwdReq dto) {
        User u = getUser(userId);
        if(! dto.getConfirmPassword().equals(dto.getNewPassword())){
            throw new BizException(ResponseCode.PASSWORD_NOT_MATCH);
        }

        if(! encoder.matches(dto.getOldPassword(), u.getPassword())){
            throw new BizException(ResponseCode.PASSWORD_INCORRECT);
        }
        u.setPassword(encoder.encode(dto.getNewPassword()));
        userRepository.save(u);
    }

    @Override
    public User getUser(long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new BizException(ResponseCode.USER_NOT_FOUND));
    }

    @Override
    public @Nullable CloudResPresignedUrlResp getAvatar(long userId) {
        User u = getUser(userId);
        String avatarPath = u.getAvatarUrl();
        if(avatarPath == null){
            return null;
        }
        return cloudFileService.getReadPublicUrlCached(
                PathUtil.buildPath(
                        KeyConstants.UPLOADS,
                        KeyConstants.AVATAR,
                        avatarPath),
                userId,
                CloudResType.AVATAR
        );
    }

    @Override
    public PostPolicyDto buildUploadPutPolicy(long userId, String fileSuffix) {
        if(fileSuffix == null){
            throw new BizException(ResponseCode.NEED_FILE_TYPE);        }
        String avatarPath = PathUtil.getUniquePathFile(fileSuffix);
        User u = getUser(userId);
        cloudFileService.removeUploadsFile(
                PathUtil.buildPath(KeyConstants.AVATAR, u.getAvatarUrl())
        );
        // clean cached avatar
        String cacheKey = cloudFileService.getCachedRedisKey(u.getId(), CloudResType.AVATAR, CloudResOperationType.READ);
        redisHelper.del(cacheKey);

        u.setAvatarUrl(avatarPath);
        userRepository.save(u);

        return cloudFileService.buildUploadPutPolicy(PathUtil.buildPath(
                KeyConstants.AVATAR,
                avatarPath
        ));
    }

    @Override
    public User updateUser(long userId, UserUpdateReq dto) {
        User u = getUser(userId);
        u = userMapper.partialUpdate(dto, u);
        return userRepository.save(u);
    }
}
