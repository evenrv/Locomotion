package com.example.locomotion.Tools;
import com.example.locomotion.Datatype.ParseInfo;
import java.util.ArrayList;
public class Point_converter {


    public double[][] convert(ArrayList<Double[]> checkPoints, Double[] CiscoCoords, ParseInfo parseInfo){

        float metersperlongitude = parseInfo.mPerLong;
        float metersperlatitude = parseInfo.mPerLat;
        float angle = parseInfo.angle;

        double[] xcoords = new double[checkPoints.size()];
        double[] ycoords = new double[checkPoints.size()];


        for (int position = 0;position< checkPoints.size(); position++) {

            //Creating a new point, which will be the same point as fetched from the url,
            // but from loomos local reference frame. These points are also multiplied with
            // metersperLatitude and metersperlongitude so that they're unit will be in meters.


            Double[] singleLocalCheckpoint = {
                    (checkPoints.get(position)[0] - CiscoCoords[0]) * metersperlongitude,
                    (checkPoints.get(position)[1] - CiscoCoords[1]) * metersperlatitude
            };

            //substituting the global point with the local point
            checkPoints.set(position, singleLocalCheckpoint);
        }



        //Transforming the points with matrix multiplication. The output coordinates will
        // be from Loomos reference frame, but transformed, so that the difference in orientation
        // is taken care of

        for (int position = 0;position< checkPoints.size(); position++) {

            Double[] singleTransformedCheckpoint = new Double [2];


            singleTransformedCheckpoint[0] =

                    Math.cos(angle)*checkPoints.get(position)[0] -Math.sin(angle)*checkPoints.get(position)[1];

            singleTransformedCheckpoint[1]= Math.sin(angle)*checkPoints.get(position)[0]
                    +Math.cos(angle)*checkPoints.get(position)[1];

            checkPoints.set(position, singleTransformedCheckpoint);

            xcoords[position] =  singleTransformedCheckpoint[0];
            ycoords[position] =  singleTransformedCheckpoint[1];
        }

        double[][] output = {xcoords,ycoords};
        return output;
    }
}