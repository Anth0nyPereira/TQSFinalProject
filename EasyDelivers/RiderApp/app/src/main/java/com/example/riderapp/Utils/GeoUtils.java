package com.example.riderapp.Utils;

import com.google.android.gms.maps.model.LatLng;

public class GeoUtils {
    public static double distanceBetween2Points(LatLng coords1, LatLng coords2) {

        double lat1 = coords1.latitude;
        double lon1 = coords1.longitude;
        double lat2 = coords2.latitude;
        double lon2 = coords2.longitude;

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }
}
