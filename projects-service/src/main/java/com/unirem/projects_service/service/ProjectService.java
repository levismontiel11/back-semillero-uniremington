package com.unirem.projects_service.service;

import com.unirem.projects_service.DTO.ProjectDTO;
import com.unirem.projects_service.entity.Project;
import com.unirem.projects_service.repository.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::projectToProjectDTO)
                .toList();
    }

    public List<ProjectDTO> getValidProjects() {
        return projectRepository.findByValidTrue().stream()
                .map(this::projectToProjectDTO)
                .toList();
    }

    public ProjectDTO getProjectById(Long projectId) {
        return projectToProjectDTO(projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found")));
    }

    private ProjectDTO projectToProjectDTO(Project project) {
        return modelMapper.map(project, ProjectDTO.class);
    }
}
