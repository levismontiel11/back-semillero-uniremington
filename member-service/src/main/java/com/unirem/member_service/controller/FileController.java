package com.unirem.member_service.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/**")
    public ResponseEntity<Resource> getFile(HttpServletRequest request) throws IOException {
        // Extraer path completo después de "/files/"
        String fullPath = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        fullPath = fullPath.replaceFirst("/files/", "");

        // Decodificar URL (%20 -> espacio, etc.)
        fullPath = java.net.URLDecoder.decode(fullPath, java.nio.charset.StandardCharsets.UTF_8);

        Path filePath = Paths.get(uploadDir).resolve(fullPath).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Detectar Content-Type automáticamente
        String contentType = java.nio.file.Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream"; // fallback genérico
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }




}

