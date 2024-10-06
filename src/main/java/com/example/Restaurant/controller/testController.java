package com.example.Restaurant.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class testController {

    @GetMapping({"/", "/hello"})
    public String res(final Model model, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
            return "user/index";
    }
}
