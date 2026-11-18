package com.blog.springblog.controller;

import com.blog.springblog.repository.ContactRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/contacts")
public class AdminContactController {

    private final ContactRepository contactRepository;

    public AdminContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("contacts", contactRepository.findAllByOrderByIdDesc());
        return "admin/contacts";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        contactRepository.deleteById(id);
        flash.addFlashAttribute("ok", "Mensaje eliminado");
        return "redirect:/admin/contacts";
    }

    @PostMapping("/{id}/reply")
    public String reply(@PathVariable Long id,
            @RequestParam String reply,
            RedirectAttributes flash) {
        if (!reply.isBlank()) {
            contactRepository.findById(id).ifPresent(c -> {
                c.setReply(reply.trim());
                c.setRepliedAt(LocalDateTime.now());
                contactRepository.save(c);
            });
            flash.addFlashAttribute("ok", "Respuesta guardada");
        }
        return "redirect:/admin/contacts";
    }
}