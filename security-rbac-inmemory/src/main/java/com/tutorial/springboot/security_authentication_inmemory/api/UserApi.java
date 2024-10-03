package com.tutorial.springboot.security_authentication_inmemory.api;

import com.tutorial.springboot.security_authentication_inmemory.dto.UserDto;
import com.tutorial.springboot.security_authentication_inmemory.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/v1/users")
public class UserApi {

    private final UserService userService;

    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("whoami")
    public ResponseEntity<String> whoami() {
        return ResponseEntity.ok(String.format("Current user is %s", userService.currentUsername()));
    }


    @GetMapping("{username}")
    public ResponseEntity<UserDto> findByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElseThrow();
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody UserDto newUser) {
        userService.save(newUser);
        var uri = fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(newUser.username())
                .toUri();
        return created(uri).body("User created");
    }

}
