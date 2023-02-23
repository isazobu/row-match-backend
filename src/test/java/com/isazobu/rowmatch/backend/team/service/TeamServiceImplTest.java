package com.isazobu.rowmatch.backend.team.service;


import com.isazobu.rowmatch.backend.exceptions.EntityAlreadyExistsException;
import com.isazobu.rowmatch.backend.exceptions.TeamCapacityFullException;
import com.isazobu.rowmatch.backend.exceptions.UserAlreadyInTeamException;
import com.isazobu.rowmatch.backend.exceptions.UserNotInTeamException;
import com.isazobu.rowmatch.backend.team.dto.CreateTeamRequest;
import com.isazobu.rowmatch.backend.team.model.Team;
import com.isazobu.rowmatch.backend.team.repository.TeamRepository;
import com.isazobu.rowmatch.backend.user.model.User;
import com.isazobu.rowmatch.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@DisplayName("TeamService.createTeam")
@Description("Test case for TeamService#createTeam method")
class TeamServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create a team when user does not have a team and team name is unique")
    @Description("Test case to verify that a team can be created successfully")
    void testCreateTeamSuccess() {
        // arrange
        String token = "valid_token";
        User user = new User();
        user.setToken(token);
        Team team = new Team();
        team.setName("unique_team_name");

        CreateTeamRequest request = new CreateTeamRequest();
        request.setName(team.getName());

        when(userRepository.findByToken(token)).thenReturn(Optional.of(user));
        when(teamRepository.findByName(team.getName())).thenReturn(Optional.empty());
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // act
        Team result = teamService.createTeam(request, token);

        // assert
        assertNotNull(result);
        assertEquals(team.getName(), result.getName());

        verify(userRepository).findByToken(token);
        verify(teamRepository).findByName(team.getName());
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    @DisplayName("Should throw EntityAlreadyExistsException when team name already exists")
    @Description("Test case to verify that EntityAlreadyExistsException is thrown when team name already exists")
    void testCreateTeamAlreadyExists() {
        // arrange
        String token = "valid_token";
        User user = new User();
        user.setToken(token);
        Team team = new Team();
        team.setName("existing_team_name");

        CreateTeamRequest request = new CreateTeamRequest();
        request.setName(team.getName());

        when(userRepository.findByToken(token)).thenReturn(Optional.of(user));
        when(teamRepository.findByName(team.getName())).thenReturn(Optional.of(team));

        // act and assert
        assertThrows(EntityAlreadyExistsException.class, () -> {
            teamService.createTeam(request, token);
        });

        verify(userRepository).findByToken(token);
        verify(teamRepository).findByName(team.getName());
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    @DisplayName("Should throw EntityAlreadyExistsException when user already has a team")
    @Description("Test case to verify that EntityAlreadyExistsException is thrown when user already has a team")
    void testCreateTeamUserHasTeam() {
        // arrange
        String token = "valid_token";
        User user = new User();
        user.setToken(token);
        Team team = new Team();
        team.setName("new_team_name");
        user.setTeam(team);

        CreateTeamRequest request = new CreateTeamRequest();
        request.setName(team.getName());

        when(userRepository.findByToken(token)).thenReturn(Optional.of(user));

        // act and assert
        assertThrows(EntityAlreadyExistsException.class, () -> {
            teamService.createTeam(request, token);
        });

        verify(userRepository).findByToken(token);
        verify(teamRepository, never()).findByName(team.getName());
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when user not found")
    @Description("Test case to verify that EntityNotFoundException is thrown when user not found")
    void testCreateTeamUserNotFound() {
        // arrange
        String token = "invalid_token";
        CreateTeamRequest request = new CreateTeamRequest();
        request.setName("Test Team");
        // Mocking userRepository behavior
        when(userRepository.findByToken(token)).thenReturn(Optional.empty());

        // Ensure that the method throws an EntityNotFoundException
        assertThrows(EntityNotFoundException.class, () -> {
            teamService.createTeam(request, token);
        });

        // Verify that userRepository.findByToken() was called exactly once with the token argument
        verify(userRepository, times(1)).findByToken(token);

    }


    @Nested
    @DisplayName("Join Team")
    class JoinTeam {


        private User user;
        private Team team;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            user = new User();
            user.setName("User A");
            user.setToken("token");

            team = new Team();
            team.setName("Team A");
            team.setId(1L);
        }

        @Test
        @DisplayName("Join team successfully")
        @Description("Join a team successfully when user is not part of any team and team is not full")
        void joinTeamSuccessfully() {
            // Arrange
            when(userRepository.findByToken(anyString())).thenReturn(Optional.of(user));
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            when(teamRepository.save(any(Team.class))).thenReturn(team);

            // Act
            Team joinedTeam = teamService.joinTeam(1L, "token");


            // Assert
            assertEquals(joinedTeam, team);


            assertTrue(joinedTeam.getUsers().contains(user));


            verify(userRepository).findByToken(anyString());
            verify(teamRepository).findById(anyLong());
            verify(teamRepository).save(any(Team.class));
            verifyNoMoreInteractions(userRepository, teamRepository);
        }

        @Test
        @DisplayName("Join team fails when user already has a team")
        @Description("Joining a team fails when the user is already part of a team")
        void joinTeamFailsWhenUserAlreadyHasTeam() {
            // Arrange
            Team newTeam = new Team();
            newTeam.setName("Team B");
            newTeam.addUsers(user);

            when(userRepository.findByToken(anyString())).thenReturn(Optional.of(user));
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));

            // Act
            assertThrows(UserAlreadyInTeamException.class, () -> {
                teamService.joinTeam(team.getId(), user.getToken());
            });

            // Assert
            verify(userRepository).findByToken(anyString());
            verify(teamRepository).findById(anyLong());
            verifyNoMoreInteractions(userRepository, teamRepository);
        }

        @Test
        @DisplayName("Join team fails when team is full")
        @Description("Joining a team fails when the team is already at full capacity (20 users)")
        void joinTeamFailsWhenTeamIsFull() {
            // Arrange
            User tempUser;
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                tempUser = new User();
                tempUser.setName("User " + i);
                tempUser.setToken("token" + i);
                team.addUsers(tempUser);
            }

            when(userRepository.findByToken(anyString())).thenReturn(Optional.of(user));
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            // Act and Assert
            assertThrows(TeamCapacityFullException.class, () -> {
                teamService.joinTeam(team.getId(), user.getToken());
            });
            verify(teamRepository).findById(1L);
            verifyNoMoreInteractions(teamRepository);
        }

        @Test
        @DisplayName("Join team fails when team is not found")
        @Description("Joining a team fails when the team is not found")
        void joinTeamFailsWhenTeamIsNotFound() {
            // Arrange
            when(userRepository.findByToken(anyString())).thenReturn(Optional.of(user));
            when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());
            // Act and Assert
            assertThrows(EntityNotFoundException.class, () -> {
                teamService.joinTeam(team.getId(), user.getToken());
            });
            verify(teamRepository).findById(1L);
            verifyNoMoreInteractions(teamRepository);
        }

        @Test
        @DisplayName("Join team fails when user is not found")
        @Description("Joining a team fails when the user is not found")
        void joinTeamFailsWhenUserIsNotFound() {
            // Arrange
            when(userRepository.findByToken(anyString())).thenReturn(Optional.empty());
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            // Act and Assert
            assertThrows(EntityNotFoundException.class, () -> {
                teamService.joinTeam(team.getId(), user.getToken());
            });
            verify(teamRepository).findById(1L);
            verifyNoMoreInteractions(teamRepository);
        }
    }

    @Nested
    @DisplayName("Leave Team")
    class LeaveTeam {
        private User user;
        private Team team;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            user = new User();
            user.setName("User A");
            user.setToken("token");

            team = new Team();
            team.setName("Team A");
            team.setId(1L);
        }

        @Test
        @DisplayName("Leave team successfully")
        @Description("Leave a team successfully when user is part of a team")
        void leaveTeamSuccessfully() {
            // Arrange
            team.addUsers(user);
            when(userRepository.findByToken(anyString())).thenReturn(Optional.of(user));
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            when(teamRepository.save(any(Team.class))).thenReturn(team);


            // Act
            teamService.leaveTeam("token");

            // Assert
            assertFalse(team.getUsers().contains(user));
            verify(userRepository).findByToken(anyString());
            verify(teamRepository).findById(anyLong());
            verify(teamRepository).save(any(Team.class));
            verifyNoMoreInteractions(userRepository, teamRepository);
        }

        @Test
        @DisplayName("Leave team fails when user is not part of a team")
        @Description("Leaving a team fails when the user is not part of a team")
        void leaveTeamFailsWhenUserIsNotPartOfTeam() {
            // Arrange
            when(userRepository.findByToken(anyString())).thenReturn(Optional.of(user));
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));

            // Act and Assert
            assertThrows(UserNotInTeamException.class, () -> {
                teamService.leaveTeam(user.getToken());
            });
            verify(teamRepository).findById(1L);
            verifyNoMoreInteractions(teamRepository);
        }

        @Test
        @DisplayName("Leave team fails when team is not found")
        @Description("Leaving a team fails when the team is not found")
        void leaveTeamFailsWhenTeamIsNotFound() {
            // Arrange
            when(userRepository.findByToken(anyString())).thenReturn(Optional.of(user));
            when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());
            // Act and Assert
            assertThrows(EntityNotFoundException.class, () -> {
                teamService.leaveTeam(user.getToken());
            });
            verify(teamRepository).findById(1L);
            verifyNoMoreInteractions(teamRepository);
        }

        @Test
        @DisplayName("Leave team fails when user is not found")
        @Description("Leaving a team fails when the user is not found")
        void leaveTeamFailsWhenUserIsNotFound() {
            // Arrange
            when(userRepository.findByToken(anyString())).thenReturn(Optional.empty());
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            // Act and Assert
            assertThrows(EntityNotFoundException.class, () -> {
                teamService.leaveTeam(user.getToken());
            });
            verify(teamRepository).findById(1L);
            verifyNoMoreInteractions(teamRepository);
        }

        // Verify that leaving a team returns the updated user profile information to the client.
        @Test
        @DisplayName("Leave team returns updated user profile")
        @Description("Leaving a team returns the updated user profile information to the client")
        void leaveTeamReturnsUpdatedUserProfile() {
            // Arrange
            team.addUsers(user);
            when(userRepository.findByToken(anyString())).thenReturn(Optional.of(user));
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            when(teamRepository.save(any(Team.class))).thenReturn(team);
            // Act
            teamService.leaveTeam("token");

            // Assert
            assertFalse(team.getUsers().contains(user));
            verify(userRepository).findByToken(anyString());
            verify(teamRepository).findById(anyLong());
            verify(teamRepository).save(any(Team.class));
            verifyNoMoreInteractions(userRepository, teamRepository);

        }

    }

}