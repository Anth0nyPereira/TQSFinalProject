package ua.deti.tqs.easydeliversadmin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.deti.tqs.easydeliversadmin.entities.Admin;
import ua.deti.tqs.easydeliversadmin.entities.Rider;
import ua.deti.tqs.easydeliversadmin.repository.AdminRepository;
import org.hibernate.exception.ConstraintViolationException;
import ch.qos.logback.core.encoder.EchoEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.deti.tqs.easydeliversadmin.entities.Delivery;
import ua.deti.tqs.easydeliversadmin.entities.Rider;
import ua.deti.tqs.easydeliversadmin.repository.DeliveryRepository;
import ua.deti.tqs.easydeliversadmin.repository.RiderRepository;


import java.util.List;


@Service
@Transactional
public class EasyDeliversService {

    @Autowired
    public AdminRepository adminRepository;


    public Admin getAdminByEmail(String email) throws AdminNotFoundException {
        Admin user = adminRepository.findAdminByEmail(email);

        if (user == null) {
            throw new AdminNotFoundException("Admin not found.");
        }

        return user;
    }
    @Autowired
    RiderRepository riderRepository;

    @Autowired
    DeliveryRepository deliveryRepository;
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


    public class AdminNotFoundException extends Throwable {
        public AdminNotFoundException(String s) {
            final Logger log = LoggerFactory.getLogger(EasyDeliversService.class);
            log.info(s);
        }
    }
    public List<Delivery> getAvailableDeliveries(){
        return deliveryRepository.findDeliveriesByState("awaiting_processing");
    }

    public String assignRiderDeliver(String deliverID, String riderID) {
        try{
            Delivery x = deliveryRepository.findDeliveryById(Integer.parseInt(deliverID));
            x.setRider(Integer.parseInt(riderID));
            x.setState("accepted");
            deliveryRepository.save(x);
            //Aqui mandar post para a loja a atualizar o state
            return "Delivery Assigned";
        }
        catch(Exception e){
            return "error";
        }

    }

    public String updateDeliveryStateByRider(String deliverID, String riderID, String state) {
        try {
            Delivery x = deliveryRepository.findDeliveryById(Integer.parseInt(deliverID));
            x.setState(state);
            deliveryRepository.save(x);
            //Aqui mandar post para a loja a atualizar o state
            return "Delivery State Changed";
        }
        catch (Exception e){
            return "error";
        }
    }
}
