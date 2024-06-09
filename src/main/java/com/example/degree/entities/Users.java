package com.example.degree.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Degree> degrees = new HashSet<>();

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Set<Degree> getDegrees() {
        return degrees;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDegrees(Set<Degree> degrees) {
        this.degrees = degrees;
    }
}


