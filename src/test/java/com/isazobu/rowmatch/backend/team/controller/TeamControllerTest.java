package com.isazobu.rowmatch.backend.team.controller;

import com.isazobu.rowmatch.backend.exceptions.EntityAlreadyExistsException;
import com.isazobu.rowmatch.backend.exceptions.TeamCapacityFullException;
import com.isazobu.rowmatch.backend.team.dto.CreateTeamRequest;
import com.isazobu.rowmatch.backend.team.model.Team;
import com.isazobu.rowmatch.backend.team.service.TeamService;
import com.isazobu.rowmatch.backend.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import jdk.jfr.Description;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TeamControllerTest {

    @Mock
    private TeamService teamService;

    @InjectMocks
    private TeamController teamController;

    @Test
    @DisplayName("Create a new team successfully")
    public void testCreateTeam() {
        // Given
        CreateTeamRequest request = new CreateTeamRequest();
        request.setName("My Team");
        String token = "my_token";
        Team team = new Team(1L, "My Team", new ArrayList<>());

        when(teamService.createTeam(request, token)).thenReturn(team);

        // When
        ResponseEntity<Team> response = teamController.createTeam(request, token);

        // Then
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertEquals(response.getBody(), team);
    }


    @Test
    @DisplayName("Should return joined team with status 200")
    @Description("Test case for successful joinTeam request")
    public void testJoinTeam() {
        // arrange
        Long teamId = 123L;
        String token = "token123";
        Team team = new Team();
        team.setId(teamId);
        team.setName("Test team");
        team.addUsers(new User());
        when(teamService.joinTeam(teamId, token)).thenReturn(team);

        // act
        ResponseEntity<Team> responseEntity = teamController.joinTeam(teamId, token);

        // assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(team, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when team not found")
    @Description("Test case for joinTeam request when team is not found")
    public void testJoinTeamTeamNotFound() {
        // arrange
        Long teamId = 123L;
        String token = "token123";
        when(teamService.joinTeam(teamId, token)).thenThrow(new EntityNotFoundException("Team not found"));

        // act & assert
        assertThrows(EntityNotFoundException.class, () -> teamController.joinTeam(teamId, token));
    }

    @Test
    @DisplayName("Should throw EntityAlreadyExistsException when user already has a team")
    @Description("Test case for joinTeam request when user already has a team")
    public void testJoinTeamUserAlreadyHasTeam() {
        // arrange
        Long teamId = 123L;
        String token = "token123";
        when(teamService.joinTeam(teamId, token)).thenThrow(new EntityAlreadyExistsException("User already has a team"));

        // act & assert
        assertThrows(EntityAlreadyExistsException.class, () -> teamController.joinTeam(teamId, token));
    }

    @Test
    @DisplayName("Should throw TeamCapacityFullException when team is already full")
    @Description("Test case for joinTeam request when team is already full")
    public void testJoinTeamTeamCapacityFull() {
        // arrange
        Long teamId = 123L;
        String token = "token123";
        when(teamService.joinTeam(teamId, token)).thenThrow(new TeamCapacityFullException("Team is already full"));

        // act & assert
        assertThrows(TeamCapacityFullException.class, () -> teamController.joinTeam(teamId, token));
    }
}

