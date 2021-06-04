package ua.deti.tqs.easydeliversadmin.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.deti.tqs.easydeliversadmin.entities.Rider;
import ua.deti.tqs.easydeliversadmin.service.EasyDeliversService;

import java.util.Map;


@RestController
@RequestMapping("/api")
public class EasyDeliversRestController {

    @Autowired
    EasyDeliversService service;

    @PostMapping(value = "/rider/login", consumes = "application/json", produces = "application/json")
    public Rider login(@RequestBody Map<String, Object> request){
        String email = (String) request.get("email");
        String password = (String) request.get("password");

        if (service.authenticateRider(email,password)) {
            return service.getRider(email);
        }
        else {
            return null;
        }
    }
    @PostMapping("/rider/account")
    public Rider createAccount(@RequestBody Map<String, Object> request){
        return null;
    }
    @PostMapping("/delivery")
    public void newDelivery(){

    }


}
