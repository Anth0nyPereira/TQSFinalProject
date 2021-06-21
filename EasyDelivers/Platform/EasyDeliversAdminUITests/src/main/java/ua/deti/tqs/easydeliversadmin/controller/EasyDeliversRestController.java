package ua.deti.tqs.easydeliversadmin.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.deti.tqs.easydeliversadmin.entities.Delivery;
import ua.deti.tqs.easydeliversadmin.entities.Rider;
import ua.deti.tqs.easydeliversadmin.service.EasyDeliversService;
import ua.deti.tqs.easydeliversadmin.utils.CouldNotEncryptException;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class EasyDeliversRestController {

    @Autowired
    EasyDeliversService service;
    

    // EndPoint for Rider Login

    @PostMapping(value = "/rider/login", consumes = "application/json", produces = "application/json")
    public Rider login(@RequestBody Map<String, Object> request) throws CouldNotEncryptException {
        String email = (String) request.get("email");
        String password = (String) request.get("password");

        if (service.authenticateRider(email, password)) {
            return service.getRider(email);
        }
        else {
            return null;
        }
    }

    // EndPoint for Rider Sign Up

    @PostMapping("/rider/account")
    public Rider createAccount(@RequestBody Map<String, Object> request){
        String firstname = (String) request.get("firstname");
        String lastname = (String) request.get("lastname");
        String email = (String) request.get("email");
        String password = (String) request.get("password");
        String telephone = (String) request.get("telephone");
        String transportation = (String) request.get("transportation");

        return service.createRider(firstname, lastname, email, password, telephone, transportation);
    }

    // EndPoint for Rider gets Available Deliveries

    @GetMapping("/rider/deliveries")
    public List<Delivery> getAvailableDeliveriesRider(){
        return service.getAvailableDeliveries();
    }

    // EndPoint for Rider accept Deliver

    @PutMapping("/rider/deliveries/{DeliverID}/{RiderID}")
    public String riderAcceptDeliver(@PathVariable String DeliverID, @PathVariable String RiderID){
       return service.assignRiderDeliver(DeliverID,RiderID);
    }

    // EndPoint for Rider Update Delivery Status

    @PutMapping("/rider/deliveries/update/{DeliverID}/{RiderID}/{state}")
    public String riderAcceptDeliver(@PathVariable String DeliverID, @PathVariable String RiderID, @PathVariable String state){
        return service.updateDeliveryStateByRider(DeliverID,RiderID, state);
    }

    @PostMapping("/delivery")
    public Integer newDelivery(@RequestBody Map<String, Object> request){
        int store = (Integer) request.get("store");
        String client_telephone = (String) request.get("client_telephone");
        String start = (String) request.get("start");
        String destination = (String) request.get("destination");
        return service.createDelivery(store, client_telephone, start, destination);
    }

}
