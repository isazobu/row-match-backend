package com.isazobu.rowmatch.backend.team.service;

import com.isazobu.rowmatch.backend.exceptions.*;
import com.isazobu.rowmatch.backend.team.dto.CreateTeamRequest;
import com.isazobu.rowmatch.backend.team.dto.GetTeams;
import com.isazobu.rowmatch.backend.team.model.Team;

import java.util.List;

public interface TeamService {

    /**
     * Creates a new team with the given team name, and the user with the given userId as the team leader.
     *
     * @param request the request containing the team name and the user id of the team leader
     * @return the newly created team
     * @throws TeamNameExistsException    if a team with the given name already exists
     * @throws UserNotFoundException      if the user with the given userId does not exist
     * @throws UserCoinNotEnoughException if the user with the given userId does not have enough coins to create a team
     * @throws TeamCapacityFullException  if the maximum number of teams (20) has been reached
     */
    Team createTeam(CreateTeamRequest request, String token)
            throws TeamNameExistsException, UserNotFoundException, UserCoinNotEnoughException, TeamCapacityFullException;

    /**
     * Adds the user with the given userId to the team with the given teamId.
     *
     * @param teamId the id of the team to join
     * @return the updated team
     * @throws TeamNotFoundException       if the team with the given teamId does not exist
     * @throws UserNotFoundException       if the user with the given userId does not exist
     * @throws TeamCapacityFullException   if the team is already at maximum capacity
     * @throws UserAlreadyInTeamException  if the user with the given userId is already a member of a team
     * @throws TeamJoinNotAllowedException if the team is not open to join or requires an invitation
     * @header token the token of the user who wants to join the team
     */

    Team getTeamByName(String teamName);


    Team joinTeam(Long teamId, String token)
            throws TeamNotFoundException, UserNotFoundException, TeamCapacityFullException, UserAlreadyInTeamException,
            TeamJoinNotAllowedException;


    // leaveTeam

    /**
     * Retrieves the team with the given teamId.
     *
     * @param teamId the id of the team to retrieve
     * @return the team with the given teamId
     * @throws TeamNotFoundException  if the team with the given teamId does not exist
     * @throws UserNotFoundException  if the user with the given userId does not exist
     * @throws UserNotInTeamException if the user with the given userId is not a member of the team
     * @header token the token of the user who wants to retrieve the team
     */

    void leaveTeam(String token)
            throws TeamNotFoundException, UserNotFoundException, UserNotInTeamException;

    /**
     * Retrieves up to ten random teams that have at least one empty spot for a new member to join.
     *
     * @return a list of up to ten random teams with at least one empty spot to join
     */
    List<GetTeams> getRandomJoinableTeams();


}
