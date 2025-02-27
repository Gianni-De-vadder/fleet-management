package fr.extia.mentoring.fleetmanagement.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public class RootController {
    @GetMapping
    public String hello() {
        return "Hello";
    }
}
