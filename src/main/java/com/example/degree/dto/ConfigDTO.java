package com.example.degree.dto;

public class ConfigDTO {
    private Long configId;
    private Long masterTypeId;
    private String value;

    // Getters and Setters
    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public Long getMasterTypeId() {
        return masterTypeId;
    }

    public void setMasterTypeId(Long masterTypeId) {
        this.masterTypeId = masterTypeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}