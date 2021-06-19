package ua.deti.tqs.easydeliversadmin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.deti.tqs.easydeliversadmin.entities.*;
import ua.deti.tqs.easydeliversadmin.repository.*;
import org.springframework.transaction.annotation.Transactional;
import ua.deti.tqs.easydeliversadmin.utils.CouldNotEncryptException;
import ua.deti.tqs.easydeliversadmin.utils.PasswordEncryption;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


@Service
@Transactional
public class EasyDeliversService {
    Logger logger = Logger.getLogger(EasyDeliversService.class.getName());
    Date date = new Date();

    @Autowired
    AdminRepository adminRepository;

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

    public boolean authenticateRider(String email, String password) throws CouldNotEncryptException{
        // Hash Password to consider
        try {
            PasswordEncryption enc = new PasswordEncryption();
            Rider x = riderRepository.findRiderByEmail(email);
            password = enc.encrypt(password);
            if (x!=null && x.getPassword().equals(password)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new CouldNotEncryptException();
        }
    }

    public Rider getRider(String email) {
        return riderRepository.findRiderByEmail(email);
    }

    public Rider createRider(String firstname, String lastname, String email, String password, String telephone, String transportation) {
        if (riderRepository.findRiderByEmail(email)!=null)
            return null;
        return riderRepository.save(new Rider(firstname,lastname,email,password,telephone,transportation, 1000.00));
    }

    public Integer createDelivery(int store, String client_telephone, String start, String destination) {
        Delivery n;

        try {
            n= deliveryRepository.save( new Delivery(store,2,"awaiting_processing",client_telephone,start,destination, 0));
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
        String address = "http://" + store_from_db.getAddress();

        URL my_final_url = new URL(address + "/delivery/" + delivery_id + "/state/" + state);
        HttpURLConnection con = (HttpURLConnection) my_final_url.openConnection(); // open HTTP connection
        con.setRequestMethod("POST");

        /*if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
            logger.info(state);
        }*/
    }

    // ADMIN STATISTICS

    public int numberDeliveriesMadeForLast24Hours() {
        return 0;
    }

    public List<Integer> numberDeliveriesMadeForLast13Days(){
        return Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0);
    }

    public double averageTimeDeliveries() {
        return 0;
    }

    public double averageRidersScore(){
        return 0.0;
    }

    public List<Rider> getAllRiders(){
        return new ArrayList<Rider>();
    }

    public double sumOfKmCoveredInLast24Hours() {
        // findStateByDescription (completed) andTimestamp (last 24h)
        // findDelivery by id
        // api get (delivery.end) e api get (delivery.start)
        // conta(operacao) qualquer(??) delivery.end - delivery.start
        //double kmsCovered = service.sumOfKmCoveredInLast24Hours();

        //http://dev.virtualearth.net/REST/v1/Routes/{travelMode}?wayPoint.1={wayPoint1}&viaWaypoint.2={viaWaypoint2}&waypoint.3={waypoint3}&wayPoint.n={waypointN}&heading={heading}&optimize={optimize}&avoid={avoid}&distanceBeforeFirstTurn={distanceBeforeFirstTurn}&routeAttributes={routeAttributes}&timeType={timeType}&dateTime={dateTime}&maxSolutions={maxSolutions}&tolerances={tolerances}&distanceUnit={distanceUnit}&key={BingMapsKey}


        //√((x_2-x_1)²+(y_2-y_1)²)
        // 40.6278521,-8.6526136
        // 40.6331731,-8.661682
        //double distance_del1 = Math.sqrt(Math.pow(40.6331731 - 40.6278521) + Math.pow(-8.661682, -8.6526136));
        //assertThat();

        return 0.0;
    }
}