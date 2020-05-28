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

    private boolean avoiding;
    private boolean goingLeft = false;
    private float avoidanceDist = 1;        //Length of one of the vectors used to drive
                                               // around an obstacle
    float checkLeftAngle;



    //Basic function for driving around the obstacle on the left side
    private void goLeft(Base mBase, float currentx, float currenty, float currentTheta){

        //Step one-three calculates the half-rectangular-route Loomo takes around an obstacle.
        //The x-value and the y-value together make up a vector from the current position, to the
        //left. The multiplicationfactor "1" in the first two equation represents how far left Loomo
        //will drive. In the second equation pair, The multiplicationfactor "2" will represent how
        //far forward Loomo will drive, and in the third two equations, the multiplicationfactor
        // will determin how far right Loomo will drive to go back to its path.
        float stepOneX   = currentx + (float) cos(currentTheta + PI/2)*avoidanceDist;
        float stepOneY   =  currenty + (float) sin(currentTheta + PI/2)*avoidanceDist;
       
        float stepTwoX   = stepOneX + (float) cos(currentTheta)*2*avoidanceDist;
        float stepTwoY   =  stepOneY + (float) sin(currentTheta)*2*avoidanceDist;
        
        float stepThreeX = stepTwoX + (float) cos(currentTheta - PI/2)*avoidanceDist;
        float stepThreeY =  stepTwoY + (float) sin(currentTheta - PI/2)*avoidanceDist;


        //Float[][] array contains the route to avoid the obstacle.
        float[][] avoidanceList = {{stepOneX, stepOneY},
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

        //The same formulas as in the goLeft()-function, but step one and step three are switched,
        //so Loomo drives around on the right side instead.
        float stepOneX  = currentx + (float) cos(currentTheta - PI/2)*avoidanceDist;
        float stepOneY = currenty + (float) sin(currentTheta - PI/2)*avoidanceDist;

        float stepTwoX  = stepOneX + (float) cos(currentTheta)*2*avoidanceDist;
        float stepTwoY = stepOneY + (float) sin(currentTheta)*2*avoidanceDist;

        float stepThreeX  = stepTwoX + (float) cos(currentTheta + PI/2)*avoidanceDist;
        float stepThreeY = stepTwoY + (float) sin(currentTheta + PI/2)*avoidanceDist;


        //Float[][] array contains the route to avoid the obstacle.
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


        //Checking left side by rotating with an angle of PI/2 from the current angle.
        //addCheckpoint is the method that rotates loomo
        //checkingLeft is true while Loomo rotates, and waits while timer counts to 2000
        //If the ultrasonic distance is more than 900, the goLeft function will be called to drive
        // around the obstacle. goingLeft decides whether if Loomo will check the right side after
        //checking the left side.

        checkLeftAngle = (float) (currentTheta + Math.PI / 2);
        mBase.addCheckPoint(currentx, currenty, checkLeftAngle);
        boolean checkingLeft = true;
        long startTime = System.currentTimeMillis();


        while (checkingLeft){


            SensorData mUltrasonicData = mSensor.querySensorData(Arrays.
                    asList(Sensor.ULTRASONIC_BODY)).get(0);
            float mUltrasonicDistance = mUltrasonicData.getIntData()[0];

            if (mUltrasonicDistance > 1000){
                goLeft(mBase, currentx, currenty, currentTheta);
                checkingLeft = false;
                goingLeft = true;
            }

            //If the difference in system time is more than 3 seconds, then that means 3 seconds
            //have passed.
            else if (System.currentTimeMillis() - startTime > 3000 ){
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
            float checkRightAngle = (float) (currentTheta - Math.PI / 2);
            mBase.addCheckPoint(currentx, currenty, checkRightAngle);
            boolean checkingRight = true;
            startTime = System.currentTimeMillis();

            while (checkingRight){

                SensorData mUltrasonicData = mSensor.querySensorData(Arrays.
                        asList(Sensor.ULTRASONIC_BODY)).get(0);
                float mUltrasonicDistance = mUltrasonicData.getIntData()[0];

                if (mUltrasonicDistance > 900){
                    goRight(mBase, currentx, currenty, currentTheta);
                    checkingRight = false;
                }

                else if (System.currentTimeMillis() - startTime > 3000){
                    System.out.println("Not possible to drive around. Calculate new route...");
                }
            }
        }
    }
}