package com.isazobu.rowmatch.backend.team.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.isazobu.rowmatch.backend.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<User> users = new ArrayList<>();


    public Team addUsers(User user) {
        this.users.add(user);
        user.setTeam(this);
        return this;
    }


    public Team removeUsers(User user) {
        this.users.remove(user);
        user.setTeam(null);
        return this;
    }

    public User getFirstUser() {
        return this.users.get(0);
    }


//
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
//    @JsonManagedReference
//    public List<User> getUsers() {
//        return users;
//    }
//
//    public void setUsers(List<User> users) {
//        this.users = users;
//    }
//

}
