package ua.deti.tqs.easydeliversadmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.deti.tqs.easydeliversadmin.entities.Admin;
import ua.deti.tqs.easydeliversadmin.entities.Delivery;
import ua.deti.tqs.easydeliversadmin.service.EasyDeliversService;
import ua.deti.tqs.easydeliversadmin.utils.PasswordEncryption;

import java.text.DecimalFormat;
import java.util.List;
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

    @GetMapping("/error")
    public ModelAndView error(ModelMap model) {
        return new ModelAndView("error", model);
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
        Admin admin = service.getAdminByEmail(email);
        if (admin == null) {
            return new ModelAndView("redirect:/error", model);
        }
        PasswordEncryption encryptor = new PasswordEncryption();
        System.out.println(admin.getPassword());
        System.out.println(encryptor.encrypt(password));
        if (admin.getPassword().equals(encryptor.encrypt(password))) {
            session = email;
            return new ModelAndView("redirect:/dashboard", model);
        } else{
            model.addAttribute("error", true);
            model.addAttribute("message", "Invalid credentials. Please, try again.");
            return new ModelAndView("login", model);
        }
    }

    @GetMapping("/dashboard")
    public ModelAndView dash(ModelMap model){
        log.info(session);
        if(!session.equals("")){
            model.addAttribute("kmsCovered", service.sumOfKmCoveredInLast24Hours());
            model.addAttribute("numdeliveries", service.numberDeliveriesMadeForLast24Hours());
            model.addAttribute("avgtime", service.averageTimeDeliveries());
            model.addAttribute("avgScore", service.averageRidersScore());
            model.addAttribute("employees", service.getTopRiders());
            model.addAttribute("nrDeliveries13Days", service.numberDeliveriesMadeForLast13Days());
            return new ModelAndView("dashboard", model);
        }
        else
            return new ModelAndView("redirect:/login", model);
    }

    @GetMapping("/account")
    public ModelAndView account(ModelMap model){
        log.info(session);
        if(!session.equals(""))
            return new ModelAndView("user", model);
        else
            return new ModelAndView("redirect:/login", model);
    }

    @PostMapping("/account")
    public ModelAndView postAccountDetails(@RequestParam(value = "first_name")String first_name, @RequestParam(value = "last_name")String last_name,  @RequestParam(value = "email")String email,  @RequestParam(value = "password")String password, @RequestParam(value = "position")String position, @RequestParam(value = "about")String about, ModelMap model) throws EasyDeliversService.AdminNotFoundException, Exception {
        log.info(session);
        if(!session.equals("")){
            service.updateAdmin(session,first_name,last_name,email,password,position,about);
            return new ModelAndView("user", model);
        }
        else
            return new ModelAndView("redirect:/login", model);
    }


    @GetMapping("/employees")
    public ModelAndView tables(ModelMap model){
        log.info(session);
        if(!session.equals("")){
            model.addAttribute("employees", service.getAllRiders());
            return new ModelAndView("employees", model);
        }
        else
            return new ModelAndView("redirect:/login", model);
    }

    @GetMapping("/deliveries")
    public ModelAndView deliveries(ModelMap model){
        log.info(session);
        if(!session.equals("")) {
            List<Delivery> deliveries = service.getAllCompletedDeliveries();
            model.addAttribute("allDeliveries", deliveries);
            model.addAttribute("employeesNames", service.allRidersNamesByDeliveries(deliveries));
            model.addAttribute("waitingTimes", service.allWaitingTimesByDeliveries(deliveries));
            return new ModelAndView("deliveries", model);
        } else
            return new ModelAndView("redirect:/login", model);
    }

    @GetMapping("/employee")
    public ModelAndView singular(@RequestParam(value="id") int id, ModelMap model){
        if(!session.equals("")){
            model.addAttribute("personalKmsCovered", service.personalSumOfKmCoveredInLast24Hours(id));
            model.addAttribute("numdeliveries", service.personalDeliveriesMadeForLast24Hours(id));
            model.addAttribute("avgtime", service.personalAverageTimeDeliveries(id));
            model.addAttribute("avgScore", service.personalScore(id));
            model.addAttribute("nrDeliveries13Days", service.personalDeliveriesMadeForLast13Days(id));
            model.addAttribute("avgTime13Days", service.personalAverageDeliveryTimeForLast13Days(id));

            return new ModelAndView("dashboard", model);
        }
        else{
            return new ModelAndView("redirect:/login", model);
        }


    }


}
