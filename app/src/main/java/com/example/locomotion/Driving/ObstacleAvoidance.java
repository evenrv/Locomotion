package com.example.locomotion.Driving;

import com.segway.robot.algo.Pose2D;
import com.segway.robot.algo.minicontroller.CheckPoint;
import com.segway.robot.algo.minicontroller.CheckPointStateListener;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.perception.sensor.Sensor;
import com.segway.robot.sdk.perception.sensor.SensorData;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.StrictMath.PI;
import java.util.Arrays;

public class ObstacleAvoidance{

    boolean avoiding;
    boolean goingLeft = false;



    //Basic function for driving around the obstacle on the left side
    private void goLeft(Base mBase, float currentx, float currenty, float currentTheta){

        //Step one-three calculates the half-rectangular-route Loomo takes around an obstacle.
        //The x-value and the y-value together make up a vector from the current position, to the
        // left.
        float stepOneX   = currentx + (float) cos(currentTheta + PI/2)*1;
        float stepOneY   =  currenty + (float) sin(currentTheta + PI/2)*1;
       
        float stepTwoX   = stepOneX + (float) cos(currentTheta)*2;
        float stepTwoY   =  stepOneY + (float) sin(currentTheta)*2;
        
        float stepThreeX = stepTwoX + (float) cos(currentTheta - PI/2)*1;
        float stepThreeY =  stepTwoY + (float) sin(currentTheta - PI/2)*1;


        //Float[][] array containing the route to avoid around the obstacle.
        float avoidanceList[][] = {  {stepOneX, stepOneY},
                {stepTwoX, stepTwoY},
                {stepThreeX, stepThreeY}
        };


        //A for-loop adds each checkpoint when the previous one is reached.
        for (int point = 0; point < 3; point++) {
            mBase.addCheckPoint(avoidanceList[point][0], avoidanceList[point][1]);

            avoiding = true;

            //This loop inhibits the program from adding another checkpoint proceeding until Loomo
            //has reached the last checkpoint added in the for loop
            while (avoiding) {


                mBase.setOnCheckPointArrivedListener(new CheckPointStateListener() {
                    @Override
                    public void onCheckPointArrived(CheckPoint checkPoint, Pose2D realPose, boolean isLast) {
                        avoiding = false;
                    }

                    @Override
                    public void onCheckPointMiss(CheckPoint checkPoint, Pose2D realPose, boolean isLast, int reason) {

                    }
                });
            }
        }
    }

    //Basic function for driving around the obstacle on the right side
    private void goRight(Base mBase, float currentx, float currenty, float currentTheta){

        //The same formulas as in the goLeft()-function, but step one and step three are switches,
        //so Loomo drives around on the right side instead.
        float stepOneX  = currentx + (float) cos(currentTheta - PI/2)*1;
        float stepOneY = currenty + (float) sin(currentTheta - PI/2)*1;

        float stepTwoX  = stepOneX + (float) cos(currentTheta)*2;
        float stepTwoY = stepOneY + (float) sin(currentTheta)*2;

        float stepThreeX  = stepTwoX + (float) cos(currentTheta + PI/2)*1;
        float stepThreeY = stepTwoY + (float) sin(currentTheta + PI/2)*1;


        float avoidanceList[][] = {  {stepOneX, stepOneY},
                {stepTwoX, stepTwoY},
                {stepThreeX, stepThreeY}
        };

        //A for-loop adds each checkpoint from the avoidanceList when the previous one is reached.
        for (int point = 0; point < 3; point++) {

            //Adding the current checkpoint
            mBase.addCheckPoint(avoidanceList[point][0], avoidanceList[point][1]);


            //avoiding is set to true, and will be set to false when Loomo arrives at the current
            //checkpoint.
            avoiding = true;


            //This loop inhibits the program from adding another checkpoint proceeding until Loomo
            //has reached the last checkpoint added in the for loop
            while (avoiding) {


                mBase.setOnCheckPointArrivedListener(new CheckPointStateListener() {
                    @Override
                    public void onCheckPointArrived(CheckPoint checkPoint, Pose2D realPose, boolean isLast) {
                        avoiding = false;
                    }

                    @Override
                    public void onCheckPointMiss(CheckPoint checkPoint, Pose2D realPose, boolean isLast, int reason) {

                    }
                });
            }
        }
    }

    //Function for deciding what to do to avoid the obstacle
    public void avoid(Base mBase, Sensor mSensor) {
    mBase.setControlMode(Base.CONTROL_MODE_NAVIGATION);

        //Fetching current pose
        Pose2D pose2D = mBase.getOdometryPose(-1);

        //Fetching the current position
        float currentx = pose2D.getX();
        float currenty = pose2D.getY();
        float currentTheta = pose2D.getTheta();


        //Checking left side by rotating with an angle of PI/4 from the current angle.
        //addCheckpoint is the method that rotates loomo
        //checkingLeft is true while Loomo rotates, and waits while timer counts to 2000
        //If the ultrasonic distance is more than 900, the goLeft function will be called to drive
        // around the obstacle. goingLeft decides whether if Loomo will check the right side after
        //checking the left side.

        float checkLeftAngle = (float) (currentTheta + Math.PI / 4);
        mBase.addCheckPoint(currentx, currenty, checkLeftAngle);
        boolean checkingLeft = true;
        int timer = 0;


        while (checkingLeft){
            timer++;

            SensorData mUltrasonicData = mSensor.querySensorData(Arrays.
                    asList(Sensor.ULTRASONIC_BODY)).get(0);
            float mUltrasonicDistance = mUltrasonicData.getIntData()[0];

            if (mUltrasonicDistance > 1000){
                goLeft(mBase, currentx, currenty, currentTheta);
                checkingLeft = false;
                goingLeft = true;
            }

            else if (timer == 3500){
                System.out.println("Not possible to go left.");
                checkingLeft = false;
                goingLeft = false;
            }
        }


        //If Loomo cannot go left, it willcheck the right side. This part of the function will only
        // be executed if going left is false. It's the same program as for checking the left side.
        //If Loomo cannot find a clear path, it will need to either parse a new route
        //or  ask for help

        if (!goingLeft){
            float checkRightAngle = (float) (currentTheta - Math.PI / 4);
            mBase.addCheckPoint(currentx, currenty, checkRightAngle);
            boolean checkingRight = true;
            timer = 0;

            while (checkingRight){
                timer = timer++;

                SensorData mUltrasonicData = mSensor.querySensorData(Arrays.
                        asList(Sensor.ULTRASONIC_BODY)).get(0);
                float mUltrasonicDistance = mUltrasonicData.getIntData()[0];

                if (mUltrasonicDistance > 900){
                    goRight(mBase, currentx, currenty, currentTheta);
                    checkingRight = false;
                }

                else if (timer == 3500){
                    System.out.println("Not possible to drive around. Calculate new route...");
                }
            }
        }
    }
}