package com.unirem.member_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(nullable = false)
    private String tittle;

    @Column(nullable = false)
    private String description;

    @Column(name = "leader_id", nullable = false)
    private Long leaderId;

    @ElementCollection
    @CollectionTable(
            name = "project_researchers",
            joinColumns = @JoinColumn(name = "project_id")
    )
    @Column(name = "researcher_id")
    private List<Long> researcherIds = new ArrayList<>();

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String creationDate;

    @Column(nullable = false)
    private String endDate;

    @Column(nullable = false)
    private String researchArea;

    @Column(nullable = false)
    private String researchTopic;

    @Column(nullable = false)
    private String identifierArea;

    private String slug;

    @Column(nullable = false)
    private Boolean valid;

    private String imageUrl;
    private String documentUrl;
}

