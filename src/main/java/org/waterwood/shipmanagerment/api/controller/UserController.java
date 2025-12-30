package org.waterwood.shipmanagerment.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.waterwood.shipmanagerment.api.ApiResponse;
import org.waterwood.shipmanagerment.api.PostPolicyDto;
import org.waterwood.shipmanagerment.api.dto.request.ChangePwdReq;
import org.waterwood.shipmanagerment.api.dto.request.UserUpdateReq;
import org.waterwood.shipmanagerment.api.dto.response.CloudResPresignedUrlResp;
import org.waterwood.shipmanagerment.api.dto.response.UserInfoRes;
import org.waterwood.shipmanagerment.infrastructure.mapper.UserMapper;
import org.waterwood.shipmanagerment.infrastructure.utils.UserContext;
import org.waterwood.shipmanagerment.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @Operation(summary = "修改密码")
    @PostMapping("/change-pwd")
    public ApiResponse<Void> changePwd(@Valid @RequestBody ChangePwdReq dto,
                                       @Parameter(hidden = true) @AuthenticationPrincipal UserContext ctx){
        userService.changePwd(ctx.getUserId(), dto);
        return ApiResponse.success();
    }

    @Operation(summary = "获取用户头像")
    @GetMapping("/avatar")
    public ApiResponse<CloudResPresignedUrlResp> getAvatar(@Parameter(hidden = true) @AuthenticationPrincipal UserContext ctx){
        return ApiResponse.success(userService.getAvatar(ctx.getUserId()));
    }

    @Operation(summary = "获取头像上传的预检URL")
    @GetMapping("/avatar/upload")
    public ApiResponse<PostPolicyDto> getAvatarUploadPolicy(@Parameter String fileSuffix,
            @Parameter(hidden = true) @AuthenticationPrincipal UserContext ctx){
        return ApiResponse.success(
                userService.buildUploadPutPolicy(ctx.getUserId(), fileSuffix)
        );
    }

    @Operation(summary = "获取用户信息")
    @GetMapping
    public ApiResponse<UserInfoRes> getUserInfo(@Parameter(hidden = true) @AuthenticationPrincipal UserContext ctx){
        UserInfoRes res = userMapper.toDto(
                userService.getUser(ctx.getUserId())
        );
        res.setAvatar(
                userService.getAvatar(ctx.getUserId())
        );
        return ApiResponse.success(res);
    }

    @Operation(summary = "更新用户信息")
    @PutMapping
    public ApiResponse<UserInfoRes> updateUserProfile(@Valid @RequestBody UserUpdateReq dto,
                                                        @Parameter(hidden = true) @AuthenticationPrincipal UserContext ctx){
        UserInfoRes res = userMapper.toDto(
                userService.updateUser(ctx.getUserId(), dto)
        );
        return ApiResponse.success(res);
    }
}
