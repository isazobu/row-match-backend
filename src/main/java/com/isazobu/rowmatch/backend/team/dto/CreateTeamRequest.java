package com.isazobu.rowmatch.backend.team.dto;

public class CreateTeamRequest {
    private String name;

    private int userId;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }


}
