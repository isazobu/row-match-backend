package com.isazobu.rowmatch.backend.team.service;

import com.isazobu.rowmatch.backend.exceptions.*;
import com.isazobu.rowmatch.backend.team.dto.CreateTeamRequest;
import com.isazobu.rowmatch.backend.team.dto.GetTeams;
import com.isazobu.rowmatch.backend.team.model.Team;
import com.isazobu.rowmatch.backend.team.repository.TeamRepository;
import com.isazobu.rowmatch.backend.user.model.User;
import com.isazobu.rowmatch.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {


    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamServiceImpl(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public Team createTeam(CreateTeamRequest request, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getTeam() != null) {
            throw new EntityAlreadyExistsException("User already has a team");
        }

        String teamName = request.getName();
        Optional<Team> teamOptional = teamRepository.findByName(teamName);
        if (teamOptional.isPresent()) {
            throw new EntityAlreadyExistsException("Team name already exists");
        }

        // create team and add the user to it
        Team team = new Team();
        team.setName(teamName);
        team.addUsers(user);


        return teamRepository.save(team);
    }


    @Override
    public Team getTeamByName(String name) {
        Optional<Team> teamOptional = teamRepository.findByName(name);
        if (teamOptional.isEmpty()) {
            throw new EntityNotFoundException("Team not found");
        }
        return teamOptional.get();
    }

    @Override
    public Team joinTeam(Long teamId, String token) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Team not found"));

        List<User> users = team.getUsers();
        if (users.size() >= 20) {
            throw new TeamCapacityFullException("Team is already full");
        }

        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getTeam() != null) {
            throw new UserAlreadyInTeamException("User already has a team");
        }


        team.addUsers(user);
        return teamRepository.save(team);
    }

    @Override
    @Transactional
    public void leaveTeam(String token) throws TeamNotFoundException, UserNotFoundException, UserNotInTeamException {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getTeam() == null) {
            throw new UserNotInTeamException("User is not in a team");
        }

        Team userTeam = user.getTeam();
        // if user is the leader, delete the team
        if (userTeam.getUsers().size() == 1) {

//            userTeam.removeUsers(user);
            userTeam.getUsers().remove(user);
            user.setTeam(null);
            teamRepository.deleteById(userTeam.getId());
            return;
        }

        Hibernate.initialize(userTeam);
        Long teamId = userTeam.getId();
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        team.removeUsers(user);
        teamRepository.save(team);

    }


    @Override
    public List<GetTeams> getRandomJoinableTeams() {
        // I want to get 10 random teams that have less t   han 20 users
        List<Team> teams = teamRepository.findAll();
        Collections.shuffle(teams); // randomize the order of teams

        // just  return name
        List<GetTeams> responseTeam = teams.stream()
                .filter(team -> team.getUsers().size() < 20)
                .limit(10)
                .map(team -> new GetTeams(team.getId(), team.getName(), team.getUsers().size()))
                .collect(Collectors.toList());


        return responseTeam;

    }


}
