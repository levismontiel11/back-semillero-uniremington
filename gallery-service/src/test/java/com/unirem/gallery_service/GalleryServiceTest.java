package com.unirem.gallery_service;

import com.unirem.gallery_service.DTO.GalleryImageDTO;
import com.unirem.gallery_service.entity.GalleryImage;
import com.unirem.gallery_service.repository.GalleryImageRepository;
import com.unirem.gallery_service.service.GalleryService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class GalleryServiceTest {
    @InjectMocks
    private GalleryService galleryService;

    @Mock
    private GalleryImageRepository galleryImageRepository;

    @Mock
    private ModelMapper modelMapper;

    private GalleryImage galleryImage;
    private GalleryImageDTO galleryImageDTO;

    @BeforeEach
    void setup() {
        galleryImage = new GalleryImage();
        galleryImage.setImageId(1L);
        galleryImage.setTittle("Project Meeting");

        galleryImageDTO = new GalleryImageDTO();
        galleryImageDTO.setImageId(1L);
        galleryImageDTO.setTittle("Project Meeting");
    }

    @Test
    void getAllGalleryImages() {
        when(galleryImageRepository.findAll()).thenReturn(List.of(galleryImage));
        when(modelMapper.map(galleryImage, GalleryImageDTO.class)).thenReturn(new GalleryImageDTO());
        List<GalleryImageDTO> result = galleryService.getAllGalleryImages();
        assertEquals(1, result.size());
    }

    @Test
    void successfulGetImageById() {
        when(galleryImageRepository.findById(1L)).thenReturn(Optional.of(galleryImage));
        when(modelMapper.map(galleryImage, GalleryImageDTO.class)).thenReturn(galleryImageDTO);

        GalleryImageDTO result = galleryService.getImageById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getImageId());
        assertEquals("Project Meeting", result.getTittle());

        verify(galleryImageRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(galleryImage, GalleryImageDTO.class);
    }

    @Test
    void wrongGetImageById() {
        when(galleryImageRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> galleryService.getImageById(1L));

        assertEquals("Gallery image not found", ex.getMessage());
        verify(galleryImageRepository, times(1)).findById(1L);
        verifyNoInteractions(modelMapper);
    }
}
