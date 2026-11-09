package com.blog.springblog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ContactForm {

    @NotBlank(message = "Field `First Name` is mandatory")
    private String firstName;

    private String lastName;

    @NotBlank(message = "Field `Email` is mandatory")
    @Email(message = "Not a valid email")
    private String email;

    @NotBlank(message = "Field `Subject` is mandatory")
    private String subject;

    @NotBlank(message = "Field `Message` is mandatory")
    private String message;

    // Getters y Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}