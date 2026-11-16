package com.blog.springblog.controller;

import com.blog.springblog.dto.ContactForm;
import com.blog.springblog.entity.Contact;
import com.blog.springblog.repository.ContactRepository;
import com.blog.springblog.repository.ImageRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {

    private final ContactRepository contactRepository;
    private final ImageRepository imageRepository;

    public PageController(ContactRepository contactRepository, ImageRepository imageRepository) {
        this.contactRepository = contactRepository;
        this.imageRepository = imageRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("slides", imageRepository.findByInCarouselTrueOrderByUploadedAtDesc());
        return "page/index";
    }

    @GetMapping("/about")
    public String about() {
        return "page/about";
    }

    @GetMapping("/contact")
    public String contactForm(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "page/contact";
    }

    @PostMapping("/contact")
    public String contactSubmit(@Valid @ModelAttribute("contactForm") ContactForm form,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "page/contact";
        }

        Contact contact = new Contact();
        contact.setFirstName(form.getFirstName());
        contact.setLastName(form.getLastName());
        contact.setEmail(form.getEmail());
        contact.setSubject(form.getSubject());
        contact.setMessage(form.getMessage());
        contactRepository.save(contact);

        return "redirect:/thankyou";
    }

    @GetMapping("/thankyou")
    public String thankyou() {
        return "thankyou";
    }
}