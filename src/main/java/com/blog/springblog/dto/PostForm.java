package com.blog.springblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostForm {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "Máximo 150 caracteres")
    private String title;

    @NotBlank(message = "El contenido es obligatorio")
    private String content;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}