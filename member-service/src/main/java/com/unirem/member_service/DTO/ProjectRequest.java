package com.unirem.member_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class    ProjectRequest {
    private String tittle;
    private String description;
    private Long leaderId;
    private List<Long> researcherIds;
    private String status;
    private String creationDate;
    private String endDate;
    private String researchArea;
    private String researchTopic;
    private String identifierArea;
    private String slug;
    private MultipartFile image;
    private MultipartFile document;
}
