package com.ecoleit.linkedin.bff.model;

import java.util.List;

public record FullProfileDTO(Integer id, String username, String email, String currentJobTitle,
                             String industry, String summary, String headline, String website,
                             Integer openForWork, List<SkillDTO> skills, List<ProfileDTO> connections,
                             List<EducationDTO> educations, List<ExperienceDTO> experiences) {
}
