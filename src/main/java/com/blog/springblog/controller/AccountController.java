package com.blog.springblog.controller;

import com.blog.springblog.repository.ContactRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AccountController {

    private final ContactRepository contactRepository;

    public AccountController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @GetMapping("/my-messages")
    public String myMessages(Principal principal, Model model) {
        model.addAttribute("messages",
                contactRepository.findByEmailIgnoreCaseOrderByIdDesc(principal.getName()));
        return "account/messages";
    }
}