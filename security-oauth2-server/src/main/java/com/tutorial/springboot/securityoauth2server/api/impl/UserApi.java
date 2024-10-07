package com.tutorial.springboot.securityoauth2server.api.impl;

import com.tutorial.springboot.securityoauth2server.api.AllApi;
import com.tutorial.springboot.securityoauth2server.dto.UserDto;
import com.tutorial.springboot.securityoauth2server.entity.User;
import com.tutorial.springboot.securityoauth2server.service.impl.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserApi extends AllApi<Long, User, UserDto> {

    protected UserApi(UserService service) {
        super(service);
    }
}
