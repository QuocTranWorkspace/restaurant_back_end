package com.example.restaurant.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * The type Test controller.
 */
@Controller
@RequestMapping("api/auth")
public class testController {
    /**
     * Res response entity.
     *
     * @param model    the model
     * @param request  the request
     * @param response the response
     * @return the response entity
     * @throws IOException the io exception
     */
    @GetMapping({"/", "/home"})
    public ResponseEntity<String> res(final Model model, final HttpServletRequest request,
                                      final HttpServletResponse response)
            throws IOException {
        return ResponseEntity.ok("hello");
    }

    /**
     * Res 1 string.
     *
     * @param model    the model
     * @param request  the request
     * @param response the response
     * @return the string
     * @throws IOException the io exception
     */
    @GetMapping({"/home1"})
    public String res1(final Model model, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        return "user/index1";
    }

    /**
     * Resad string.
     *
     * @param model    the model
     * @param request  the request
     * @param response the response
     * @return the string
     * @throws IOException the io exception
     */
    @GetMapping({"admin/hello"})
    public String resad(final Model model, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        return "admin/index";
    }
}