package com.unirem.gallery_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gallery_images")
public class GalleryImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long imageId;
    @Column(nullable = false)
    private String tittle;
    @Column(nullable = false)
    private String description;
    private String imageUrl;
}
