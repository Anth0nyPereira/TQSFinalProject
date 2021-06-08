package ua.deti.tqs.easydeliversadmin.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.deti.tqs.easydeliversadmin.entities.Admin;

import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.logging.Logger;

@Controller
@RequestMapping("/")
public class EasyDeliversController {
    private static final Logger log = Logger.getLogger(EasyDeliversController.class.getName());

    @GetMapping("/login")
    public String login(){
        return "login";
    }

   /* @PostMapping("/login")
    public String postLogin(Model model, HttpSession session){
        log.info("postLogin()");

        // read principal out of security context and set it to session
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Admin loggedInUser = ((Admin) authentication.getPrincipal());

        session.setAttribute("userEmail", loggedInUser.getEmail());
        return "redirect:/wallPage";
    }*/

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

    @GetMapping("/employee")
    public String singular(@RequestParam(value="id") int id, Model model){
        Random randomNumb = new Random();
        int kms = randomNumb.nextInt(40);
        int deliveries = randomNumb.nextInt(40);
        int time = randomNumb.nextInt(40);
        DecimalFormat df = new DecimalFormat("#.#");
        double score = 5 * randomNumb.nextDouble();
        model.addAttribute("kms", kms);
        model.addAttribute("deliveries", deliveries);
        model.addAttribute("time", time);
        model.addAttribute("score", df.format(score));
        return "riderDashboard";
    }


}
