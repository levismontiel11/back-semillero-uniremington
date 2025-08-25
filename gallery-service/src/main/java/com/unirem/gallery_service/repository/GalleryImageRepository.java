package com.unirem.gallery_service.repository;

import com.unirem.gallery_service.entity.GalleryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryImageRepository extends JpaRepository<GalleryImage, Long> {
}
