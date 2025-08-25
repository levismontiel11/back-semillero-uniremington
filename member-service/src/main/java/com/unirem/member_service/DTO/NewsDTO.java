package com.unirem.member_service.DTO;

import com.unirem.member_service.entity.NewsCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {
    private Long newId;
    private String tittle;
    private String excerpt;
    private String content;
    private NewsCategory category;
    private String date;
    private String imageUrl;
    private Long authorId;
    private String slug;
    private Boolean valid;
}
