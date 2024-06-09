package com.example.degree.entities;

import jakarta.persistence.*;

import java.util.List;
@Entity
public class ConfigTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ConfigId;

    private String value; // Degree type (list of international/national degrees)

    @ManyToOne
    @JoinColumn(name = "master_type_id")
    private MasterType masterType;

    @OneToMany(mappedBy = "configTable")
    private List<Degree> degrees;



    public String getValue() {
        return value;
    }

    public MasterType getMasterType() {
        return masterType;
    }

    public List<Degree> getDegrees() {
        return degrees;
    }

    public void setValue(String value) {
        this.value = value;
    }



    public void setMasterType(MasterType masterType) {
        this.masterType = masterType;
    }

    public void setDegrees(List<Degree> degrees) {
        this.degrees = degrees;
    }

    public Long getConfigId() {
        return ConfigId;
    }

    public void setConfigId(Long configId) {
        ConfigId = configId;
    }
}
