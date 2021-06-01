package ua.deti.tqs.easydeliversadmin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class EasyDeliversController {

    @GetMapping("/")
    public String login(){
        return "login";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @GetMapping("/dashboard")
    public String dash(){
        return "dashboard";
    }

    @GetMapping("/account")
    public String account(){
        return "user";
    }

    @GetMapping("/employees")
    public String tables(){
        return "employees";
    }

    @GetMapping("/deliveries")
    public String deliveries(){
        return "deliveries";
    }


}
