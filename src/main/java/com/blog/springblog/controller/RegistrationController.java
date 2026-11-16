package com.blog.springblog.controller;

import com.blog.springblog.dto.RegistrationForm;
import com.blog.springblog.entity.User;
import com.blog.springblog.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String form(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "registration/register";
    }

    @PostMapping("/register")
    public String submit(@Valid @ModelAttribute("registrationForm") RegistrationForm form,
                         BindingResult result) {
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "mismatch", "Las contraseñas no coinciden");
        }
        if (userRepository.existsByEmail(form.getEmail())) {
            result.rejectValue("email", "duplicate", "Ya existe una cuenta con ese email");
        }
        if (result.hasErrors()) {
            return "registration/register";
        }

        User user = new User();
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        userRepository.save(user);

        return "redirect:/login?registered";
    }
}