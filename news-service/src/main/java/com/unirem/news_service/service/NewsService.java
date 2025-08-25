package com.unirem.news_service.service;

import com.unirem.news_service.DTO.NewsDTO;
import com.unirem.news_service.entity.News;
import com.unirem.news_service.repository.NewsRepository;
import com.unirem.news_service.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<NewsDTO> getAllNews() {
        return newsRepository.findAll().stream()
                .map(this::newsToNewsDTO)
                .toList();
    }

    public List<NewsDTO> getValidNews() {
        return newsRepository.findByValidTrue().stream()
                .map(this::newsToNewsDTO)
                .toList();
    }

    public NewsDTO getNewsById(Long newsId) {
        return newsToNewsDTO(newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found")));
    }

    private NewsDTO newsToNewsDTO(News news) {
        return modelMapper.map(news, NewsDTO.class);
    }
}
