package com.blog.springblog.controller;

import com.blog.springblog.service.ImageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin/images")
public class AdminController {

    private final ImageService imageService;

    public AdminController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("images", imageService.findAll());
        return "admin/images";
    }

    @PostMapping
    public String upload(@RequestParam String title,
                         @RequestParam("file") MultipartFile file,
                         @RequestParam(defaultValue = "false") boolean inCarousel,
                         RedirectAttributes flash) {

        if (title.isBlank() || file.isEmpty()) {
            flash.addFlashAttribute("error", "Rellena el título y elige un archivo");
            return "redirect:/admin/images";
        }
        try {
            imageService.store(title, file, inCarousel);
            flash.addFlashAttribute("ok", "Imagen subida correctamente");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
        } catch (IOException e) {
            flash.addFlashAttribute("error", "No se pudo guardar el archivo");
        }
        return "redirect:/admin/images";
    }

    @PostMapping("/{id}/carousel")
    public String toggle(@PathVariable Long id) {
        imageService.toggleCarousel(id);
        return "redirect:/admin/images";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes flash) throws IOException {
        imageService.delete(id);
        flash.addFlashAttribute("ok", "Imagen eliminada");
        return "redirect:/admin/images";
    }
}