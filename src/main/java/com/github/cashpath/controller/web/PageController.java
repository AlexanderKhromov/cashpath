package com.github.cashpath.controller.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class PageController {
    /*

    @GetMapping("/")
    public String index() {
        return "index"; // templates/index.html
    }
     */

    // Testing endpoints for simulation errors
    @GetMapping("/error400")
    public void error400() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test 400 Bad Request");
    }

    @GetMapping("/error403")
    public void error403() {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Test 403 Forbidden");
    }

    @GetMapping("/error404")
    public void error404() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Test 404 Not Found");
    }

    @GetMapping("/error500")
    public void error500() {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Test 500 Internal Server Error");
    }

    @GetMapping("/error503")
    public void error503() {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Test 503 Service Unavailable");
    }
}
