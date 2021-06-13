package ua.deti.tqs.easydeliversadmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
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

    @GetMapping("/")
    public ModelAndView loginOrDashboard(ModelMap model) {
        if (session.equals("")) {
            return new ModelAndView("redirect:/login", model);
        } else {
            return new ModelAndView("redirect:/dashboard", model);
        }
    }

    @GetMapping("/logout")
    public ModelAndView logout(ModelMap model){
        session = "";
        return new ModelAndView("redirect:/login", model);
    }

    @GetMapping("/login")
    public ModelAndView login(ModelMap model){
        if (session.equals("")) {
            return new ModelAndView("login", model);
        } else {
            return new ModelAndView("redirect:/dashboard", model);
        }
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam(value = "email", defaultValue = "hello")String email, @RequestParam(value = "password", defaultValue = "bye")String password, ModelMap model) throws EasyDeliversService.AdminNotFoundException, Exception {
        log.warning("LOGS");
        Admin admin = service.getAdminByEmail(email);
        log.info("Email: " + email);
        log.info("Pass: " + password);
        log.info(admin.getFirst_name());
        PasswordEncryption encryptor = new PasswordEncryption();
        if (admin.getPassword().equals(encryptor.encrypt(password))) {
            session = email;
            return new ModelAndView("redirect:/dashboard", model);
        } else{
            return new ModelAndView("login", model);
        }
    }

    @GetMapping("/dashboard")
    public ModelAndView dash(ModelMap model){
        log.info(session);
        if(!session.equals(""))
            return new ModelAndView("dashboard", model);
        else
            return new ModelAndView("redirect:/login", model);
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
