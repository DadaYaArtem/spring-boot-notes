package com.groupone;

import com.groupone.users.UserEntity;
import com.groupone.users.UsersService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
@AllArgsConstructor
public class MainController {
    UsersService usersService;

    @GetMapping("/")
    public String showHomePage() {
        return "redirect:/note/list";
    }

    @GetMapping("/register")
    public ModelAndView showRegistrationForm() {
        ModelAndView modelAndView = new ModelAndView("register");
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView  processRegister(@RequestParam(name = "setEmail") String email,
                                @RequestParam(name = "setPassword") String password){
        ModelAndView modelAndView;

        if (usersService.findByEmail(email) != null){
            modelAndView = new ModelAndView("register");
            modelAndView.addObject("message", "User with this email already exists");
        }else {
            usersService.createUser(email, password);
            modelAndView = new ModelAndView("login");
        }
        return modelAndView;

    }

    @GetMapping("/activate/{code}")
    public RedirectView activate(@PathVariable String code,
                                 RedirectAttributes attributes) {
        boolean isActivated = usersService.activateUser(code);
        if (isActivated) {
            attributes.addFlashAttribute("message", "User successfully activated");
        } else {
            attributes.addFlashAttribute("message", "Activation code is incorrect or expired");
        }
        return new RedirectView("/login");
    }

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView("login");
        return modelAndView;
    }

}
