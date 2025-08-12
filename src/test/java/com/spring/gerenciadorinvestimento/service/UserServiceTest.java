package com.spring.gerenciadorinvestimento.service;

import com.spring.gerenciadorinvestimento.controller.dto.CreateUserDto;
import com.spring.gerenciadorinvestimento.controller.dto.UpdateUserDto;
import com.spring.gerenciadorinvestimento.entity.User;
import com.spring.gerenciadorinvestimento.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested
    class createUser {
        @Test
        //(1)Arrange - (2)Act - (3)Assert
        @DisplayName("User is expected to be created with success")
        void expectedCreateUser(){
        //(1)
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null
            );

            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
            var input = new CreateUserDto(
                    "username",
                    "email@email.com",
                    "123456"
            );
        //(2)
            var output = userService.createUser(input);
        //(3)
            assertNotNull(output);

            var userCaptured = userArgumentCaptor.getValue();
            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());
        }

        @Test
        //(1)Arrange - (2)Act - (3)Assert
        @DisplayName("Exception is expected when error occurs")
        void expectedThrowsExceptionWhenErrorOccurs(){
        //(1)
            var specificException = new RuntimeException("Database connection failed");
            doThrow(specificException).when(userRepository).save(userArgumentCaptor.capture());

            var input = new CreateUserDto(
                    "username",
                    "email@email.com",
                    "123456"
            );
        //(2)
            var thrownException = assertThrows(RuntimeException.class, () -> userService.createUser(input));
        //(3)
            assertEquals("Database connection failed", thrownException.getMessage());

            var userCaptured = userArgumentCaptor.getValue();
            assertNotNull(userCaptured);
            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());
            assertNotNull(userCaptured.getCreationTimestamp());
            assertNull(userCaptured.getUpdateTimestamp());
        }

    }

    @Nested
    class getUserById {
        @Test
        //(1)Arrange - (2)Act - (3)Assert
        @DisplayName("Expected to get User by Id with success when Optional is present")
        void expectedGetUserByIdOptionalPresent() {
        //(1)
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null
            );
            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());
        //(2)
            var output = userService.getUserById(user.getUserId().toString());
        //(3)
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        //(1)Arrange - (2)Act - (3)Assert
        @DisplayName("Expected to get User by Id with success when Optional is empty")
        void expectedGetUserByIdEmpty() {
        //(1)
            var userId = UUID.randomUUID();
            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());
        //(2)
            var output = userService.getUserById(userId.toString());
        //(3)
            assertTrue(output.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }
    }

    @Nested
    class listUsers {
        @Test
        //(1)Arrange - (2)Act - (3)Assert
        @DisplayName("Expected to return all users with success")
        void expectedListUsers() {
        //(1)
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null
            );
            var userList = List.of(user);
            doReturn(userList).when(userRepository).findAll();
        //(2)
            var output = userService.listAllUsers();
        //(3)
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }

        @Test
        //(1)Arrange - (2)Act - (3)Assert
        @DisplayName("Expected to return empty list when no users exist")
        void expectedEmptyListWhenNoUsers() {
            doReturn(List.of()).when(userRepository).findAll();
        //(2)
            var output = userService.listAllUsers();
        //(3)
            assertNotNull(output);
            assertTrue(output.isEmpty());
        }
    }

    @Nested
    class updateUserById{
        @Test
        //(1)Arrange - (2)Act - (3)Assert
        @DisplayName("Expected to update User by Id with success when user exist and Username/Password is filled")
        void expectedUpdateUserByIdWhenExistAndFilled() {
            //(1)
            var updateUserDto = new UpdateUserDto(
                    "newUsername",
                    "newPassword"
            );

            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null
            );

            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
            //(2)
            userService.updateUserById(user.getUserId().toString(), updateUserDto);
            //(3)
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
            var userCaptured = userArgumentCaptor.getValue();
            assertEquals(updateUserDto.username(), userCaptured.getUsername());
            assertEquals(updateUserDto.password(), userCaptured.getPassword());

            verify(userRepository, times(1)).findById(uuidArgumentCaptor.capture());
            verify(userRepository, times(1)).save(user);
        }

        @Test
        //(1)Arrange - (2)Act - (3)Assert
        @DisplayName("Expected to NOT update User by Id with success when user NOT exist")
        void expectedNotUpdateUserByIdWhenUserNotExist() {
            //(1)
            var updateUserDto = new UpdateUserDto(
                    "newUsername",
                    "newPassword"
            );

            var userId = UUID.randomUUID();

            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());
            //(2)
            userService.updateUserById(userId.toString(), updateUserDto);
            //(3)
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1)).findById(uuidArgumentCaptor.capture());
            verify(userRepository, times(0)).save(any());
        }
    }

    @Nested
    class deleteById {
        @Test
        //(1)Arrange - (2)Act - (3)Assert
        @DisplayName("Expected to delete user with success when user exist")
        void expectedDeleteUserWhenUserExist() {
        //(1)
            doReturn(true).when(userRepository).existsById(uuidArgumentCaptor.capture());
            doNothing().when(userRepository).deleteById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();
        //(2)
            userService.deleteUserById(userId.toString());
        //(3)
            var idList = uuidArgumentCaptor.getAllValues();
            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));
            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));
        }

        @Test
        //(1)Arrange - (2)Act - (3)Assert
        @DisplayName("Expected to not delete user when user DON'T exist")
        void expectedNotDeleteUserWhenUserDontExist() {
        //(1)
            doReturn(false).when(userRepository).existsById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();
        //(2)
            userService.deleteUserById(userId.toString());
        //(3)
            assertEquals(userId, uuidArgumentCaptor.getValue());
            verify(userRepository, times(1)).existsById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).deleteById(any());
        }
    }
}