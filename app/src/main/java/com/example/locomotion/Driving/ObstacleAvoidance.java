package com.example.locomotion.Driving;

import android.content.Context;

import com.segway.robot.algo.Pose2D;
import com.segway.robot.algo.minicontroller.CheckPoint;
import com.segway.robot.algo.minicontroller.CheckPointStateListener;
import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.locomotion.head.Head;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.perception.sensor.Sensor;
import com.segway.robot.sdk.perception.sensor.SensorData;
import com.segway.robot.algo.minicontroller.CheckPointStateListener;
import com.segway.robot.sdk.vision.Vision;
import com.segway.robot.sdk.vision.stream.StreamInfo;
import com.segway.robot.sdk.voice.Speaker;

import java.util.Arrays;

public class ObstacleAvoidance{


    //Basic function for driving around the obstacle on the left side
    private void goLeft(Base mBase, Sensor mSensor, Head mHead, Pose2D pose2D, float currentx, float currenty, float currentTheta){

        mBase.addCheckPoint(currentx,currenty + 0.7f);
        mBase.addCheckPoint(currentx + 1f, currenty + 0.7f);
        mBase.addCheckPoint(currentx + 1f, currenty);

        //Need to wait with getting the coordinates.
        currentx = pose2D.getX();
        currenty = pose2D.getY();
        currentTheta = pose2D.getTheta();

        mBase.addCheckPoint(currentx, currenty, (float) (currentTheta + Math.PI / 2));
    }




    //Deciding what to do to avoid the obstacle
    public void avoid(Base mBase, Sensor mSensor, Head mHead, Pose2D pose2D) {
    mBase.setControlMode(Base.CONTROL_MODE_NAVIGATION);

        SensorData mUltrasonicData = mSensor.querySensorData(Arrays.
                asList(Sensor.ULTRASONIC_BODY)).get(0);
        float mUltrasonicDistance = mUltrasonicData.getIntData()[0];
        System.out.println("-------------Distance: " + mUltrasonicDistance + "-------------");



        //Fetching the current position
        float currentx = pose2D.getX();
        float currenty = pose2D.getY();
        float currentTheta = pose2D.getTheta();

        System.out.println(currentx);
        System.out.println(currenty);
        System.out.println(currentTheta);


        //Checking left side
        float checkLeftAngle = (float) (currentTheta - Math.PI / 4);
        boolean checkingLeft = true;
        mBase.addCheckPoint(0, 0, checkLeftAngle);

        while (checkingLeft){

            SensorData mUltrasonicData1 = mSensor.querySensorData(Arrays.
                    asList(Sensor.ULTRASONIC_BODY)).get(0);
            float mUltrasonicDistance1 = mUltrasonicData1.getIntData()[0];

            if (mUltrasonicDistance1 > 1300){
                goLeft(mBase, mSensor, mHead, pose2D, currentx, currenty, currentTheta);
                checkingLeft = false;
            }
        }



        System.out.println("current theta: " + currentTheta);



        /*mHead.setMode(Head.MODE_SMOOTH_TACKING);
        mHead.setPitchAngularVelocity(0.1f);
        mHead.setYawAngularVelocity(0.1f);
        mHead.setWorldPitch((float) Math.PI);
        mHead.setWorldYaw((float) Math.PI);*/
    }
}

