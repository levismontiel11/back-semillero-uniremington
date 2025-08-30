package com.unirem.member_service.service;

import com.unirem.member_service.DTO.*;
import com.unirem.member_service.entity.GalleryImage;
import com.unirem.member_service.entity.News;
import com.unirem.member_service.entity.Project;
import com.unirem.member_service.entity.User;
import com.unirem.member_service.repository.GalleryImageRepository;
import com.unirem.member_service.repository.NewsRepository;
import com.unirem.member_service.repository.ProjectRepository;
import com.unirem.member_service.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class MemberService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRespository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private GalleryImageRepository galleryImageRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileStorageService fileStorageService;

    private static final String PROJECT_IMAGE_DIR = "projects/images";
    private static final String PROJECT_DOC_DIR   = "projects/docs";
    private static final String NEWS_IMAGE_DIR = "news/images";


    public ProjectDTO createProject(ProjectRequest request, MultipartFile image, MultipartFile document) {
        Project project = new Project();

        project.setTittle(request.getTittle());
        project.setDescription(request.getDescription());

        // solo guardamos IDs
        project.setLeaderId(request.getLeaderId());
        project.setResearcherIds(request.getResearcherIds());


        project.setStatus(request.getStatus());
        project.setCreationDate(request.getCreationDate());
        project.setEndDate(request.getEndDate());
        project.setResearchArea(request.getResearchArea());
        project.setResearchTopic(request.getResearchTopic());
        project.setIdentifierArea(request.getIdentifierArea());
        project.setSlug(request.getSlug());
        project.setValid(false);


        if (image != null && !image.isEmpty()) {
            project.setImageUrl(fileStorageService.saveFile(image, PROJECT_IMAGE_DIR));
        }

        if (document != null && !document.isEmpty()) {
            project.setDocumentUrl(fileStorageService.saveFile(document, PROJECT_DOC_DIR));
        }

        try {
            project = projectRepository.save(project);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving project: " + e.getMessage());
        }

        return projectToProjectDTO(project);
    }

    public void addUserToProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getResearcherIds().contains(userId)) {
            project.getResearcherIds().add(userId);
            projectRepository.save(project);
        }
    }

    public void approveProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        project.setValid(true);

        projectRepository.save(project);
    }

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

    public ProjectDTO editProject(Long projectId, ProjectRequest request, MultipartFile image,
                                  MultipartFile document) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        project.setTittle(request.getTittle());
        project.setDescription(request.getDescription());

        // solo guardamos IDs
        project.setLeaderId(request.getLeaderId());
        project.getResearcherIds().addAll(request.getResearcherIds());

        project.setStatus(request.getStatus());
        project.setCreationDate(request.getCreationDate());
        project.setEndDate(request.getEndDate());
        project.setResearchArea(request.getResearchArea());
        project.setResearchTopic(request.getResearchTopic());
        project.setIdentifierArea(request.getIdentifierArea());
        project.setSlug(request.getSlug());

        if (image != null && !image.isEmpty()) {
            project.setImageUrl(fileStorageService.saveFile(image, PROJECT_IMAGE_DIR));
        }

        if (document != null && !document.isEmpty()) {
            project.setDocumentUrl(fileStorageService.saveFile(document, PROJECT_DOC_DIR));
        }

        project = projectRepository.save(project);

        return projectToProjectDTO(project);
    }

    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    public NewsDTO createNews(NewsRequest newsRequest, MultipartFile image) {
        News news = new News();

        news.setTittle(newsRequest.getTittle());
        news.setExcerpt(newsRequest.getExcerpt());
        news.setContent(newsRequest.getContent());
        news.setCategory(newsRequest.getCategory());
        news.setDate(newsRequest.getDate());
        news.setAuthorId(newsRequest.getAuthorId());
        news.setSlug(newsRequest.getSlug());
        news.setValid(false);

        if (image != null && !image.isEmpty()) {
            news.setImageUrl(fileStorageService.saveFile(image, NEWS_IMAGE_DIR));
        }

        news = newsRepository.save(news);

        return newsToNewsDTO(news);
    }

    public void approveNews(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));

        news.setValid(true);

        newsRepository.save(news);
    }

    public List<NewsDTO> getAllNews() {
        return newsRepository.findAll().stream()
                .map(this::newsToNewsDTO)
                .toList();
    }

    public List<NewsDTO> getValidNews() {
        return newsRepository.findByValidTrue().stream()
                .map(this::newsToNewsDTO)
                .toList();
    }

    public NewsDTO editNews(Long newsId, NewsRequest request, MultipartFile image) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));

        news.setTittle(request.getTittle());
        news.setExcerpt(request.getExcerpt());
        news.setContent(request.getContent());
        news.setCategory(request.getCategory());
        news.setDate(request.getDate());
        news.setAuthorId(request.getAuthorId());
        news.setSlug(request.getSlug());

        if (image != null && !image.isEmpty()) {
            news.setImageUrl(fileStorageService.saveFile(request.getImage(), NEWS_IMAGE_DIR));
        }

        news = newsRepository.save(news);

        return newsToNewsDTO(news);
    }

    public void deleteNews(Long newsId) {
        newsRepository.deleteById(newsId);
    }

    public GalleryImageDTO createGalleryImage(GalleryImageRequest galleryImageRequest, MultipartFile image) {
        GalleryImage galleryImage = new GalleryImage();

        galleryImage.setTittle(galleryImageRequest.getTittle());
        galleryImage.setDescription(galleryImageRequest.getDescription());

        if (image != null && !image.isEmpty()) {
            galleryImage.setImageUrl(fileStorageService.saveFile(galleryImageRequest.getImage(), ""));
        }

        galleryImage = galleryImageRepository.save(galleryImage);

        return galleryImageToGalleryImageDTO(galleryImage);
    }

    public List<GalleryImageDTO> getAllGalleryImages() {
        return galleryImageRepository.findAll().stream()
                .map(this::galleryImageToGalleryImageDTO)
                .toList();
    }

    public GalleryImageDTO editGalleryImage(Long galleryImageId, GalleryImageRequest request) {
        GalleryImage galleryImage = galleryImageRepository.findById(galleryImageId)
                .orElseThrow(() -> new RuntimeException("Gallery image not found"));

        galleryImage.setTittle(request.getTittle());
        galleryImage.setDescription(request.getDescription());

        galleryImage = galleryImageRepository.save(galleryImage);

        return galleryImageToGalleryImageDTO(galleryImage);
    }

    public void deleteGalleryImage(Long galleryImageId) {
        galleryImageRepository.deleteById(galleryImageId);
    }

    public List<UserDTO> getAllUsers() {
        return userRespository.findAll().stream()
                .map(this::userToUserDTO)
                .toList();
    }

    public void deleteUser(Long userId) {
        userRespository.deleteById(userId);
    }

    private ProjectDTO projectToProjectDTO(Project project) {
        return modelMapper.map(project, ProjectDTO.class);
    }

    private NewsDTO newsToNewsDTO(News news) {
        return modelMapper.map(news, NewsDTO.class);
    }

    private GalleryImageDTO galleryImageToGalleryImageDTO(GalleryImage galleryImage) {
        return modelMapper.map(galleryImage, GalleryImageDTO.class);
    }

    private UserDTO userToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }


}
