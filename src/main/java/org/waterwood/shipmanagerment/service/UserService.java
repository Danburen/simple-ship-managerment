package org.waterwood.shipmanagerment.service;

import org.jetbrains.annotations.Nullable;
import org.waterwood.shipmanagerment.api.PostPolicyDto;
import org.waterwood.shipmanagerment.api.dto.request.ChangePwdReq;
import org.waterwood.shipmanagerment.api.dto.request.UserUpdateReq;
import org.waterwood.shipmanagerment.api.dto.response.CloudResPresignedUrlResp;
import org.waterwood.shipmanagerment.api.dto.response.UserInfoRes;
import org.waterwood.shipmanagerment.entity.User;

public interface UserService {
    void changePwd(long userId, ChangePwdReq dto);

    User getUser(long userId);

    @Nullable CloudResPresignedUrlResp getAvatar(long userId);

    PostPolicyDto buildUploadPutPolicy(long userId, String fileSuffix);

    User updateUser(long userId, UserUpdateReq dto);
}
