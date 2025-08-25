package com.unirem.member_service;

import com.unirem.member_service.DTO.*;
import com.unirem.member_service.entity.GalleryImage;
import com.unirem.member_service.entity.News;
import com.unirem.member_service.entity.Project;
import com.unirem.member_service.repository.GalleryImageRepository;
import com.unirem.member_service.repository.NewsRepository;
import com.unirem.member_service.repository.ProjectRepository;
import com.unirem.member_service.repository.UserRepository;
import com.unirem.member_service.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
        "eureka.client.enabled=false"
})
public class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private GalleryImageRepository galleryImageRepository;

    @Mock
    private ModelMapper modelMapper;

    private Project project;
    private News news;
    private GalleryImage galleryImage;

    @BeforeEach
    void setup() {
        project = new Project();
        project.setProjectId(1L);
        project.setTittle("Software Architecture");
        project.setValid(false);
        project.setResearcherIds(new ArrayList<>());

        news = new News();
        news.setNewId(1L);
        news.setTittle("University Awards");
        news.setValid(false);

        galleryImage = new GalleryImage();
        galleryImage.setImageId(1L);
        galleryImage.setTittle("Project Meeting");
    }

    //Tests for projects
    @Test
    void createProject() {
        ProjectRequest request = new ProjectRequest();
        request.setTittle("Software Architecture");
        request.setLeaderId(1L);
        request.setResearchesIds(List.of(2L));
        request.setSlug("sw-arq");

        MockMultipartFile image = new MockMultipartFile("img", "test.png", "image/png", "img".getBytes());
        MockMultipartFile doc = new MockMultipartFile("doc", "test.pdf", "application/pdf", "pdf".getBytes());

        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(modelMapper.map(any(Project.class), eq(ProjectDTO.class)))
                .thenReturn(new ProjectDTO(1L, "Software Architecture", null, 1L, List.of(2L), null,
                        null, null, null, null, null, "sw-arq", false, null, null));

        ProjectDTO result = memberService.createProject(request, image, doc);

        assertNotNull(result);
        assertEquals("Software Architecture", result.getTittle());
        assertFalse(result.getValid());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void approveProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        memberService.approveProject(1L);
        assertTrue(project.getValid());
        verify(projectRepository).save(project);
    }

    @Test
    void getValidProjects() {
        project.setValid(true);
        when(projectRepository.findByValidTrue()).thenReturn(List.of(project));
        when(modelMapper.map(project, ProjectDTO.class)).thenReturn(new ProjectDTO());
        List<ProjectDTO> result = memberService.getValidProjects();
        assertEquals(1, result.size());
    }

    @Test
    void getAllProjects() {
        when(projectRepository.findAll()).thenReturn(List.of(project));
        when(modelMapper.map(project, ProjectDTO.class)).thenReturn(new ProjectDTO());
        List<ProjectDTO> result = memberService.getAllProjects();
        assertEquals(1, result.size());
    }

    @Test
    void editProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        ProjectRequest updated = new ProjectRequest();
        updated.setTittle("Updated Title");
        when(projectRepository.save(project)).thenReturn(project);
        when(modelMapper.map(project, ProjectDTO.class)).thenReturn(new ProjectDTO(1L, "Updated Title", null, null, null, null,
                null, null, null, null, null, null, false, null, null));
        ProjectDTO result = memberService.editProject(1L, updated, null, null);
        assertEquals("Updated Title", result.getTittle());
    }

    @Test
    void deleteProject() {
        memberService.deleteProject(1L);
        verify(projectRepository).deleteById(1L);
    }

    @Test
    void addUserToProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        memberService.addUserToProject(1L, 5L);
        assertTrue(project.getResearcherIds().contains(5L));
        verify(projectRepository).save(project);
    }

    //Tests for news
    @Test
    void createNews() {
        NewsRequest request = new NewsRequest();
        request.setTittle("University Awards");

        MockMultipartFile image = new MockMultipartFile("img", "news.png", "image/png", "img".getBytes());

        when(newsRepository.save(any(News.class))).thenReturn(news);
        when(modelMapper.map(any(News.class), eq(NewsDTO.class))).thenReturn(new NewsDTO());

        NewsDTO result = memberService.createNews(request, image);
        assertNotNull(result);
        verify(newsRepository).save(any(News.class));
    }

    @Test
    void approveNews() {
        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));
        memberService.approveNews(1L);
        assertTrue(news.getValid());
        verify(newsRepository).save(news);
    }

    @Test
    void getValidNews() {
        news.setValid(true);
        when(newsRepository.findByValidTrue()).thenReturn(List.of(news));
        when(modelMapper.map(news, NewsDTO.class)).thenReturn(new NewsDTO());
        List<NewsDTO> result = memberService.getValidNews();
        assertEquals(1, result.size());
    }

    @Test
    void getAllNews() {
        when(newsRepository.findAll()).thenReturn(List.of(news));
        when(modelMapper.map(news, NewsDTO.class)).thenReturn(new NewsDTO());
        List<NewsDTO> result = memberService.getAllNews();
        assertEquals(1, result.size());
    }

    @Test
    void editNews() {
        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));
        NewsRequest updated = new NewsRequest();
        updated.setTittle("Updated News");
        when(newsRepository.save(news)).thenReturn(news);
        when(modelMapper.map(news, NewsDTO.class)).thenReturn(new NewsDTO(1L, "Updated News", null, null, null, null,
                null, null, null, false));
        NewsDTO result = memberService.editNews(1L, updated, null);
        assertEquals("Updated News", result.getTittle());
    }

    @Test
    void deleteNews() {
        memberService.deleteNews(1L);
        verify(newsRepository).deleteById(1L);
    }

    //Tests for gallery
    @Test
    void createGalleryImage() {
        GalleryImageRequest request = new GalleryImageRequest();
        request.setTittle("Project Meeting");

        MockMultipartFile file = new MockMultipartFile("img", "gallery.png", "image/png", "img".getBytes());

        when(galleryImageRepository.save(any(GalleryImage.class))).thenReturn(galleryImage);
        when(modelMapper.map(any(GalleryImage.class), eq(GalleryImageDTO.class))).thenReturn(new GalleryImageDTO());

        GalleryImageDTO result = memberService.createGalleryImage(request, file);
        assertNotNull(result);
        verify(galleryImageRepository).save(any(GalleryImage.class));
    }

    @Test
    void getAllGalleryImages() {
        when(galleryImageRepository.findAll()).thenReturn(List.of(galleryImage));
        when(modelMapper.map(galleryImage, GalleryImageDTO.class)).thenReturn(new GalleryImageDTO());
        List<GalleryImageDTO> result = memberService.getAllGalleryImages();
        assertEquals(1, result.size());
    }

    @Test
    void editGalleryImage() {
        when(galleryImageRepository.findById(1L)).thenReturn(Optional.of(galleryImage));
        GalleryImageRequest updated = new GalleryImageRequest();
        updated.setTittle("Updated Image");
        when(galleryImageRepository.save(galleryImage)).thenReturn(galleryImage);
        when(modelMapper.map(galleryImage, GalleryImageDTO.class))
                .thenReturn(new GalleryImageDTO(1L, "Updated Image", null, null));
        GalleryImageDTO result = memberService.editGalleryImage(1L, updated);
        assertEquals("Updated Image", result.getTittle());
    }

    @Test
    void deleteGalleryImage() {
        memberService.deleteGalleryImage(1L);
        verify(galleryImageRepository).deleteById(1L);
    }
}
