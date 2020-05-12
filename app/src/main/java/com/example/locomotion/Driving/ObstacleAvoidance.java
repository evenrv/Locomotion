package com.example.locomotion.Driving;

import com.segway.robot.algo.Pose2D;
import com.segway.robot.sdk.locomotion.head.Head;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.perception.sensor.Sensor;
import com.segway.robot.sdk.perception.sensor.SensorData;


import java.util.Arrays;

public class ObstacleAvoidance{


    //Basic function for driving around the obstacle on the left side
    private void goLeft(Base mBase, float currentx, float currenty, float currentTheta){

        //Driving to the left
        mBase.addCheckPoint(currentx,currenty + 0.7f);
        //Driving straight forward 1 meter
        mBase.addCheckPoint(currentx + 1f, currenty + 0.7f);
        //Driving back to the original track, but 1 meter forward, and hopefully past the obstacle
        mBase.addCheckPoint(currentx + 1f, currenty);
    }

    //Basic function for driving around the obstacle on the left side
    private void goRight(Base mBase, float currentx, float currenty, float currentTheta){

        //Driving to the left
        mBase.addCheckPoint(currentx,currenty - 0.7f);
        //Driving straight forward 1 meter
        mBase.addCheckPoint(currentx + 1f, currenty - 0.7f);
        //Driving back to the original track, but 1 meter forward, and hopefully past the obstacle
        mBase.addCheckPoint(currentx + 1f, currenty);
    }


    //Deciding what to do to avoid the obstacle
    public void avoid(Base mBase, Sensor mSensor, Head mHead, Pose2D pose2D) {
    mBase.setControlMode(Base.CONTROL_MODE_NAVIGATION);

        //Fetching the current position
        float currentx = pose2D.getX();
        float currenty = pose2D.getY();
        float currentTheta = pose2D.getTheta();

        System.out.println("currentx: " + currentx);
        System.out.println("currentx: " + currenty);
        System.out.println("currentTheta" + currentTheta);


        //Checking left side
        float checkLeftAngle = (float) (currentTheta - Math.PI / 2);
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
        }
    }
}