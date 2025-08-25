package com.unirem.projects_service.controller;

import com.unirem.projects_service.DTO.ProjectDTO;
import com.unirem.projects_service.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping("/get-all-projects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/get-valid-projects")
    public ResponseEntity<List<ProjectDTO>> getValidProjects() {
        return ResponseEntity.ok(projectService.getValidProjects());
    }

    @GetMapping("/get-project-by-id")
    public ResponseEntity<ProjectDTO> getById(@RequestParam Long projectId) {
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }
}
