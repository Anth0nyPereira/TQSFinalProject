package ua.deti.tqs.easydeliversadmin.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.deti.tqs.easydeliversadmin.component.Geocoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GeocoderUnitTest {

    @Autowired
    private Geocoder geocoder;

    @Test
    public void checkDistanceTest() {
        double expectedDistance = 38.739;
        assertEquals(expectedDistance, geocoder.getDistanceInKmsBetweenTwoAddressesWithExternalApi("Rua Carvalho de Baixo", "Universidade de Aveiro"));
    }

}
