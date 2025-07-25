package se2.group6.librarymanagement.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Component
@ControllerAdvice
public class CustomErrorHandler {

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, RedirectAttributes redirectAttributes) {
        if (ex.getMessage().contains("Username already exists")) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/500";
        }
        
        // Handle other specific exceptions as needed
        
        return "error";
    }
}
