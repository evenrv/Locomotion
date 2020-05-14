package com.example.locomotion.Driving;

import com.segway.robot.algo.Pose2D;
import com.segway.robot.algo.minicontroller.CheckPoint;
import com.segway.robot.algo.minicontroller.CheckPointStateListener;
import com.segway.robot.sdk.locomotion.head.Head;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.perception.sensor.Sensor;
import com.segway.robot.sdk.perception.sensor.SensorData;

import java.util.Arrays;

public class ObstacleAvoidance{

    boolean avoiding;
    private float currentx;
    private float currenty;
    private float currentTheta;
    private float avoidanceListLeft[][] = {  {currentx, currenty + 0.7f},
                                             {currentx + 2, currenty + 0.7f},
                                             {currentx + 2, currenty}
                                       };

    private float avoidanceListRight[][] = {  {currentx, currenty - 0.7f},
                                              {currentx + 2, currenty - 0.7f},
                                              {currentx + 2, currenty}
    };


    //Basic function for driving around the obstacle on the left side
    private void goLeft(Base mBase){


        for (int point = 0; point < 3; point++) {
            mBase.addCheckPoint(avoidanceListLeft[point][0], avoidanceListLeft[point][1]);


            avoiding = true;

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

    //Basic function for driving around the obstacle on the ritght side
    private void goRight(Base mBase){
        for (int point = 0; point < 3; point++) {
            mBase.addCheckPoint(avoidanceListRight[point][0], avoidanceListRight[point][1]);


            avoiding = true;

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


    //Deciding what to do to avoid the obstacle
    public void avoid(Base mBase, Sensor mSensor, Head mHead, Pose2D pose2D) {
    mBase.setControlMode(Base.CONTROL_MODE_NAVIGATION);

        Pose2D pose2D1 = mBase.getOdometryPose(-1);

        //Fetching the current position
        currentx = pose2D1.getX();
        currenty = pose2D1.getY();
        currentTheta = pose2D1.getTheta();

        System.out.println("currentx: " + currentx);
        System.out.println("currenty: " + currenty);
        System.out.println("currentTheta" + currentTheta);

        goLeft(mBase);



        //Checking left side

        /*float checkLeftAngle = (float) (currentTheta - Math.PI / 2);
        mBase.addCheckPoint(0, 0, checkLeftAngle);
        boolean checkingLeft = true;
        int timer = 0;


        while (checkingLeft){
            timer = timer++;

            SensorData mUltrasonicData = mSensor.querySensorData(Arrays.
                    asList(Sensor.ULTRASONIC_BODY)).get(0);
            float mUltrasonicDistance = mUltrasonicData.getIntData()[0];

            if (mUltrasonicDistance > 1300){
                goLeft(mBase, currentx, currenty, currentTheta);
                checkingLeft = false;
            }

            else if (timer == 2000){
                System.out.println("Not possible to go left.");
                checkingLeft = false;
            }
        }*/
    }
}