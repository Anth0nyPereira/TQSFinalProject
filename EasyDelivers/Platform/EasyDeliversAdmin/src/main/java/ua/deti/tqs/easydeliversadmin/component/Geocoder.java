package ua.deti.tqs.easydeliversadmin.component;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

@Component
public class Geocoder {

    @Value("${apiKey}") // my API KEY DONT SEE IT PLS :P
    private String apiKey;

    private static RestTemplate restTemplate = new RestTemplate();

    public double getDistanceBetweenTwoAddressesWithExternalApi(String departure, String destination) throws JSONException {
        RestTemplate restTemplate = getRestTemplate();
        String apiResult = restTemplate.getForObject("https://api.distancematrix.ai/maps/api/distancematrix/json?origins=" + departure + "&destinations=" + destination + "&key=" + apiKey, String.class);
        JSONObject json = new JSONObject(apiResult);
        JSONArray rowsArray = json.getJSONArray("rows");
        JSONObject rowsObject = rowsArray.getJSONObject(0);
        JSONArray elementsArray = rowsObject.getJSONArray("elements");
        JSONObject distanceObject = elementsArray.getJSONObject(0);
        JSONObject valuesObject = distanceObject.getJSONObject("distance");
        double distance = valuesObject.getDouble("value");

        return distance;
    }

    public static RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
