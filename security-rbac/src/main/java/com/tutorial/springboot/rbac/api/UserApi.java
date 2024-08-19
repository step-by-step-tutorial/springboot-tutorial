package com.tutorial.springboot.rbac.api;

import com.tutorial.springboot.rbac.dto.UserDto;
import com.tutorial.springboot.rbac.entity.User;
import com.tutorial.springboot.rbac.service.impl.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserApi extends AllApi<Long, User, UserDto> {

    protected UserApi(UserService service) {
        super(service);
    }
}
