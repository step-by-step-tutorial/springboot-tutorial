package com.tutorial.springboot.security_authentication_inmemory.api;

import com.tutorial.springboot.security_authentication_inmemory.dto.UserDto;
import com.tutorial.springboot.security_authentication_inmemory.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/v1/users")
public class UserApi {

    private final UserService userService;

    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok(String.format("Hello, %s!", userService.currentUsername()));
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody UserDto newUser) {
        return userService.save(newUser)
                .map(user -> created(createUriFor(newUser.username())).body("User created"))
                .orElseThrow();
    }

    private static URI createUriFor(String username) {
        return fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(username)
                .toUri();
    }
}
