package com.example.Restaurant.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class testController {

    @GetMapping({ "/", "/home" })
    public String res(final Model model, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        return "user/index";
    }

    @GetMapping({ "/home1" })
    public String res1(final Model model, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        return "user/index1";
    }

    @GetMapping({ "admin/hello" })
    public String resad(final Model model, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        return "admin/index";
    }

}