package com.ecoleit.linkedin.profile.service;

import com.ecoleit.linkedin.profile.entity.Profile;
import com.ecoleit.linkedin.profile.modele.FullProfileDTO;
import com.ecoleit.linkedin.profile.modele.ProfileDTO;
import com.ecoleit.linkedin.profile.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ExperienceService experienceService;
    private final EducationService educationService;
    private final ConnectionService connectionService;
    private final SkillService skillService;

    public ProfileService(ProfileRepository profileRepository,
                          ExperienceService experienceService,
                          EducationService educationService,
                          ConnectionService connectionService,
                          SkillService skillService) {
        this.profileRepository = profileRepository;
        this.experienceService = experienceService;
        this.educationService = educationService;
        this.connectionService = connectionService;
        this.skillService = skillService;
    }

    private FullProfileDTO convertToFullProfileDTO(Profile fullProfile){
        return  new FullProfileDTO(
                fullProfile.getId(),
                fullProfile.getUserId(),
                fullProfile.getCurrentJobTitle(),
                fullProfile.getIndustry(),
                fullProfile.getSummary(),
                fullProfile.getHeadline(),
                fullProfile.getWebsite(),
                fullProfile.getOpenForWork(),
                fullProfile.getSkills()
                        .stream()
                        .map(skillService::convertToSkillDTO)
                        .toList(),
                fullProfile.getConnections()
                        .stream()
                        .map(connectionService::convertToProfileDTO)
                        .toList(),
                fullProfile.getEducations()
                        .stream()
                        .map(educationService::convertToEducationDTO)
                        .toList(),
                fullProfile.getExperiences()
                        .stream()
                        .map(experienceService::convertToExperienceDTO)
                        .toList());
    }

    public FullProfileDTO getFullProfileByUserId(Integer userId){
        return profileRepository
                .findByUserId(userId)
                .map(this::convertToFullProfileDTO)
                .orElseThrow(()->
                        new IllegalArgumentException("Profile not found"));
    }
    public List<FullProfileDTO> getProfileList(){
        return profileRepository
                .findAll()
                .stream()
                .map(this::convertToFullProfileDTO)
                .toList();
    }

    public FullProfileDTO updateProfile(Integer userId,ProfileDTO profileDTO){
        Profile profile = CommonService.getProfileByUserId(userId);
        profile.setCurrentJobTitle(profileDTO.currentJobTitle());
        profile.setIndustry(profileDTO.industry());
        profile.setSummary(profileDTO.summary());
        profile.setHeadline(profileDTO.headline());
        profile.setWebsite(profileDTO.website());
        profile.setOpenForWork(profileDTO.openForWork());
        profileRepository.save(profile);
        return convertToFullProfileDTO(profile);
    }

    public FullProfileDTO createProfile( ProfileDTO profileDTO) {
        Profile newProfile = new Profile();
        newProfile.setUserId(profileDTO.userId());
        newProfile.setCurrentJobTitle(profileDTO.currentJobTitle());
        newProfile.setIndustry(profileDTO.industry());
        newProfile.setSummary(profileDTO.summary());
        newProfile.setHeadline(profileDTO.headline());
        newProfile.setWebsite(profileDTO.website());
        newProfile.setOpenForWork(profileDTO.openForWork());

        Profile savedProfile = profileRepository.save(newProfile);

        return convertToFullProfileDTO(savedProfile);
    }
}
