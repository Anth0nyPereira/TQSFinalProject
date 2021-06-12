package ua.deti.tqs.easydeliversadmin.service;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.deti.tqs.easydeliversadmin.entities.Rider;
import ua.deti.tqs.easydeliversadmin.repository.RiderRepository;

import java.sql.SQLException;


@Service
@Transactional
public class EasyDeliversService {

    @Autowired
    RiderRepository riderRepository;

    public boolean authenticateRider(String email, String password) {
        // Hash Password to consider
        Rider x = riderRepository.findRiderByEmail(email);
        if(x!=null && x.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public Rider getRider(String email) {
        return riderRepository.findRiderByEmail(email);
    }

    public Rider createRider(String firstname, String lastname, String email, String password, String telephone, String transportation) {
        if (riderRepository.findRiderByEmail(email)!=null)
            return null;
        return riderRepository.save(new Rider(firstname,lastname,email,password,telephone,transportation));
    }

    public String createDelivery(int store, String client_telephone, String start, String destination) {
        return "Delivery accepted";
    }
}
