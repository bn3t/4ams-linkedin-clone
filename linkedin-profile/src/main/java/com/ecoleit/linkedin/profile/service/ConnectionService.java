package com.ecoleit.linkedin.profile.service;

import com.ecoleit.linkedin.profile.entity.Profile;
import com.ecoleit.linkedin.profile.modele.ProfileDTO;
import com.ecoleit.linkedin.profile.modele.SkillDTO;
import com.ecoleit.linkedin.profile.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConnectionService {
    private final ProfileRepository profileRepository;

    public ConnectionService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public ProfileDTO convertToProfileDTO(Profile profile){
        return  new ProfileDTO(
                profile.getId(),
                profile.getUserId(),
                profile.getCurrentJobTitle(),
                profile.getIndustry(),
                profile.getSummary(),
                profile.getHeadline(),
                profile.getWebsite(),
                profile.getOpenForWork());
    }

    public List<ProfileDTO> getConnectionList(){
        return profileRepository
                .findAll()
                .stream()
                .map(this::convertToProfileDTO)
                .toList();
    }

    public List<ProfileDTO> getConnectionListByUserId(Integer userId){
        Profile profile = CommonService.getProfileByUserId(userId);
        return profile.getConnections()
                .stream()
                .map(this::convertToProfileDTO)
                .toList();
    }

    @Transactional
    public void addConnection(Integer userId, Integer otherUserId){
        Profile profile = CommonService.getProfileByUserId(userId);
        Profile otherProfile = CommonService.getProfileByUserId(otherUserId);
        profile.getConnections().add(otherProfile);
        profileRepository.save(profile);
    }

    @Transactional
    public  void removeConnection(Integer userId, Integer otherUserId){
        Profile profile = CommonService.getProfileByUserId(userId);
        Profile otherProfile = CommonService.getProfileByUserId(otherUserId);
        profile.getConnections().remove(otherProfile);
        profileRepository.save(profile);
    }
}
