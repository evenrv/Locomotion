package com.example.locomotion.Tools;

import com.segway.robot.algo.Pose2D;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.perception.sensor.Sensor;
import com.segway.robot.sdk.perception.sensor.SensorData;

import java.util.Arrays;

import static java.lang.Math.cos;
import static java.lang.StrictMath.PI;
import static java.lang.StrictMath.acos;
import static java.lang.StrictMath.sqrt;


public class Calibrate {

    private DistanceCalculator distanceCalculator = new DistanceCalculator();



    //This function will calibrate Loomos local coordinate-system with the global coordinate system.
    //It returns a float, which will be the degree that the Loomo has to rotate with, in order to
    //align the coordinate systems.


    public float[]  calibrate(Base mBase, Sensor mSensor) {
        final double metersPerLatitude;
        final double metersPerLongitude;



        //Fetching the cisco position for the robot, which returns
        float ciscoLong = (float) 8.57617285865058;
        float ciscoLat = (float)  58.33447638466072;


        metersPerLongitude = distanceCalculator.calculateDistance(ciscoLong, ciscoLat, ciscoLong + 1.0, ciscoLat);

        metersPerLatitude= distanceCalculator.calculateDistance(ciscoLong, ciscoLat, ciscoLong, ciscoLat + 1.0);


        float originCiscoPositionx = ciscoLong * (float) metersPerLongitude;
        float originCiscoPositiony = ciscoLat * (float) metersPerLatitude;


        //Driving 1 meter forward test
         //mBase.addCheckPoint(1, 0);


        //GetCiscoPosition()
        //These are the actual coordinates of x2 and y2 in latitude and longitude.



        //If Loomo was already headed east, we would expect these coordinates in meters:
        float expectedCiscoPositionx2 = (originCiscoPositionx + 1);

        float newCiscoPointlon = (float) 8.57617413279317;
        float newCiscoPointlat = (float) 58.334468102336245;


        float actualCiscoPositionx2 = newCiscoPointlon * (float) metersPerLongitude;
        float actualCiscoPositiony2 = newCiscoPointlat * (float) metersPerLatitude;



        //Defining 3 vectors that make up a triangle abc
        double[] aVector = {expectedCiscoPositionx2 - originCiscoPositionx, originCiscoPositiony - originCiscoPositiony};
        double[] bVector = {actualCiscoPositionx2 - originCiscoPositionx, actualCiscoPositiony2 - originCiscoPositiony};
        double[] cVector = {expectedCiscoPositionx2 - actualCiscoPositionx2, originCiscoPositiony - actualCiscoPositiony2};



        //Calculating the length of each vector / side of the triangle with the pythagorean therm.
        double aVectorLengthSquared = Math.pow(aVector[0], 2.0) + Math.pow(aVector[1], 2.0);
        double bVectorLengthSquared = Math.pow(bVector[0], 2.0) + Math.pow(bVector[1], 2.0);
        double cVectorLengthSquared = Math.pow(cVector[0], 2.0) + Math.pow(cVector[1], 2.0);

        System.out.println("Length a = " + sqrt(aVectorLengthSquared));
        System.out.println("Length b = " + sqrt(bVectorLengthSquared));
        System.out.println("Length c = " + sqrt(cVectorLengthSquared));
        System.out.println(" ");

        //Calculating the angle between the vector from the expected points and the vector from the
        // real points using the cosine law. This gives us an angle ∈ [0,PI].
        float angle = (float) acos((aVectorLengthSquared + bVectorLengthSquared - cVectorLengthSquared)
                / (2 * sqrt(aVectorLengthSquared) * sqrt(bVectorLengthSquared)));



        //If the y-element of the b-vector is negative, that means loomo has been driving
        // southwards. This means, if we are to rotate the coordinates to fit loomo,
        // they will need to be rotated in the positive direction, or against the clock. -> angle = angle
        // If the y-element is positive, then loomo has been driving north, and the coordinates will
        // need to be rotated with the clock. Then "angle" will be the negative value
        // found with the cosine law.

        if (bVector[1] < 0) {
            angle = angle;
        } else if (bVector[1] > 0) {
            angle = -angle;
        }

        System.out.println("The angle between the reference systems are: " + angle + ", which equals " +  angle / PI + "*PI\n" );

        mBase.cleanOriginalPoint();

        //Setting Fetching the original point where Loomo stands.
        mBase.cleanOriginalPoint();
        Pose2D pose2D = mBase.getOdometryPose(-1);
        mBase.setOriginalPoint(pose2D);

        //Driving North: 	58.334512491588754, 8.576253530218082 to 58.33452608839093, 8.576253888068521
        //Driving East:     58.33452608839093, 8.576253888068521 to 58.33452618178464, 8.576285073219395
        //Driving West:    58.33452618178464, 8.576285073219395 to 58.33452693486112, 8.576258173706947
        //Driving South:     58.33452693486112, 8.576258173706947 to 58.33451544969185, 8.576258890746544

        float[] calibInfo = {angle, (float) metersPerLongitude, (float) metersPerLatitude};
        return calibInfo;
    }
}
