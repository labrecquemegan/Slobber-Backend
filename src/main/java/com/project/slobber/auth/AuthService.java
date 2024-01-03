package com.project.slobber.auth;

import com.project.slobber.auth.dtos.requests.NewUserRequest;
import com.project.slobber.auth.dtos.requests.LoginRequest;
import com.project.slobber.user.User;
import com.project.slobber.user.UserRepository;
import com.project.slobber.util.annotations.Inject;
import com.project.slobber.util.custom_exception.InvalidRequestException;
import com.project.slobber.util.custom_exception.ResourceConflictException;

import java.lang.reflect.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    @Inject
    private final UserRepository userRepository;

    @Inject
    @Autowired
    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User login(LoginRequest loginRequest){
        if(loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            throw new InvalidRequestException(); //401
        }
        if(!isValidUsername(loginRequest.getUsername()) || !isValidPassword(loginRequest.getPassword())){
            throw new InvalidRequestException("invalid username or password");
        }
        User user = userRepository.getUserByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        if(user == null) {
            throw new InvalidRequestException(("Invalid login request, make sure you have an account"));
        }
        return user;
    }

    public User register(NewUserRequest request){
        User user = request.extractUser();

        //validation checks
        String message = nullChecker(request);
        if(!message.isEmpty()) throw new InvalidRequestException(message);
        if(request.getAge() < 13) throw new InvalidRequestException("User age is below 13");
        if(!isValidBio(request.getBio())) throw new InvalidRequestException("Bio is longer than 255 characters");
        if(userExists(user.getUsername())) throw new ResourceConflictException("This username is already taken");
        if(!isValidUsername(user.getUsername())) throw new InvalidRequestException("Invalid username, must be 8-20 characters long and no special characters except _ and .");
        if(!isValidPassword(user.getPassword())) throw new InvalidRequestException("Invalid password, must be longer than 8 characters and contain one number, one special character, and one alphabetical character");
        if(!isValidEmail(user.getEmail())) throw new InvalidRequestException("Invalid email, must be a valid email address");

        userRepository.saveUser(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRole(), user.getBio(), user.getAge());
        return user;
    }

    private boolean userExists(String username){
        return userRepository.getUserByUsername(username) != null;
    }


    // nullChecker() checks to see if a field is null and returns to the user the field that is null, so they can fix it
    private String nullChecker(NewUserRequest request){
        String eMessage = "";
        try {
            Field[] fields = com.project.slobber.auth.dtos.requests.NewUserRequest.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(request) == null) {
                    if(!eMessage.isEmpty()){
                        eMessage += ", ";
                    }
                    eMessage += field.getName() + " is null";
                }
            }
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return eMessage;
    }

    // Validation checking
    public boolean isValidEmail(String email){
        return email.matches("^([\\w][\\-\\_\\.]?)*\\w@([\\w+]\\-?)*\\w\\.\\w+$");
    }

    private boolean isValidUsername(String username){
        return username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
    }

    private boolean isValidPassword(String password){
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
    }

    private boolean isValidBio(String bio){
        return bio.length() < 255;
    }
}
