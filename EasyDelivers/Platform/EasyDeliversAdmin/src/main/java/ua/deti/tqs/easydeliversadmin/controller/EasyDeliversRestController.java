package ua.deti.tqs.easydeliversadmin.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.deti.tqs.easydeliversadmin.entities.Delivery;
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
        String name = (String) request.get("name");
        String email = (String) request.get("email");
        String password = (String) request.get("password");
        String telephone = (String) request.get("telephone");
        String transportation = (String) request.get("transportation");

        return service.createRider(name, email, password, telephone, transportation);
    }
    @PostMapping("/delivery")
    public String newDelivery(@RequestBody Map<String, Object> request){
        int store = (Integer) request.get("store");
        String client_telephone = (String) request.get("client_telephone");
        String start = (String) request.get("start");
        String destination = (String) request.get("destination");

        return service.createDelivery(store, client_telephone, start, destination);
    }


}
