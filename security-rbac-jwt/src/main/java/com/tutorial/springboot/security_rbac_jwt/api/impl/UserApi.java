package com.tutorial.springboot.security_rbac_jwt.api.impl;

import com.tutorial.springboot.security_rbac_jwt.api.AllApi;
import com.tutorial.springboot.security_rbac_jwt.dto.ChangeCredentialsDto;
import com.tutorial.springboot.security_rbac_jwt.dto.UserDto;
import com.tutorial.springboot.security_rbac_jwt.entity.User;
import com.tutorial.springboot.security_rbac_jwt.service.impl.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tutorial.springboot.security_rbac_jwt.util.ApiErrorUtils.checkValidation;
import static com.tutorial.springboot.security_rbac_jwt.util.SecurityUtils.getCurrentUsername;

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

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody @Validated ChangeCredentialsDto dto, BindingResult bindingResult) {
        logger.info("Received an inbound request to change password of {}", getCurrentUsername());
        checkValidation(bindingResult);
        getService().changePassword(dto.username(), dto.password(), dto.newPassword());
        return ResponseEntity.noContent().build();
    }
}
