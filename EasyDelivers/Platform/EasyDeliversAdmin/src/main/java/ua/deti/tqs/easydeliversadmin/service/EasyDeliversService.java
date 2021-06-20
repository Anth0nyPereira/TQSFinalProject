package ua.deti.tqs.easydeliversadmin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;
import ua.deti.tqs.easydeliversadmin.component.Geocoder;
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
import java.util.concurrent.TimeUnit;
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

    @Autowired
    Geocoder geocoder;

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
        long currentTime = System.currentTimeMillis();
        List<State> completedDeliveriesLast24Hours = stateRepository.findStatesByDescriptionAndTimestampBetween(
                "completed", new Timestamp(currentTime - 86400000), new Timestamp(currentTime));
        for(int i=0; i<completedDeliveriesLast24Hours.size();i++){
            System.out.println(completedDeliveriesLast24Hours.get(i));
        }
        return completedDeliveriesLast24Hours.size();
    }

    public List<Integer> numberDeliveriesMadeForLast13Days(){
        List<Integer> allDeliveriesOfLast13Days = Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0);
        long stoptime = System.currentTimeMillis();
        long starttime = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1);

        for(int i=0; i<13; i++){
            allDeliveriesOfLast13Days.set(i, stateRepository.findStatesByDescriptionAndTimestampBetween(
                    "completed", new Timestamp(starttime), new Timestamp(stoptime)).size());

            starttime -= TimeUnit.DAYS.toMillis(1);
            stoptime -= TimeUnit.DAYS.toMillis(1);
        }
        return allDeliveriesOfLast13Days;
    }

    public double averageTimeDeliveries() {
        Long totalTime = Long.valueOf(0);
        List<Long> listOfTimes = new ArrayList<>() ;
        long currentTime = System.currentTimeMillis();
        List<State> completedDeliveriesLast24Hours = stateRepository.findStatesByDescriptionAndTimestampBetween(
                "completed", new Timestamp(currentTime - TimeUnit.DAYS.toMillis(1)), new Timestamp(currentTime));
        for(State state : completedDeliveriesLast24Hours){
            long accepted_time = stateRepository.findStateByDeliveryAndDescription(
                    state.getDelivery(), "accepted").getTimestamp().getTime();
            long completed_time = state.getTimestamp().getTime();
            long iteration_result = completed_time - accepted_time;
            listOfTimes.add(iteration_result);
            totalTime += iteration_result;
        }
        if(listOfTimes.size() > 0)
            return TimeUnit.MILLISECONDS.toMinutes(totalTime  / listOfTimes.size());
        else
            return 0.0;
    }


    public List<Double> averageDeliveryTimeForLast13Days(){
        List<Integer> numberOfDeliveriesOfLast13Days = Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0);
        List<Double> sumTimesOfDeliveriesOfLast13Days = Arrays.asList(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0);
        long stoptime = System.currentTimeMillis();
        long starttime = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1);

        for(int i=0; i<13; i++){
            List<State> deliveriesOfThatDay = stateRepository.findStatesByDescriptionAndTimestampBetween(
                    "completed", new Timestamp(starttime), new Timestamp(stoptime));

            for(State state : deliveriesOfThatDay){
                long accepted_time = stateRepository.findStateByDeliveryAndDescription(
                        state.getDelivery(), "accepted").getTimestamp().getTime();
                long completed_time = state.getTimestamp().getTime();
                sumTimesOfDeliveriesOfLast13Days.set(i, sumTimesOfDeliveriesOfLast13Days.get(i) + Double.valueOf(completed_time-accepted_time));
                numberOfDeliveriesOfLast13Days.set(i, numberOfDeliveriesOfLast13Days.get(i) + 1);
            }
            starttime -= TimeUnit.DAYS.toMillis(1);
            stoptime -= TimeUnit.DAYS.toMillis(1);
        }

        for(int i=0; i<13; i++){
            sumTimesOfDeliveriesOfLast13Days.set(i, sumTimesOfDeliveriesOfLast13Days.get(i)/numberOfDeliveriesOfLast13Days.get(i));
        }
        return sumTimesOfDeliveriesOfLast13Days;
    }

    public double averageRidersScore(){
        double sumOfScores = Double.valueOf(0);
        List<Delivery> allDeliveries = deliveryRepository.findDeliveriesByState("completed");
        for(Delivery delivery : allDeliveries){
            sumOfScores += delivery.getScore();
        }
        if(allDeliveries.size() > 0)
            return sumOfScores / allDeliveries.size();
        else
            return 0.0;
    }

    public List<Rider> getAllRiders(){
        return riderRepository.findAll();
    }

    public List<Rider> getTopRiders(){
        List<Rider> allRiders = getAllRiders();
        if(allRiders.size() <= 4)
            return allRiders;
        else
            return allRiders.subList(allRiders.size() -3, allRiders.size());
    }

    public List<Delivery> getAllCompletedDeliveries() {
        return deliveryRepository.findDeliveriesByState("completed");
    }

    public double sumOfKmCoveredInLast24Hours() {
        long currentTime = System.currentTimeMillis();
        // findStateByDescription (completed) andTimestamp (last 24h)
        List<State> listOfCompletedStates = stateRepository.findStatesByDescriptionAndTimestampBetween("completed", new Timestamp(currentTime - TimeUnit.DAYS.toMillis(1)), new Timestamp(currentTime));

        // findDelivery by id
        double totalDistance = 0;
        for (int i = 0; i<listOfCompletedStates.size(); i++) {
            State iteratedState = listOfCompletedStates.get(i);
            System.out.println(iteratedState);
            Delivery foundDelivery = deliveryRepository.findDeliveryById(iteratedState.getDelivery());
            String departure = foundDelivery.getStart();
            String destination = foundDelivery.getDestination();
            double distance = geocoder.getDistanceInKmsBetweenTwoAddressesWithExternalApi(departure, destination);
            totalDistance += distance;
        }
        // api get (delivery.end) e api get (delivery.start)
        // conta(operacao) qualquer(??) delivery.end - delivery.start
        //double kmsCovered = service.sumOfKmCoveredInLast24Hours();

        //http://dev.virtualearth.net/REST/v1/Routes/{travelMode}?wayPoint.1={wayPoint1}&viaWaypoint.2={viaWaypoint2}&waypoint.3={waypoint3}&wayPoint.n={waypointN}&heading={heading}&optimize={optimize}&avoid={avoid}&distanceBeforeFirstTurn={distanceBeforeFirstTurn}&routeAttributes={routeAttributes}&timeType={timeType}&dateTime={dateTime}&maxSolutions={maxSolutions}&tolerances={tolerances}&distanceUnit={distanceUnit}&key={BingMapsKey}


        //√((x_2-x_1)²+(y_2-y_1)²)
        // 40.6278521,-8.6526136
        // 40.6331731,-8.661682
        //double distance_del1 = Math.sqrt(Math.pow(40.6331731 - 40.6278521) + Math.pow(-8.661682, -8.6526136));
        //assertThat();

        return totalDistance;
    }
}