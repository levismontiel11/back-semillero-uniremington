package com.unirem.news_service.controller;

import com.unirem.news_service.DTO.NewsDTO;
import com.unirem.news_service.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping("/get-all-news")
    public ResponseEntity<List<NewsDTO>> getAllNews() {
        return ResponseEntity.ok(newsService.getAllNews());
    }

    @GetMapping("/get-valid-news")
    public ResponseEntity<List<NewsDTO>> getValidNews() {
        return ResponseEntity.ok(newsService.getValidNews());
    }

    @GetMapping("/get-news-by-id")
    public ResponseEntity<NewsDTO> getNewsById(@RequestParam Long newsId) {
        return ResponseEntity.ok(newsService.getNewsById(newsId));
    }
}
