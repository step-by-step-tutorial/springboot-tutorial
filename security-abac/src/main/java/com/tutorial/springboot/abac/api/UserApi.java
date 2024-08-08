package com.tutorial.springboot.abac.api;

import com.tutorial.springboot.abac.dto.UserDto;
import com.tutorial.springboot.abac.entity.User;
import com.tutorial.springboot.abac.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserApi extends AbstractApi<Long, User, UserDto> {

    protected UserApi(UserService service) {
        super(service);
    }
}
