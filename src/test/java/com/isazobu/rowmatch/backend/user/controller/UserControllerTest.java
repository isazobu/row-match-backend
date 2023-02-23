package com.isazobu.rowmatch.backend.user.controller;

import com.isazobu.rowmatch.backend.user.dto.CreateUserRequest;
import com.isazobu.rowmatch.backend.user.dto.UpdateLevelRequest;
import com.isazobu.rowmatch.backend.user.model.User;
import com.isazobu.rowmatch.backend.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void createUser_returnsCreatedUser() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setName("John");

        User createdUser = new User();
        createdUser.setName(request.getName());

        when(userService.createUser(request)).thenReturn(createdUser);

        // Act
        ResponseEntity<User> response = userController.createUser(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdUser, response.getBody());
    }


    @Test
    public void updateLevel_withValidToken_returnsUpdatedUser() {
        // Arrange
        String token = "validToken";

        UpdateLevelRequest user = new UpdateLevelRequest();
        user.setId(1L);
        user.setName("John");
        user.setLevel(1);
        user.setCoins(5000);



        when(userService.updateLevel(token)).thenReturn(user);

        // Act
        ResponseEntity<UpdateLevelRequest> response = userController.updateLevel(token);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void updateLevel_withEmptyToken_returnsUnauthorized() {
        // Arrange
        String token = "";

        // Act
        ResponseEntity<User> response = userController.updateLevel(token);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void updateLevel_withInvalidToken_returnsUnauthorized() {
        // Arrange
        String token = "invalidToken";

        when(userService.verifyToken(token)).thenReturn(null);

        // Act
        ResponseEntity<User> response = userController.updateLevel(token);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}