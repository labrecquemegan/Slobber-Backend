package com.project.slobber.user;

import com.project.slobber.auth.AuthService;
import com.project.slobber.user.dtos.requests.EditUserRequest;
import com.project.slobber.util.annotations.Inject;
import com.project.slobber.util.custom_exception.InvalidRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class UserService {

    @Inject
    private final UserRepository userRepository;

    @Inject
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByUserId(String Id) {
        return userRepository.getUserByID(Id);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return userRepository.getUserByUsernameAndPassword(username, password);
    }

    public ArrayList<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User UpdateUser(String tokenId, EditUserRequest request) {
        User currentUser = userRepository.getUserByID(tokenId);

        if(!request.getEmail().equals("")) {
            if (new AuthService(userRepository).isValidEmail(request.getEmail()))
                currentUser.setEmail(request.getEmail());
            else{ // Email invalid format
                throw new InvalidRequestException("Email invalid!"); //Change to 406 later.
            }
        }

        if (!request.getBio().equals("") ) {
            if (request.getBio().length() < 255) currentUser.setBio(request.getBio());
            else throw new InvalidRequestException("Bio must be less than 255 characters!"); //Change to 406 later
        }

        if (request.getAge() > 13 || request.getAge() == 0) currentUser.setAge(request.getAge());
        else if (request.getAge() <= 13) throw new InvalidRequestException("Users must be older than 13 to use our services for Child Protection.");

        userRepository.updateUser(currentUser.getEmail(), currentUser.getBio(), currentUser.getAge(), currentUser.getId());//, isPasswordChanged);

        return currentUser;
    }
}
