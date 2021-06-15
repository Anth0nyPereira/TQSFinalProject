package ua.deti.tqs.easydeliversadmin.service;

import ch.qos.logback.classic.util.LogbackMDCAdapter;
import ch.qos.logback.core.encoder.EchoEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.deti.tqs.easydeliversadmin.entities.Delivery;
import ua.deti.tqs.easydeliversadmin.entities.Rider;
import ua.deti.tqs.easydeliversadmin.repository.DeliveryRepository;
import ua.deti.tqs.easydeliversadmin.repository.RiderRepository;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class EasyDeliversService {
    Map<Integer,String> addresses;

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

    public Integer createDelivery(int store, String client_telephone, String start, String destination) {
       Delivery n;

        try {
            n= deliveryRepository.save( new Delivery(store,2,"awaiting_processing",client_telephone,start,destination));
            return n.getId();
        }
        catch (Exception e){
            return -1;
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
            //postToApi(state,x.getId(),Integer.parseInt(deliverID));
            return "Delivery State Changed";
        }
        catch (Exception e){
            return "error";
        }
    }

    //Function to update Delivery Status from Delivery from certain store

    private void postToApi(String state, int store, int delivery_id) throws Exception {
        //For now 1;Later this will be updated
        URL my_final_url = new URL(1 + "/update/" + delivery_id + "/state/" + state);
        HttpURLConnection con = (HttpURLConnection) my_final_url.openConnection(); // open HTTP connection
        con.setRequestMethod("POST");
    }
}
