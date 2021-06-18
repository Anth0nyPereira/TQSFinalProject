package ua.deti.tqs.easydeliversadmin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.deti.tqs.easydeliversadmin.entities.*;
import ua.deti.tqs.easydeliversadmin.repository.*;
import org.springframework.transaction.annotation.Transactional;
import ua.deti.tqs.easydeliversadmin.utils.PasswordEncryption;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


@Service
@Transactional
public class EasyDeliversService {
    Logger logger = Logger.getLogger(EasyDeliversService.class.getName());
    Date date = new Date();

    @Autowired
    public AdminRepository adminRepository;

    @Autowired
    RiderRepository riderRepository;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    StateRepository stateRepository;

    @Autowired
    StoreRepository storeRepository;

    public Admin getAdminByEmail(String email) throws AdminNotFoundException {
        Admin user = adminRepository.findAdminByEmail(email);

        if (user == null) {
            throw new AdminNotFoundException("Admin not found.");
        }

        return user;
    }

    public boolean authenticateRider(String email, String password) {
        PasswordEncryption encryptor = new PasswordEncryption();
        Rider x = riderRepository.findRiderByEmail(email);
        try {
            if(x!=null && encryptor.encrypt(x.getPassword()).equals(password)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public Integer createDelivery(int store, String client_telephone, String start, String destination) {
       Delivery n;

        try {
            n= deliveryRepository.save( new Delivery(store,2,"awaiting_processing",client_telephone,start,destination));
            State s = stateRepository.save(new State("awaiting_processing", n.getId(), new Timestamp(System.currentTimeMillis())));
            return n.getId();
        }
        catch (Exception e){
            return -1;
        }

    }


    public class AdminNotFoundException extends Throwable {
        public AdminNotFoundException(String s) {
            final Logger log = Logger.getLogger(EasyDeliversService.class.getName());
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
            State s = stateRepository.save(new State("accepted", x.getId(), new Timestamp(System.currentTimeMillis())));
            deliveryRepository.save(x);
            postToApi("accepted",x.getId(),Integer.parseInt(deliverID));
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
            State s = stateRepository.save(new State(state, x.getId(), new Timestamp(System.currentTimeMillis())));
            postToApi(state,x.getId(),Integer.parseInt(deliverID));
            return "Delivery State Changed";
        }
        catch (Exception e){
            return "error";
        }
    }

    //Function to update Delivery Status from Delivery from certain store
    // EasyDelivers -> ProudPapers
    private void postToApi(String state, int store, int delivery_id) throws Exception {
        Store store_from_db = storeRepository.findStoreById(store);
        String address = store_from_db.getAddress();

        URL my_final_url = new URL(address + "/update/" + delivery_id + "/state/" + state);
        HttpURLConnection con = (HttpURLConnection) my_final_url.openConnection(); // open HTTP connection
        con.setRequestMethod("POST");

        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
            logger.info(state);
        }
    }
}
