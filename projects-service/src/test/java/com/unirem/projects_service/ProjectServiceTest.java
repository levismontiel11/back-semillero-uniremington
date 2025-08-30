package com.unirem.projects_service;

import com.unirem.projects_service.DTO.ProjectDTO;
import com.unirem.projects_service.entity.Project;
import com.unirem.projects_service.repository.ProjectRepository;
import com.unirem.projects_service.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ProjectServiceTest {
    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ModelMapper modelMapper;

    private Project project;
    private ProjectDTO projectDTO;

    @BeforeEach
    void setup() {
        project = new Project();
        project.setProjectId(1L);
        project.setTittle("Software Architecture");
        project.setValid(false);
        project.setResearcherIds(new ArrayList<>());

        projectDTO = new ProjectDTO();
        projectDTO.setProjectId(1L);
        projectDTO.setTittle("Software Architecture");
        projectDTO.setValid(false);
        projectDTO.setResearcherIds(new ArrayList<>());
    }

    @Test
    void getValidNews() {
        project.setValid(true);
        when(projectRepository.findByValidTrue()).thenReturn(List.of(project));
        when(modelMapper.map(project, ProjectDTO.class)).thenReturn(new ProjectDTO());
        List<ProjectDTO> result = projectService.getValidProjects();
        assertEquals(1, result.size());
    }

    @Test
    void getAllNews() {
        when(projectRepository.findAll()).thenReturn(List.of(project));
        when(modelMapper.map(project, ProjectDTO.class)).thenReturn(new ProjectDTO());
        List<ProjectDTO> result = projectService.getAllProjects();
        assertEquals(1, result.size());
    }

    @Test
    void successfulGetProjectById() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(modelMapper.map(project, ProjectDTO.class)).thenReturn(projectDTO);

        ProjectDTO result = projectService.getProjectById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getProjectId());
        assertEquals("Software Architecture", result.getTittle());

        verify(projectRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(project, ProjectDTO.class);
    }

    @Test
    void wrongGetProjectById() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> projectService.getProjectById(1L));

        assertEquals("Project not found", ex.getMessage());
        verify(projectRepository, times(1)).findById(1L);
        verifyNoInteractions(modelMapper);
    }
}
