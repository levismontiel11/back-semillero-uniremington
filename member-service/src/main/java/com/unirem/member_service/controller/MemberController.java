package com.unirem.member_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unirem.member_service.DTO.*;
import com.unirem.member_service.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping(value = "/create-project", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectDTO> createProject(
            @RequestParam("data") String data,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "document", required = false) MultipartFile document) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        ProjectRequest request = mapper.readValue(data, ProjectRequest.class);

        ProjectDTO project = memberService.createProject(request, image, document);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/add-user-to-project")
    public ResponseEntity<?> addUserToProject(@RequestParam Long projectId, @RequestBody Long userId) {
        memberService.addUserToProject(projectId, userId);
        return ResponseEntity.ok("User added");
    }

    @PutMapping("/approve-project")
    public ResponseEntity<?> approveProject(@RequestParam Long projectId) {
        memberService.approveProject(projectId);
        return ResponseEntity.ok("Project approved");
    }

    @GetMapping("/get-all-projects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        return ResponseEntity.ok(memberService.getAllProjects());
    }

    @GetMapping("/get-valid-projects")
    public ResponseEntity<List<ProjectDTO>> getValidProjects() {
        return ResponseEntity.ok(memberService.getValidProjects());
    }

    @PutMapping(value = "/edit-project", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectDTO> editProject(
            @RequestParam Long projectId,
            @RequestParam("data") String data,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "document", required = false) MultipartFile document) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        ProjectRequest request = mapper.readValue(data, ProjectRequest.class);

        ProjectDTO project = memberService.editProject(projectId, request, image, document);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/delete-project")
    public ResponseEntity<?> deleteProject(@RequestParam Long projectId) {
        memberService.deleteProject(projectId);
        return ResponseEntity.ok("Project deleted");
    }

    @PostMapping(value = "/create-news", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NewsDTO> createNews(
            @RequestParam("data") String data,
            @RequestPart(value = "image", required = false) MultipartFile image) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        NewsRequest request = mapper.readValue(data, NewsRequest.class);

        NewsDTO news = memberService.createNews(request, image);
        return ResponseEntity.ok(news);
    }

    @PutMapping("/approve-news")
    public ResponseEntity<?> approveNews(@RequestParam Long newsId) {
        memberService.approveNews(newsId);
        return ResponseEntity.ok("News approved");
    }

    @GetMapping("/get-all-news")
    public ResponseEntity<List<NewsDTO>> getAllNews() {
        return ResponseEntity.ok(memberService.getAllNews());
    }

    @GetMapping("/get-valid-news")
    public ResponseEntity<List<NewsDTO>> getValidNews() {
        return ResponseEntity.ok(memberService.getValidNews());
    }

    @PutMapping(value = "/edit-news", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NewsDTO> editNews(
            @RequestParam Long newsId,
            @RequestParam("data") String data,
            @RequestPart(value = "image", required = false) MultipartFile image) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        NewsRequest request = mapper.readValue(data, NewsRequest.class);

        NewsDTO news = memberService.editNews(newsId, request, image);
        return ResponseEntity.ok(news);
    }

    @DeleteMapping("/delete-news")
    public ResponseEntity<?> deleteNews(@RequestParam Long newsId) {
        memberService.deleteNews(newsId);
        return ResponseEntity.ok("News deleted");
    }

    @PostMapping(value = "/create-gallery-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GalleryImageDTO> createGalleryImage(
            @RequestParam("data") String data,
            @RequestPart(value = "image", required = false) MultipartFile image) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        GalleryImageRequest request = mapper.readValue(data, GalleryImageRequest.class);

        GalleryImageDTO galleryImage = memberService.createGalleryImage(request, image);
        return ResponseEntity.ok(galleryImage);
    }

    @GetMapping("/get-all-gallery-images")
    public ResponseEntity<List<GalleryImageDTO>> getAllGalleryImages() {
        return ResponseEntity.ok(memberService.getAllGalleryImages());
    }

    @PutMapping(value = "/edit-gallery-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GalleryImageDTO> editGalleryImage(
            @RequestParam Long newsId,
            @RequestParam("data") String data) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        GalleryImageRequest request = mapper.readValue(data, GalleryImageRequest.class);

        GalleryImageDTO galleryImages = memberService.editGalleryImage(newsId, request);
        return ResponseEntity.ok(galleryImages);
    }

    @DeleteMapping("/delete-gallery-image")
    public ResponseEntity<?> deleteGalleryImage(@RequestParam Long galleryImageId) {
        memberService.deleteGalleryImage(galleryImageId);
        return ResponseEntity.ok("Gallery image deleted");
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(memberService.getAllUsers());
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestParam Long userId) {
        memberService.deleteUser(userId);
        return ResponseEntity.ok("User deleted");
    }
}
