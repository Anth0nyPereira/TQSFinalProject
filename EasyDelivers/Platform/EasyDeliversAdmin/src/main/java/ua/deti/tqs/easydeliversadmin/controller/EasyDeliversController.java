package ua.deti.tqs.easydeliversadmin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/dashboard")
    public String dash(@RequestParam(value = "email", defaultValue = "")String email, @RequestParam(value = "password", defaultValue = "")String password){
        return "dashboard";
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
