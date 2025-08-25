package com.unirem.member_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GalleryImageDTO {
    private Long imageId;
    private String tittle;
    private String description;
    private String imageUrl;
}
