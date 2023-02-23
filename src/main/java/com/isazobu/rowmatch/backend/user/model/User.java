package com.isazobu.rowmatch.backend.user.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.isazobu.rowmatch.backend.team.model.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
public class User implements Serializable {

    // user uuid
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String name;

    @Column
    private Integer level = 1;

    @Column
    private Integer coins = 5000;


    @Column
    private String token = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Team team;


    //
    public User(Long id, String name, Integer level, Integer coins, String token) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.coins = coins;
        this.token = token;
    }
//    public User(){
//
//    }
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "uuid='" + id + '\'' +
//                ", name='" + name + '\'' +
//                ", level=" + level +
//                ", coins=" + coins +
//                ", token='" + token + '\'' +
//                '}';
//    }
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Integer getLevel() {
//        return level;
//    }
//
//    public void setLevel(Integer level) {
//        this.level = level;
//    }
//
//    public Integer getCoins() {
//        return coins;
//    }
//
//    public void setCoins(Integer coins) {
//        this.coins = coins;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    @JsonBackReference
//    public Team getTeam() {
//        return team;
//    }
//
//    public void setTeam(Team team) {
//        this.team = team;
//    }
}
