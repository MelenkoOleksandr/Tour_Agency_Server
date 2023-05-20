package com.example.touragency.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    private String img;
    private String name;
    private String surname;
    private String phone;
    private UserType userType;

    public User(String email, String password, UserType userType, String name) {
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.name = name;
    }

    public User(long userId, String email, String img, String name, String surname, String phone) {
        this.id = userId;
        this.email = email;
        this.img = img;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
    }
}