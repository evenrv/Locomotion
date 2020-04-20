package com.example.locomotion.Tools;

import com.example.locomotion.R;

import java.util.ArrayList;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Point_converter {

    final double grad = 111319.99;



    //Converting global coordinates in degrees to global coordinates in metres
    //by multiplying every element in the list by grad = 111319.99

    public double[][] convert(ArrayList<Double[]> checkPoints, Double[] CiscoCoords, float angle) {
        double[] xcoords = new double[checkPoints.size()];
        double[] ycoords = new double[checkPoints.size()];


        for (int position = 0;position< checkPoints.size(); position++) {


            //Creating a new point, which will be the same point as fetched from the url,
            // but from loomos local reference frame. These points are also multiplied with grad,
            // so that they're unit will be in meters.

            Double[] singleLocalCheckpoint = {
                    (checkPoints.get(position)[0] - CiscoCoords[0]) * grad,
                    (checkPoints.get(position)[1] - CiscoCoords[1]) * grad
            };

            //substituting the global point with the local point
            checkPoints.set(position, singleLocalCheckpoint);

            System.out.println("x fra loomo uten transformasjon: " + checkPoints.get(position)[0]);
            System.out.println("y fra loomo uten transformasjon: " + checkPoints.get(position)[1]);

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


            System.out.println("final x: " + xcoords[position]);
            System.out.println("final y: " + ycoords[position]);

        }

        double[][] output = {xcoords,ycoords};
        return output;
    }
}