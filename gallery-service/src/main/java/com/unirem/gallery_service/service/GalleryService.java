package com.unirem.gallery_service.service;

import com.unirem.gallery_service.DTO.GalleryImageDTO;
import com.unirem.gallery_service.entity.GalleryImage;
import com.unirem.gallery_service.repository.GalleryImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GalleryService {
    @Autowired
    private GalleryImageRepository galleryImageRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<GalleryImageDTO> getAllGalleryImages() {
        return galleryImageRepository.findAll().stream()
                .map(this::galleryImageToGalleryImageDTO)
                .toList();
    }

    public GalleryImageDTO getImageById(Long imageId) {
        return galleryImageToGalleryImageDTO(galleryImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Gallery image not found")));
    }

    private GalleryImageDTO galleryImageToGalleryImageDTO(GalleryImage galleryImage) {
        return modelMapper.map(galleryImage, GalleryImageDTO.class);
    }
}
