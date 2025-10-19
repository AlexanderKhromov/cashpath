package com.github.cashpath.controller.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

@Controller
public class CashPathErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CashPathErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        ServletWebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(
                webRequest,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE)
        );

        int status = (int) attributes.getOrDefault("status", 500);
        model.addAllAttributes(attributes);

        if (status == 400) return "error/400";
        if (status == 403) return "error/403";
        if (status == 404) return "error/404";
        if (status == 500) return "error/500";
        if (status == 503) return "error/503";

        return "error/generic";
    }
}
