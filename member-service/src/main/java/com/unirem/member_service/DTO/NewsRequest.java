package com.unirem.member_service.DTO;

import com.unirem.member_service.entity.NewsCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequest {
    private String tittle;
    private String excerpt;
    private String content;
    private NewsCategory category;
    private String date;
    private MultipartFile image;
    private Long authorId;
    private String slug;
    private Boolean valid;
}
