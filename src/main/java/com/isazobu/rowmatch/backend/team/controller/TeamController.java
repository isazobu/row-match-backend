package com.isazobu.rowmatch.backend.team.controller;

import com.isazobu.rowmatch.backend.team.dto.CreateTeamRequest;
import com.isazobu.rowmatch.backend.team.dto.GetTeams;
import com.isazobu.rowmatch.backend.team.model.Team;
import com.isazobu.rowmatch.backend.exceptions.NotEnoughCoinsException;
import com.isazobu.rowmatch.backend.team.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }


    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody CreateTeamRequest request, @RequestHeader("Authorization") String token) throws NotEnoughCoinsException {
        Team team = teamService.createTeam(request, token);
        return new ResponseEntity<Team>(team, HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Team> getTeamByName(@PathVariable String name) {
        Team team = teamService.getTeamByName(name);
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    @PostMapping("/{teamId}/join")
    public ResponseEntity<Team> joinTeam(@PathVariable Long teamId, @RequestHeader(value = "Authorization", required = true) String token) throws NotEnoughCoinsException {
        Team team = teamService.joinTeam(teamId, token);
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    @PostMapping("/leave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveTeam(@RequestHeader(value = "Authorization", required = true) String token) {
        teamService.leaveTeam(token);
    }

    @GetMapping("/random")
    public List<GetTeams> getRandomTeams() {
        List<GetTeams> teams = teamService.getRandomJoinableTeams();
        if (teams.isEmpty()) {
            return Collections.emptyList();
        }

        return teams;
    }
}
