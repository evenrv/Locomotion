package com.example.locomotion;

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


    //This function will calibrate Loomos local coordinate-system with the global coordinate system.
    //It returns a float, which will be the degree that the Loomo has to rotate with, in order to
    //align the coordinate systems.




    float  calibrate(Base mBase, Sensor mSensor) {


        SensorData mPose2DData = mSensor.querySensorData(Arrays.asList(Sensor.POSE_2D)).get(0);
        Pose2D pose2D1 = mSensor.sensorDataToPose2D(mPose2DData);


        //Fetching the cisco position for the robot, which returns
        // CiscoPositionDegx and CiscoPositionDegy

        //GetCiscoPosition()


        float ciscoLong = (float) 8.576258173706947;
        float ciscoLat = (float) 58.33452693486112;


        // calculating meters per degree longitude in longitude and latitude directions.
        final double metersPerLongitude =  111319.488;

        //((PI/180)*OriginCiscoPositiony) is for converting degrees to radians.
        final double metersPerLatitude =   metersPerLongitude * cos(((PI/180)*ciscoLat));

        System.out.println("metersPerLatitude: " + metersPerLatitude + "metersperlongitude: " + metersPerLongitude);


        float OriginCiscoPositionx =  ciscoLong * (float) metersPerLongitude;
        float OriginCiscoPositiony =  ciscoLat * (float) metersPerLatitude;


        //If Loomo was already headed east, we would expect these coordinatesafter
        float ExpectedCiscoPositionx2 = (OriginCiscoPositionx + 1);
        float ExpectedCiscoPositiony2 = (((OriginCiscoPositiony)));


        //Driving 1 meter forward test
         //mBase.addCheckPoint(1, 0);


        //GetCiscoPosition()
        //These are the actual coordinates of x2 and y2 in latitude and longitude.

        float newCiscoPointlon = (float) 8.576258890746544;
        float newCiscoPointlat = (float) 58.33451544969185;

        float ActualCiscoPositionx2 =  newCiscoPointlon * (float) metersPerLongitude;
        float ActualCiscoPositiony2 = newCiscoPointlat  * (float) metersPerLatitude;




        //Defining 3 vectors that make up a triangle abc
        double[] aVector = {ExpectedCiscoPositionx2 - OriginCiscoPositionx, ExpectedCiscoPositiony2 - OriginCiscoPositiony};
        double[] bVector = {ActualCiscoPositionx2 - OriginCiscoPositionx, ActualCiscoPositiony2 - OriginCiscoPositiony};
        double[] cVector = {ExpectedCiscoPositionx2 - ActualCiscoPositionx2, ExpectedCiscoPositiony2 - ActualCiscoPositiony2};

        //Calculating the length of each vector / side of the triangle with the pythagorean therm.
        double aVectorLengthSquared = Math.pow(aVector[0], 2.0) + Math.pow(aVector[1], 2.0);
        double bVectorLengthSquared = Math.pow(bVector[0], 2.0) + Math.pow(bVector[1], 2.0);
        double cVectorLengthSquared = Math.pow(cVector[0], 2.0) + Math.pow(cVector[1], 2.0);

        System.out.println("Length a = " + sqrt(aVectorLengthSquared));
        System.out.println("Length b = " + sqrt(bVectorLengthSquared));
        System.out.println("Length c = " + sqrt(cVectorLengthSquared));
        System.out.println(" ");
        System.out.println(bVector[1]);


        //Calculating the angle between the vector from the expected points and the vector from the
        // real points using the cosine law. This gives us an angle ∈ [0,PI].
        float angle = (float) acos((aVectorLengthSquared + bVectorLengthSquared - cVectorLengthSquared)
                / (2 * sqrt(aVectorLengthSquared) * sqrt(bVectorLengthSquared)));



        System.out.println("The angle to rotate: " + angle);
        System.out.println("This angle is" + angle / PI + "*PI");


        //If the y-element of the b-vector is negative, that means loomo has been driving
        // south, to some extent. This means, if we're to rotate the coordinates to fit loomo,
        // they will need to be rotated in the negative direction, or with the clock. -> angle = -angle
        // If the y-element is positive, then loomo has been driving north, and the coordinates will
        // need to be rotated against the clock. Then "angle" will be the positive value
        // found with the cosine law.

        if (bVector[1] < 0) {
            angle = -angle;
        } else if (bVector[1] > 0) {
            angle = angle;
        }

        System.out.println("We will be rotating with: " + angle );
        mBase.cleanOriginalPoint();

        //Kjøreretning mot Nord: 	58.334512491588754 og 8.576253530218082 til 58.33452608839093 og 8.576253888068521
        //Kjøreretning mot Øst:     58.33452608839093 og 8.576253888068521 til 58.33452618178464 og 8.576285073219395
        //Kjøreretning mot Vest:    58.33452618178464 og 8.576285073219395 til 58.33452693486112 og 8.576258173706947
        //Kjøreretning mot Sør:     58.33452693486112 og 8.576258173706947 til 58.33451544969185 og 8.576258890746544

        return angle;
    }
}
