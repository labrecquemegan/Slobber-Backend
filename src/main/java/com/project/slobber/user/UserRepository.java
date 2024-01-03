package com.project.slobber.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

// This class is used to query the DB

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    //GET values
    @Query (value = "SELECT * FROM users WHERE id = ?1", nativeQuery = true)
    User getUserByID(String id);
    @Query (value = "SELECT * FROM users WHERE username = ?1", nativeQuery = true)
    User getUserByUsername(String username);
    @Query (value = "SELECT * FROM users WHERE username = ?1 AND password = crypt(?2, password)", nativeQuery = true)
    User getUserByUsernameAndPassword(String username, String password);
    @Query (value = "SELECT * FROM users", nativeQuery = true)
    ArrayList<User> getAllUsers();

    //POST values
    @Modifying
    @Query (value = "INSERT INTO users (id, username, password, email, role, bio, age) VALUES (?1, ?2, crypt(?3, gen_salt('bf')), ?4, ?5, ?6, ?7)", nativeQuery = true)
    public void saveUser(String id, String username, String password, String email, String role, String bio, int age);

    //PUT values
    @Modifying
    @Query (value = "UPDATE users SET email = ?1, bio = ?2, age = ?3 WHERE id = ?4", nativeQuery = true)
    void updateUser(String email, String bio, int age, String id);

}
