package com.example.degree.entities;

import jakarta.persistence.*;

import java.util.List;
@Entity
public class MasterType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // National or International



    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

}
