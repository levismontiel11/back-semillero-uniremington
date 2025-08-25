package com.unirem.news_service.entity;

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
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long newId;
    @Column(nullable = false)
    private String tittle;
    @Column(nullable = false)
    private String excerpt;
    @Column(nullable = false)
    private String content;
    @Enumerated(EnumType.STRING)
    private NewsCategory category;
    @Column(nullable = false)
    private String date;
    private String imageUrl;
    @Column(name = "author_id")
    private Long authorId;
    private String slug;
    @Column(nullable = false)
    private Boolean valid;
}
