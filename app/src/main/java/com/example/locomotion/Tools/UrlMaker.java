package com.example.locomotion.Tools;

public class UrlMaker {

    /* URL_template = "https://api.mazemap.com/routing/path/?srid=4326&hc=true&sourcelat<FLOAT>=&sourcelon=<FLOAT>&targetlat=<FLOAT>&targetlon=<FLOAT>&sourcez=<INT>&targetz=<INT>&lang=en&distanceunitstype=metric&mode=PEDESTRIAN"; */




    //Inserting starting longitude,latitude, floor and destination longitude, latitude, and floor
    // to the MazeMap api-url.
    //String.format puts variables anywhere where there's a $s. The function returns a url path.

    public String makeUrl(Double[] currentCoords, Double[] destCoords, double destFloor) {

        double lon1 = currentCoords[0];
        double lat1 = currentCoords[1];
        double lon2 = destCoords[0];
        double lat2 = destCoords[1];
        double z1 = currentCoords[2];
        double z2 = destFloor;

        String urlPath = String.format("https://api.mazemap.com/routing/path/?srid=4326&hc=true&sourcelat=%s&sourcelon=%s&targetlat=%s&targetlon=%s&sourcez=%s&targetz=%s&lang=en&distanceunitstype=metric&mode=PEDESTRIAN", lat1,lon1,lat2,lon2,z1,z2);
        System.out.println(urlPath);
        return urlPath;
    }
}
