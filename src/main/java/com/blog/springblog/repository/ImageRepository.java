package com.blog.springblog.repository;

import com.blog.springblog.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByOrderByUploadedAtDesc();
    List<Image> findByInCarouselTrueOrderByUploadedAtDesc();
}