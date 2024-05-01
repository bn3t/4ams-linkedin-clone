package com.ecoleit.linkedin.bff.controller;

import com.ecoleit.linkedin.bff.config.ConfigProperties;
import com.ecoleit.linkedin.bff.model.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

record ProfileApiDTO(Integer id, Integer userId, String currentJobTitle,
                     String industry, String summary, String headline, String website,
                     Integer openForWork){}
record FullProfileApiDTO(Integer id, Integer userId, String currentJobTitle,
                      String industry, String summary, String headline, String website,
                      Integer openForWork, List<SkillDTO> skills, List<ProfileDTO> connections,
                      List<EducationDTO> educations, List<ExperienceDTO> experiences) {}
//http://localhost:8080/api

@RestController
@RequestMapping("/api")
public class ProfileBffController {
    private final RestClient restClient;
    private final ConfigProperties configProperties;


    public ProfileBffController(RestClient restClient, ConfigProperties configProperties) {
        this.restClient = restClient;
        this.configProperties = configProperties;
    }

    @GetMapping("/profiles/{userId}")
    public FullProfileApiDTO getProfileByUserId(@PathVariable Integer userId) {
        try{
            FullProfileApiDTO fullProfileApiDTO = restClient
                    .get()
                    .uri("http://"
                            + configProperties
                            .getProfileHost()
                            + ":8082/profiles/{userId}", userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(FullProfileApiDTO.class);
            if (fullProfileApiDTO != null) {
                return new FullProfileApiDTO(
                        fullProfileApiDTO.id(),
                        fullProfileApiDTO.userId(),
                        fullProfileApiDTO.currentJobTitle(),
                        fullProfileApiDTO.industry(),
                        fullProfileApiDTO.summary(),
                        fullProfileApiDTO.headline(),
                        fullProfileApiDTO.website(),
                        fullProfileApiDTO.openForWork(),
                        fullProfileApiDTO.skills(),
                        fullProfileApiDTO.connections(),
                        fullProfileApiDTO.educations(),
                        fullProfileApiDTO.experiences());
            } else {
                return null;
            }
        }catch (Exception exception){
            throw new IllegalArgumentException(exception.getMessage());
        }
    }

    @PostMapping("/profiles")
    public FullProfileApiDTO createProfile(@RequestBody ProfileApiDTO profileApiDTO) {
        try{
            ProfileApiDTO profile = restClient
                    .post()
                    .uri("http://"
                            + configProperties
                            .getProfileHost()
                            + ":8082/profiles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(profileApiDTO)
                    .retrieve()
                    .body(ProfileApiDTO.class);

            if (profile != null) {
                return new FullProfileApiDTO(
                        profile.id(),
                        profile.userId(),
                        profile.currentJobTitle(),
                        profile.industry(),
                        profile.summary(),
                        profile.headline(),
                        profile.website(),
                        profile.openForWork(),
                        null,
                        null,
                        null,
                        null);
            } else {
                return null;
            }
        }catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @PutMapping("/profiles/{userId}")
    public FullProfileApiDTO updateProfile(@RequestBody ProfileApiDTO profileApiDTO, @PathVariable Integer userId) {
        try{
            FullProfileApiDTO fullProfileApiDTO = restClient
                    .put()
                    .uri("http://"
                            + configProperties
                            .getProfileHost()
                            + ":8082/profiles/{userId}", userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(profileApiDTO)
                    .retrieve()
                    .body(FullProfileApiDTO.class);

            if (fullProfileApiDTO != null) {
                return new FullProfileApiDTO(
                        fullProfileApiDTO.id(),
                        fullProfileApiDTO.userId(),
                        fullProfileApiDTO.currentJobTitle(),
                        fullProfileApiDTO.industry(),
                        fullProfileApiDTO.summary(),
                        fullProfileApiDTO.headline(),
                        fullProfileApiDTO.website(),
                        fullProfileApiDTO.openForWork(),
                        fullProfileApiDTO.skills(),
                        fullProfileApiDTO.connections(),
                        fullProfileApiDTO.educations(),
                        fullProfileApiDTO.experiences());
            } else {
                return null;
            }
        }catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}
