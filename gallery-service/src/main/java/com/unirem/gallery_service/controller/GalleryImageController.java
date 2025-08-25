package com.unirem.gallery_service.controller;

import com.unirem.gallery_service.DTO.GalleryImageDTO;
import com.unirem.gallery_service.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gallery")
public class GalleryImageController {
    @Autowired
    private GalleryService galleryService;

    @GetMapping("/get-all-gallery-images")
    public ResponseEntity<List<GalleryImageDTO>> getAllGalleryImages() {
        return ResponseEntity.ok(galleryService.getAllGalleryImages());
    }

    @GetMapping("/get-gallery-image-by-id")
    public ResponseEntity<GalleryImageDTO> getImageById(@RequestParam Long imageId) {
        return ResponseEntity.ok(galleryService.getImageById(imageId));
    }
}
