package com.unirem.news_service;

import com.unirem.news_service.DTO.NewsDTO;
import com.unirem.news_service.entity.News;
import com.unirem.news_service.repository.NewsRepository;
import com.unirem.news_service.service.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class NewsServiceTest {
    @InjectMocks
    private NewsService newsService;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private ModelMapper modelMapper;

    private News news;
    private NewsDTO newsDTO;

    @BeforeEach
    void setup() {
        news = new News();
        news.setNewId(1L);
        news.setTittle("Test News");

        newsDTO = new NewsDTO();
        newsDTO.setNewId(1L);
        newsDTO.setTittle("Test News");
    }

    @Test
    void getValidNews() {
        news.setValid(true);
        when(newsRepository.findByValidTrue()).thenReturn(List.of(news));
        when(modelMapper.map(news, NewsDTO.class)).thenReturn(new NewsDTO());
        List<NewsDTO> result = newsService.getValidNews();
        assertEquals(1, result.size());
    }

    @Test
    void getAllNews() {
        when(newsRepository.findAll()).thenReturn(List.of(news));
        when(modelMapper.map(news, NewsDTO.class)).thenReturn(new NewsDTO());
        List<NewsDTO> result = newsService.getAllNews();
        assertEquals(1, result.size());
    }

    @Test
    void successfulGetNewsById() {
        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));
        when(modelMapper.map(news, NewsDTO.class)).thenReturn(newsDTO);

        NewsDTO result = newsService.getNewsById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getNewId());
        assertEquals("Test News", result.getTittle());

        verify(newsRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(news, NewsDTO.class);
    }

    @Test
    void wrongGetNewsById() {
        when(newsRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> newsService.getNewsById(1L));

        assertEquals("News not found", ex.getMessage());
        verify(newsRepository, times(1)).findById(1L);
        verifyNoInteractions(modelMapper);
    }
}
