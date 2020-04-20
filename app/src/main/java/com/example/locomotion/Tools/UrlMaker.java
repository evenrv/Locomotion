package com.example.locomotion.Tools;

public class UrlMaker {

    /* URL_template = "https://api.mazemap.com/routing/path/?srid=4326&hc=true&sourcelat<FLOAT>=&sourcelon=<FLOAT>&targetlat=<FLOAT>&targetlon=<FLOAT>&sourcez=<INT>&targetz=<INT>&lang=en&distanceunitstype=metric&mode=PEDESTRIAN"; */





    public String makeUrl(Double[] currentCoords, double currentFloor, Double[] destCoords, double destFloor) {

        double lon1 = currentCoords[0];
        double lat1 = currentCoords[1];
        double lon2 = destCoords[0];
        double lat2 = destCoords[1];
        double z1 = currentFloor;
        double z2 = destFloor;

        String urlPath = String.format("https://api.mazemap.com/routing/path/?srid=4326&hc=true&sourcelat=%s&sourcelon=%s&targetlat=%s&targetlon=%s&sourcez=%s&targetz=%s&lang=en&distanceunitstype=metric&mode=PEDESTRIAN", lat1,lon1,lat2,lon2,z1,z2);
        System.out.println(urlPath);
        return urlPath;
    }
}
