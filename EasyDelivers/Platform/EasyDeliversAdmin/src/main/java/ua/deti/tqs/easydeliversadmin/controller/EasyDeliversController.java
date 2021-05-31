package ua.deti.tqs.easydeliversadmin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class EasyDeliversController {

    @GetMapping("/")
    public String index(){
        return "dashboard";
    }

    @GetMapping("/account")
    public String account(){
        return "user";
    }

    @GetMapping("/tables")
    public String tables(){
        return "tables";
    }

    @GetMapping("/typography")
    public String typ(){
        return "typography";
    }

    @GetMapping("/icons")
    public String icons(){
        return "icons";
    }

    @GetMapping("/map")
    public String map(){
        return "maps";
    }

    @GetMapping("/notifications")
    public String notif(){
        return "notifications";
    }

}
