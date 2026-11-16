package com.blog.springblog.service;

import com.blog.springblog.entity.Image;
import com.blog.springblog.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ImageService {

    private static final Set<String> ALLOWED = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private final Path uploadDir = Path.of("uploads");

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<Image> findAll() {
        return imageRepository.findAllByOrderByUploadedAtDesc();
    }

    public void store(String title, MultipartFile file, boolean inCarousel) throws IOException {
        String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        int dot = original.lastIndexOf('.');
        String ext = dot >= 0 ? original.substring(dot + 1).toLowerCase() : "";

        if (!ALLOWED.contains(ext)) {
            throw new IllegalArgumentException("Solo se permiten imágenes (jpg, png, gif, webp)");
        }

        // Nombre generado: nunca usamos el nombre original del usuario
        String fileName = UUID.randomUUID() + "." + ext;

        Files.createDirectories(uploadDir);
        Files.copy(file.getInputStream(), uploadDir.resolve(fileName));

        Image image = new Image();
        image.setTitle(title);
        image.setFileName(fileName);
        image.setInCarousel(inCarousel);
        imageRepository.save(image);
    }

    public void toggleCarousel(Long id) {
        imageRepository.findById(id).ifPresent(img -> {
            img.setInCarousel(!img.isInCarousel());
            imageRepository.save(img);
        });
    }

    public void delete(Long id) throws IOException {
        var image = imageRepository.findById(id);
        if (image.isPresent()) {
            Files.deleteIfExists(uploadDir.resolve(image.get().getFileName()));
            imageRepository.delete(image.get());
        }
    }
}