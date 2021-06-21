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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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

    public void updateAdmin(String session, String first_name, String last_name, String email, String password, String position, String about) throws AdminNotFoundException, Exception {
        Admin logged_in_admin = adminRepository.findAdminByEmail(session);
        if (logged_in_admin == null) {
            throw new AdminNotFoundException("Admin not found.");
        }
        if(!first_name.equals(""))
            logged_in_admin.setFirst_name(first_name);
        if(!last_name.equals(""))
            logged_in_admin.setLast_name(last_name);
        if(!email.equals(""))
            logged_in_admin.setEmail(email);
        if(!password.equals("")){
            PasswordEncryption enc = new PasswordEncryption();
            logged_in_admin.setPassword(enc.encrypt(password));
        }
        if(!position.equals(""))
            logged_in_admin.setPosition(position);
        if(!about.equals(""))
            logged_in_admin.setDescription(about);

        adminRepository.save(logged_in_admin);
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
            Rider rider = riderRepository.findRiderById(Integer.parseInt(riderID));
            x.setState(state);

            if(state.equals("completed")){
                long now = System.currentTimeMillis();
                long days_to_go_back = TimeUnit.DAYS.toMillis(Calendar.DAY_OF_MONTH - 1);
                long hours_to_go_back = TimeUnit.HOURS.toMillis(Calendar.HOUR_OF_DAY);

                int salary = 0;
                double score = 0.0;

                x.setScore(ThreadLocalRandom.current().nextInt(0, 6));

                List<State> thisMonthDeliveries = stateRepository.findStatesByDescriptionAndTimestampBetween(
                        "completed", new Timestamp(now - days_to_go_back - hours_to_go_back), new Timestamp(now));

                int counter = 0;
                for(State s : thisMonthDeliveries){
                    Delivery thisdelivery = deliveryRepository.findDeliveryById(s.getDelivery());
                    if(thisdelivery.getRider() == (Integer.parseInt(riderID))){
                        counter += 1;
                        int delivery_fee = thisdelivery.getRider_fee();
                        salary += delivery_fee;
                        int delivery_score = thisdelivery.getScore();
                        score += delivery_score;
                    }
                }
                rider.setSalary(Double.valueOf(salary));
                rider.setScore(score/counter);
            }

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
        return stateRepository.findStatesByDescriptionAndTimestampBetween(
                "completed", new Timestamp(currentTime - 86400000), new Timestamp(currentTime)).size();
    }

    public int personalDeliveriesMadeForLast24Hours(int id){
        long currentTime = System.currentTimeMillis();
        int counter = 0;
        List<State> completedDeliveriesLast24Hours = stateRepository.findStatesByDescriptionAndTimestampBetween(
                "completed", new Timestamp(currentTime - 86400000), new Timestamp(currentTime));
        for(State state : completedDeliveriesLast24Hours){
            Delivery delivery = deliveryRepository.findDeliveryById(state.getDelivery());
            if(delivery.getRider() == id)
                counter += 1;
        }
        return counter;
    }

    public List<Integer> numberDeliveriesMadeForLast13Days(){
        List<Integer> allDeliveriesOfLast13Days = Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0);
        long stoptime = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(Calendar.HOUR_OF_DAY);;
        long starttime = stoptime - TimeUnit.DAYS.toMillis(1);

        for(int i=0; i<13; i++){
            allDeliveriesOfLast13Days.set(i, stateRepository.findStatesByDescriptionAndTimestampBetween(
                    "completed", new Timestamp(starttime), new Timestamp(stoptime)).size());

            starttime -= TimeUnit.DAYS.toMillis(1);
            stoptime -= TimeUnit.DAYS.toMillis(1);
        }
        System.out.println("numberDeliveriesMadeForLast13Days: " +  allDeliveriesOfLast13Days);
        return allDeliveriesOfLast13Days;
    }

    public List<Integer> personalDeliveriesMadeForLast13Days(int id){
        List<Integer> personalDeliveriesOfLast13Days = Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0);

        long stoptime = System.currentTimeMillis();
        long starttime = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1);

        for(int i=0; i<13; i++){
            List<State> allDeliveriesOfThatDay = stateRepository.findStatesByDescriptionAndTimestampBetween(
                    "completed", new Timestamp(starttime), new Timestamp(stoptime));

            for(State state: allDeliveriesOfThatDay){
                Delivery delivery = deliveryRepository.findDeliveryById(state.getDelivery());

                if(delivery.getRider() == id)
                    personalDeliveriesOfLast13Days.set(i, personalDeliveriesOfLast13Days.get(i) + 1);
            }

            starttime -= TimeUnit.DAYS.toMillis(1);
            stoptime -= TimeUnit.DAYS.toMillis(1);
        }
        System.out.println("personalDeliveriesMadeForLast13Days: " +  personalDeliveriesOfLast13Days);
        return personalDeliveriesOfLast13Days;
    }

    public double averageTimeDeliveries() {
        Long totalTime = Long.valueOf(0);
        List<Long> listOfTimes = new ArrayList<>() ;
        long currentTime = System.currentTimeMillis();
        List<State> completedDeliveriesLast24Hours = stateRepository.findStatesByDescriptionAndTimestampBetween(
                "completed", new Timestamp(
                        currentTime - TimeUnit.DAYS.toMillis(1)), new Timestamp(currentTime));
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

    public double personalAverageTimeDeliveries(int id){
        Long totalTime = Long.valueOf(0);
        List<Long> listOfTimes = new ArrayList<>() ;
        long currentTime = System.currentTimeMillis();
        List<State> completedDeliveriesLast24Hours = stateRepository.findStatesByDescriptionAndTimestampBetween(
                "completed", new Timestamp(
                        currentTime - TimeUnit.DAYS.toMillis(1)), new Timestamp(currentTime));
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
        long stoptime = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(Calendar.HOUR_OF_DAY);
        long starttime = stoptime - TimeUnit.DAYS.toMillis(1);

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
        // 1 millisecond = 1.66666667 × 10-5 minutes
        for(int i=0; i<13; i++){
            if (numberOfDeliveriesOfLast13Days.get(i) == 0.0) {
                sumTimesOfDeliveriesOfLast13Days.set(i, 0.0);
            } else {
                sumTimesOfDeliveriesOfLast13Days.set(i, (sumTimesOfDeliveriesOfLast13Days.get(i)/numberOfDeliveriesOfLast13Days.get(i))/1000/60);
            }
        }
        System.out.println("averageDeliveryTimeForLast13Days: " +  sumTimesOfDeliveriesOfLast13Days);
        return sumTimesOfDeliveriesOfLast13Days;
    }

    public List<Double> personalAverageDeliveryTimeForLast13Days(int id){
        List<Integer> numberOfDeliveriesOfLast13Days = Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0);
        List<Double> sumTimesOfDeliveriesOfLast13Days = Arrays.asList(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0);
        long stoptime = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(Calendar.HOUR_OF_DAY);;
        long starttime = stoptime - TimeUnit.DAYS.toMillis(1);

        for(int i=0; i<13; i++){
            List<State> deliveriesOfThatDay = stateRepository.findStatesByDescriptionAndTimestampBetween(
                    "completed", new Timestamp(starttime), new Timestamp(stoptime));

            for(State state : deliveriesOfThatDay){
                Delivery delivery = deliveryRepository.findDeliveryById(state.getDelivery());
                if(delivery.getRider() == id){
                    long accepted_time = stateRepository.findStateByDeliveryAndDescription(
                            state.getDelivery(), "accepted").getTimestamp().getTime();
                    long completed_time = state.getTimestamp().getTime();
                    sumTimesOfDeliveriesOfLast13Days.set(i, sumTimesOfDeliveriesOfLast13Days.get(i) + Double.valueOf(completed_time-accepted_time));
                    numberOfDeliveriesOfLast13Days.set(i, numberOfDeliveriesOfLast13Days.get(i) + 1);
                }
            }
            starttime -= TimeUnit.DAYS.toMillis(1);
            stoptime -= TimeUnit.DAYS.toMillis(1);
        }

        for(int i=0; i<13; i++){
            if (numberOfDeliveriesOfLast13Days.get(i) == 0) {
                sumTimesOfDeliveriesOfLast13Days.set(i, 0.0);
            } else {
                sumTimesOfDeliveriesOfLast13Days.set(i, sumTimesOfDeliveriesOfLast13Days.get(i)/numberOfDeliveriesOfLast13Days.get(i)/1000/60);
            }
        }
        System.out.println("personalAverageDeliveryTimeForLast13Days: " +  sumTimesOfDeliveriesOfLast13Days);
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

    public double personalScore(int id){
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
        return totalDistance;
    }

    public List<Double> sumOfKmCoveredForLast13Days() {
        List<Double> sumKmsCoveredOfLast13Days = Arrays.asList(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0);
        long stoptime = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(Calendar.HOUR_OF_DAY);;
        long starttime = stoptime - TimeUnit.DAYS.toMillis(1);

        for(int i=0; i<13; i++) {
            List<State> deliveriesOfThatDay = stateRepository.findStatesByDescriptionAndTimestampBetween(
                    "completed", new Timestamp(starttime), new Timestamp(stoptime));

            double totalDistance = 0;
            for(State state : deliveriesOfThatDay) {
                Delivery foundDelivery = deliveryRepository.findDeliveryById(state.getDelivery());
                String departure = foundDelivery.getStart();
                String destination = foundDelivery.getDestination();
                double distance = geocoder.getDistanceInKmsBetweenTwoAddressesWithExternalApi(departure, destination);
                totalDistance += distance;
            }
            sumKmsCoveredOfLast13Days.set(i, totalDistance);
            starttime -= TimeUnit.DAYS.toMillis(1);
            stoptime -= TimeUnit.DAYS.toMillis(1);
        }
        System.out.println("personalAverageDeliveryTimeForLast13Days: " +  sumTimesOfDeliveriesOfLast13Days);
        return sumKmsCoveredOfLast13Days;
    }

    public List<Double> personalsumOfKmCoveredForLast13Days(int id) {
        List<Double> sumKmsCoveredOfLast13Days = Arrays.asList(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0);
        long stoptime = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(Calendar.HOUR_OF_DAY);;
        long starttime = stoptime - TimeUnit.DAYS.toMillis(1);

        for(int i=0; i<13; i++) {
            List<State> deliveriesOfThatDay = stateRepository.findStatesByDescriptionAndTimestampBetween(
                    "completed", new Timestamp(starttime), new Timestamp(stoptime));

            double totalDistance = 0;
            for(State state : deliveriesOfThatDay) {
                Delivery foundDelivery = deliveryRepository.findDeliveryById(state.getDelivery());
                if (foundDelivery.getRider() == id) {
                    String departure = foundDelivery.getStart();
                    String destination = foundDelivery.getDestination();
                    double distance = geocoder.getDistanceInKmsBetweenTwoAddressesWithExternalApi(departure, destination);
                    totalDistance += distance;
                }

            }
            sumKmsCoveredOfLast13Days.set(i, totalDistance);
            starttime -= TimeUnit.DAYS.toMillis(1);
            stoptime -= TimeUnit.DAYS.toMillis(1);
        }
        System.out.println("personalsumOfKmCoveredForLast13Days: " + sumKmsCoveredOfLast13Days);
        return sumKmsCoveredOfLast13Days;
    }

    public double personalSumOfKmCoveredInLast24Hours(int id) {
        long currentTime = System.currentTimeMillis();
        List<State> listOfCompletedStates = stateRepository.findStatesByDescriptionAndTimestampBetween(
                "completed", new Timestamp(currentTime - TimeUnit.DAYS.toMillis(1)),
                new Timestamp(currentTime));
        double totalDistance = 0;
        for (int i = 0; i<listOfCompletedStates.size(); i++) {
            State iteratedState = listOfCompletedStates.get(i);
            Delivery foundDelivery = deliveryRepository.findDeliveryById(iteratedState.getDelivery());
            if(foundDelivery.getRider() == id){
                String departure = foundDelivery.getStart();
                String destination = foundDelivery.getDestination();
                double distance = geocoder.getDistanceInKmsBetweenTwoAddressesWithExternalApi(departure, destination);
                totalDistance += distance;
            }
        }
        return totalDistance;
    }


    public Map<Integer, String> allRidersNamesByDeliveries(List<Delivery> mydeliveries){
        Map<Integer,String> ridersNames = new HashMap<>();
        for(Delivery delivery: mydeliveries){
            Rider deliveryRider = riderRepository.findRiderById(delivery.getRider());
            String riderName = deliveryRider.getFirstname() + " " + deliveryRider.getLastname();
            ridersNames.put(delivery.getId(), riderName);
        }
        return ridersNames;
    }

    public Map<Integer, String> allWaitingTimesByDeliveries(List<Delivery> mydeliveries){
        Map<Integer,String> waitingTimes = new HashMap<>();
        for(Delivery delivery: mydeliveries){
            long completedTime = stateRepository.findStateByDeliveryAndDescription(
                    delivery.getId(), "completed").getTimestamp().getTime();
            long acceptedTime = stateRepository.findStateByDeliveryAndDescription(
                    delivery.getId(), "accepted").getTimestamp().getTime();

            waitingTimes.put(delivery.getId(), new SimpleDateFormat("HH:mm:ss").format(new Timestamp(completedTime - acceptedTime)));
        }
        return waitingTimes;
    }
}