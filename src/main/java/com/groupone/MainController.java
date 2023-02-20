package com.groupone;

import com.groupone.users.Users;
import com.groupone.users.UsersService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
        modelAndView.addObject("user", new Users());
        return modelAndView;
    }

    @PostMapping("/register")
    public void processRegister(@RequestParam(name = "setEmail") String email,
                                @RequestParam(name = "setPassword") String password,
                                HttpServletResponse response) throws IOException {
        usersService.createUser(email, password);
        response.sendRedirect("login");
    }

    @GetMapping("/activate/{code}")
    public ModelAndView activate(@PathVariable String code,
                                 HttpServletResponse response) throws IOException {
        ModelAndView modelAndView = new ModelAndView("login");
        boolean isActivated = usersService.activateUser(code);
        if (isActivated) {
            modelAndView.addObject("message", "User successfully activated");
        } else {
            modelAndView.addObject("message", "activation code is not found");
        }

        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView("login");
        return modelAndView;
    }

}
