package com.isazobu.rowmatch.backend.user.service;

import com.isazobu.rowmatch.backend.exceptions.EntityAlreadyExistsException;
import com.isazobu.rowmatch.backend.user.dto.CreateUserRequest;
import com.isazobu.rowmatch.backend.user.model.User;
import com.isazobu.rowmatch.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jdk.jfr.Description;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("user1");
    }

    @Test
    @Description("Test create user success")
    public void testCreateUser_Success() throws NoSuchAlgorithmException, NoSuchProviderException {
        // arrange
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName("user1");
        when(userRepository.findByName(createUserRequest.getName())).thenReturn(Optional.empty());
        // act
        User user = userService.createUser(createUserRequest);

        // assert
        assertEquals(createUserRequest.getName(), user.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    @Description("Test create user already exists")
    public void testCreateUser_AlreadyExists() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName("user1");
        // Arrange
        User existingUser = new User(1L, "user1", 1, 5000, null, null);
        when(userRepository.findByName(createUserRequest.getName())).thenReturn(Optional.of(existingUser));

        // Act
        userService.createUser(createUserRequest);

        // Assert
        verify(userRepository, never()).save(any());
    }


//        @Test
//        @Description("Test create user with token creation error")
//        public void createUser_tokenCreationError() throws Exception {
//            CreateUserRequest createUserRequest = new CreateUserRequest();
//            createUserRequest.setName("user1");
//
//            // mock objects
//            UserService userServiceMock = mock(UserService.class);
//            UserRepository userRepositoryMock = mock(UserRepository.class);
//
//            // configure mock objects
//            when(userRepositoryMock.findByName(createUserRequest.getName())).thenReturn(Optional.empty());
//            doThrow(new NoSuchAlgorithmException()).when(userServiceMock.createHashWithSalt(anyString()));
//
//            // create user using mock objects
//            User user = userServiceMock.createUser(createUserRequest);
//
//            // Assert
//            assertNull(user);
//        }


    @Test
    public void updateLevel_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setLevel(1);
        user.setCoins(5000);

        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateLevel(user);

        assertNotNull(updatedUser);
        assertEquals(user.getId(), updatedUser.getId());
        assertEquals(user.getName(), updatedUser.getName());
        assertEquals(Integer.valueOf(2), updatedUser.getLevel());
        assertEquals(Integer.valueOf(5000 + 25), updatedUser.getCoins());
    }

    @Test
    public void verifyToken_success() {
        String token = "0a46b01722c63520fd88d7972a147ae9";

        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setLevel(1);
        user.setCoins(5000);
        user.setToken(token);

        Mockito.when(userRepository.findByToken(token)).thenReturn(Optional.of(user));

        User result = userService.verifyToken(token);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getLevel(), result.getLevel());
        assertEquals(user.getCoins(), result.getCoins());
        assertEquals(user.getToken(), result.getToken());
    }

    @Test
    public void verifyToken_userNotFound() {
        String token = "0a46b01722c63520fd88d7972a147ae9";

        when(userRepository.findByToken(token)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.verifyToken(token);
        });
    }

    @Test
    public void testCreateHashWithSalt() throws NoSuchAlgorithmException {
        String input = "test";
        String output = userService.createHashWithSalt(input);

        assertNotNull(output);
        assertEquals(64, output.length());
    }

    @Test
    public void testCreateHashWithSalt_DifferentInputProducesDifferentOutput() throws NoSuchAlgorithmException {
        String input1 = "test1";
        String input2 = "test2";
        String output1 = userService.createHashWithSalt(input1);
        String output2 = userService.createHashWithSalt(input2);

        assertNotEquals(output1, output2);
    }

//    @Test
//    public void testCreateHashWithSalt_NoSuchAlgorithmException() throws NoSuchAlgorithmException, NoSuchProviderException {
//        String input = "test";
//        UserService userServiceSpy = spy(userService);
//
//        doThrow(NoSuchAlgorithmException.class).when(userServiceSpy).createSalt();
//        assertThrows(RuntimeException.class, () -> {
//            userServiceSpy.createHashWithSalt(input);
//        });
//    }

    @Test
    public void testCreateSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
        byte[] salt = userService.createSalt();

        assertNotNull(salt);
        assertEquals(16, salt.length);
    }

    @Test
    public void testCreateSalt_DifferentInvocationsProduceDifferentOutput() throws NoSuchAlgorithmException, NoSuchProviderException {
        byte[] salt1 = userService.createSalt();
        byte[] salt2 = userService.createSalt();

        assertNotEquals(salt1, salt2);
    }

    @Test
    public void testVerifyToken_Success() {
        String token = "token";
        User mockUser = new User(1L, "John", 1, 5000, "hash");
        when(userRepository.findByToken(token)).thenReturn(Optional.of(mockUser));

        User result = userService.verifyToken(token);

        assertEquals(mockUser, result);
        verify(userRepository, times(1)).findByToken(token);
    }

    @Test
    public void testVerifyToken_EntityNotFoundException() {
        String token = "token";
        when(userRepository.findByToken(token)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.verifyToken(token);
        });

        verify(userRepository, times(1)).findByToken(token);
    }

}
