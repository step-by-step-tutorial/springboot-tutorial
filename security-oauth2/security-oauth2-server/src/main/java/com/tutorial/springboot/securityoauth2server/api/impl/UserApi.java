package com.tutorial.springboot.securityoauth2server.api.impl;

import com.tutorial.springboot.securityoauth2server.api.AllApi;
import com.tutorial.springboot.securityoauth2server.dto.ChangeCredentialsDto;
import com.tutorial.springboot.securityoauth2server.dto.UserDto;
import com.tutorial.springboot.securityoauth2server.entity.User;
import com.tutorial.springboot.securityoauth2server.service.impl.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.tutorial.springboot.securityoauth2server.util.ApiErrorUtils.checkValidation;
import static com.tutorial.springboot.securityoauth2server.util.SecurityUtils.getCurrentUsername;

@RestController
@RequestMapping("/api/v1/users")
public class UserApi extends AllApi<Long, User, UserDto> {

    protected UserApi(UserService service) {
        super(service);
    }

    @Override
    protected UserService getService() {
        return (UserService) service;
    }

    @GetMapping("/userinfo")
    public ResponseEntity<Map<String, Object>> userInfo(@AuthenticationPrincipal Jwt jwt) {
        var userInfo = new HashMap<String, Object>();
        userInfo.put("sub", jwt.getClaimAsString("sub"));
        userInfo.put("aud", jwt.getClaimAsString("aud"));
        userInfo.put("iss", jwt.getClaimAsString("iss"));
        return ResponseEntity.ok(userInfo);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody @Validated ChangeCredentialsDto dto, BindingResult bindingResult) {
        logger.info("Received an inbound request to change password of {}", getCurrentUsername());
        checkValidation(bindingResult);
        getService().changePassword(dto.username(), dto.currentPassword(), dto.newPassword());
        return ResponseEntity.noContent().build();
    }
}
