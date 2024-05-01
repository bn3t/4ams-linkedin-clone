package com.ecoleit.linkedin.bff.controller;

import com.ecoleit.linkedin.bff.config.ConfigProperties;
import com.ecoleit.linkedin.bff.model.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.sql.Date;
import java.util.List;


record UserApiDTO(Integer id, String username, String firstName, String lastName,
                  String password, String email, Date birthDate,
                  LocalDateTime registrationDate, Timestamp lastLoginDate) {
}


//http://localhost:8080/api

@RestController
@RequestMapping("/api")
public class UserBffController {
    private final RestClient restClient;
    private final ConfigProperties configProperties;

    public UserBffController(RestClient restClient, ConfigProperties configProperties) {
        this.restClient = restClient;
        this.configProperties = configProperties;
    }

    @GetMapping("/users/{userId}")
    public UserDTO getUserById(@PathVariable Integer userId) {
        try{
            UserApiDTO user = restClient
                    .get()
                    .uri("http://"
                            + configProperties
                            .getUserHost()
                            + ":8081/users/{userId}", userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(UserApiDTO.class);
            if (user != null) {
                return new UserDTO(
                        user.id(),
                        user.username(),
                        user.firstName(),
                        user.lastName(),
                        user.email(),
                        user.birthDate(),
                        user.registrationDate(),
                        user.lastLoginDate()
                );
            } else {
                return null;
            }
        }catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }


    @GetMapping("/search-users/{username}")
    public List<UserDTO> getUserList(@PathVariable String username) {
        UserApiDTO[] users = restClient
                .get()
                .uri("http://"
                        + configProperties.getUserHost() +
                        ":8081/users/search/{username}", username)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(UserApiDTO[].class);
        assert users != null;
        return Arrays.stream(users)
                .map(user ->
                        new UserDTO(
                                user.id(),
                                user.username(),
                                user.firstName(),
                                user.lastName(),
                                user.email(),
                                user.birthDate(),
                                user.registrationDate(),
                                user.lastLoginDate()))
                .toList();
    }

    @PostMapping("/users/register")
    public UserDTO createNewUser(@RequestBody UserApiDTO userApiDTO) {
        try{
            UserApiDTO newUser = restClient
                    .post()
                    .uri("http://"
                            + configProperties
                            .getUserHost()
                            + ":8081/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userApiDTO)
                    .retrieve()
                    .body(UserApiDTO.class);

            if (newUser != null) {
                return new UserDTO(
                        newUser.id(),
                        newUser.username(),
                        newUser.firstName(),
                        newUser.lastName(),
                        newUser.email(),
                        newUser.birthDate(),
                        newUser.registrationDate(),
                        newUser.lastLoginDate()
                );
            } else {
                return null;
            }
        }catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @PutMapping("/users/{userId}")
    public UserDTO updateUser(@RequestBody UserApiDTO userApiDTO, @PathVariable Integer userId) {
        try{
            UserApiDTO newUser = restClient
                    .put()
                    .uri("http://"
                            + configProperties
                            .getUserHost()
                            + ":8081/users/{userId}", userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userApiDTO)
                    .retrieve()
                    .body(UserApiDTO.class);

            if (newUser != null) {
                return new UserDTO(
                        newUser.id(),
                        newUser.username(),
                        newUser.firstName(),
                        newUser.lastName(),
                        newUser.email(),
                        newUser.birthDate(),
                        newUser.registrationDate(),
                        newUser.lastLoginDate()
                );
            } else {
                return null;
            }
        }catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}




