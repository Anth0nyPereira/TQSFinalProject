package ua.deti.tqs.easydeliversadmin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.deti.tqs.easydeliversadmin.entities.Rider;
import ua.deti.tqs.easydeliversadmin.repository.RiderRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class EasyDeliversService {

    @Autowired
    RiderRepository riderRepository;

    public boolean authenticateRider(String email, String password) {
        return true;
    }

    public Rider getRider(String email) {
        return null;
    }

    public Rider createRider(String firstname, String lastname, String email, String password, String telephone, String transportation) {
        return null;
    }

    public String createDelivery(int store, String client_telephone, String start, String destination) {
        return "Delivery accepted";
    }
}
