package com.isazobu.rowmatch.backend;

import com.isazobu.rowmatch.backend.team.model.Team;
import com.isazobu.rowmatch.backend.team.repository.TeamRepository;
import com.isazobu.rowmatch.backend.user.model.User;
import com.isazobu.rowmatch.backend.user.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;


    User user;
    List<User> users = new ArrayList<>();
    String generatedString;

    void generateUser(Team team) {
        for (int i = 0; i < 10; i++) {
            generatedString = RandomStringUtils.randomAlphabetic(10);
            user = new User();
            user.setName(generatedString);
            user.setLevel(i);
            user.setCoins(5000 + i * 25);
            user.setToken(generatedString);
            user.setTeam(team);
            users.add(user);
        }
    }


    @Override
    public void run(String... args) throws Exception {


        // Create some sample teams
        Team team1 = new Team();
        team1.setName("Team 1");

        Team team2 = new Team();
        team2.setName("Team 2");

        // Save the teams to the database

        teamRepository.deleteAll();

        teamRepository.save(team1);
        teamRepository.save(team2);


        generateUser(team1);
        generateUser(team2);


        userRepository.saveAll(users);

    }
}
