package ua.deti.tqs.easydeliversadmin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.deti.tqs.easydeliversadmin.entities.Delivery;
import ua.deti.tqs.easydeliversadmin.entities.Rider;
import ua.deti.tqs.easydeliversadmin.repository.DeliveryRepository;
import ua.deti.tqs.easydeliversadmin.repository.RiderRepository;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@Service
@Transactional
public class EasyDeliversService {
    Logger logger = Logger.getLogger(EasyDeliversService.class.getName());

    Map<Integer,String> addresses;

    @Autowired
    RiderRepository riderRepository;

    public void initializer(){
        addresses = new HashMap<Integer, String>();
        addresses.put(1, "localhost:9000"); // TODO: change this to match ProudPapers address
    }

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
            //ADD STATE WITH FOR TimeStampValues
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
            //ADD STATE WITH FOR TimeStampValues
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
            //ADD STATE WITH FOR TimeStampValues
            postToApi(state,x.getId(),Integer.parseInt(deliverID));
            return "Delivery State Changed";
        }
        catch (Exception e){
            return "error";
        }
    }

    //Function to update Delivery Status from Delivery from certain store

    private void postToApi(String state, int store, int delivery_id) throws Exception {
        initializer(); // For now, this will be updated
        //Fazer um get da store e do seu url
        //Mandamos as coisas por Json de modo a que se possa usar diferents links

        URL my_final_url = new URL(store + "/update/" + delivery_id + "/state/" + state);
        HttpURLConnection con = (HttpURLConnection) my_final_url.openConnection(); // open HTTP connection
        con.setRequestMethod("POST");

        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
            logger.info(state);
        }
    }
}
