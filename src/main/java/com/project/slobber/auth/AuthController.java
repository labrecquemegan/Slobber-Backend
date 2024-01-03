package com.project.slobber.auth;

import com.project.slobber.auth.dtos.requests.LoginRequest;
import com.project.slobber.auth.dtos.requests.NewUserRequest;
import com.project.slobber.auth.dtos.response.Principal;


import com.project.slobber.util.annotations.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/*
 allows us to combine @Controller + @ResponseBody for easier request handling
 by using spring MVC annotations and Spring's classpath-scanning.
 Using @RestController allows us to not have to type out the @ResponseBody every time
 */

@RestController
@RequestMapping("/auth")
public class AuthController {

    // @Inject allows us to inject these constructors when we need them at runtime
    @Inject
    private final AuthService authService;
    private final TokenService tokenService;

    // creates a bean that we can use through Dependency Injection
    @Inject
    @Autowired
    public AuthController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    /**
     * Returns a principal containing the login token when given an appropriate login request
     * @param request A JSON object containing the username and password of the user
     * @param resp The servlet response that the header will be
     * @return Returns a principal with a token
     */

    @CrossOrigin
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Principal login(@RequestBody LoginRequest request, HttpServletResponse resp) {
        Principal principal = new Principal(authService.login(request));
        String token = tokenService.generateToken(principal);
        principal.setToken(token);
        resp.setHeader("Authorization", token);
        return principal;
    }

    /**
     * Creates a new user in the users table based on the attributes of the newUserRequest JSON
     * @param newUserRequest A JSON object containing the details to create a new user
     */

    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/newuser", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public void createUser(@RequestBody NewUserRequest newUserRequest) {
        authService.register(newUserRequest);
    }

}
