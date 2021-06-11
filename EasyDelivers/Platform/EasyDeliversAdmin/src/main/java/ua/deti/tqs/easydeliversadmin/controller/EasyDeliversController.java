package ua.deti.tqs.easydeliversadmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.deti.tqs.easydeliversadmin.entities.Admin;
import ua.deti.tqs.easydeliversadmin.service.EasyDeliversService;
import ua.deti.tqs.easydeliversadmin.utils.PasswordEncryption;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.logging.Logger;

@Controller
@RequestMapping("/")
public class EasyDeliversController {
    private static final Logger log = Logger.getLogger(EasyDeliversController.class.getName());

    private String session = "";

    @Autowired
    EasyDeliversService service;

    @GetMapping("/logout")
    public String logout(){
        session = "";
        return "login";
    }

    @GetMapping("/login")
    public String login(){
        log.warning("LOGIN");
        return "login";
    }

    @PostMapping("/dashboard")
    public String login(@RequestParam(value = "email", defaultValue = "ola")String email, @RequestParam(value = "password", defaultValue = "ola")String password) throws EasyDeliversService.AdminNotFoundException, Exception {
        log.warning("LOGS");
        Admin admin = service.getAdminByEmail(email);
        log.info("Email: " + email);
        log.info("Pass: " + password);
        log.info(admin.getFirst_name());
        PasswordEncryption encryptor = new PasswordEncryption();
        if (admin.getPassword().equals(encryptor.encrypt(password))) {
            session = email;
            return "dashboard";
        } else{
            return "login";
        }
    }

    @GetMapping("/dashboard")
    public String dash(){
        log.info(session);
        if(!session.equals(""))
            return "dashboard";
        else
            return "login";
    }

    @GetMapping("/account")
    public String account(){
        log.info(session);
        if(!session.equals(""))
            return "user";
        else
            return "login";
    }

    @GetMapping("/employees")
    public String tables(){
        log.info(session);
        if(!session.equals(""))
            return "employees";
        else
            return "login";
    }

    @GetMapping("/deliveries")
    public String deliveries(){
        log.info(session);
        if(!session.equals(""))
            return "deliveries";
        else
            return "login";
    }

    @GetMapping("/employee")
    public String singular(@RequestParam(value="id") int id, Model model){
        log.info(session);
        if(!session.equals("")){
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
        else{
            return "login";
        }


    }


}
