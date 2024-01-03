package com.project.slobber.user;

import com.project.slobber.auth.TokenService;
import com.project.slobber.auth.dtos.response.Principal;
import com.project.slobber.user.dtos.requests.EditUserRequest;
import com.project.slobber.util.annotations.Inject;
import com.project.slobber.util.custom_exception.InvalidRequestException;
import com.project.slobber.util.custom_exception.UnauthorizedException;
import com.project.slobber.util.custom_exception.ResourceConflictException;
import com.project.slobber.util.custom_exception.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Inject
    private final UserService userService;

    @Inject
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin
    @GetMapping(value = "/all-users")
    public @ResponseBody ArrayList<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @CrossOrigin
    @GetMapping(value = "user-id/{id}")
    public @ResponseBody User getUserById(@PathVariable String id) {
        return userService.getByUserId(id);
    }

    @CrossOrigin
    @GetMapping(value = "user-username/{username}")
    public @ResponseBody User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @CrossOrigin
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping(value = "/edit", consumes="application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody User editUser(@RequestHeader("Authorization") String token, @RequestBody EditUserRequest request) {
        Principal principal = new TokenService().extractRequesterDetails(token);
        if (principal.getId() == null) throw new UnauthorizedException();

        return userService.UpdateUser(principal.getId(), request);
    }


}
