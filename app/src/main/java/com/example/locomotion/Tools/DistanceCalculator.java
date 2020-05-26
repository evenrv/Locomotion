package com.example.locomotion.Tools;

public class DistanceCalculator {
    double phi1;
    double phi2;
    double delta_phi;
    double delta_lambda;
    double a;
    double c;
    double meters;
    double R;


    public float calculateDistance(double lon1, double lat1, double lon2, double lat2){

        R = 6371000;

        phi1 = Math.toRadians(lat1);
        phi2 = Math.toRadians(lat2);

        delta_phi = Math.toRadians(lat2 - lat1);
        delta_lambda = Math.toRadians(lon2 - lon1);

        a = Math.pow( Math.sin(delta_phi / 2.0), 2) + + Math.cos(phi1) * Math.cos(phi2) * Math.pow(Math.sin(delta_lambda / 2.0), 2);

        c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        meters = R * c;  //output distance in meters

        System.out.println(meters);

        return 1;
    }
}

//Source: https://community.esri.com/groups/coordinate-reference-systems/blog/2017/10/05/haversine-formula
