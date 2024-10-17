package com.tutorial.springboot.securityoauth2client.api.impl;

import com.tutorial.springboot.securityoauth2client.api.AllApi;
import com.tutorial.springboot.securityoauth2client.dto.UserDto;
import com.tutorial.springboot.securityoauth2client.entity.User;
import com.tutorial.springboot.securityoauth2client.service.impl.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserApi extends AllApi<Long, User, UserDto> {

    protected UserApi(UserService service) {
        super(service);
    }
}
