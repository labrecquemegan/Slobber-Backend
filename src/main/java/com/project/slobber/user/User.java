package com.project.slobber.user;

import javax.persistence.*;

// This class defines the user. Uses the persistence API to define the Datatable

@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;
    @Column (name = "username", nullable = false)
    private String username;
    @Column (name = "password", nullable = false)
    private String password;
    @Column (name = "email", nullable = false)
    private String email;
    @Column (name = "role", nullable = false)
    private String role;
    @Column (name = "bio")
    private String bio;
    @Column (name = "age")
    private int age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
