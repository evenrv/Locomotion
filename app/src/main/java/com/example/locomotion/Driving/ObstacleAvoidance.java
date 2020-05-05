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

        //Driving to the left
        mBase.addCheckPoint(currentx,currenty + 0.7f);
        //Driving straight forward 1 meter
        mBase.addCheckPoint(currentx + 1f, currenty + 0.7f);
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
        float checkLeftAngle = (float) (currentTheta - Math.PI / 4);
        mBase.addCheckPoint(0, 0, checkLeftAngle);
        boolean checkingLeft = true;

        while (checkingLeft){

            SensorData mUltrasonicData = mSensor.querySensorData(Arrays.
                    asList(Sensor.ULTRASONIC_BODY)).get(0);
            float mUltrasonicDistance = mUltrasonicData.getIntData()[0];

            if (mUltrasonicDistance > 1300){
                goLeft(mBase, mSensor, mHead, pose2D, currentx, currenty, currentTheta);
                checkingLeft = false;
            }
        }



        /*mHead.setMode(Head.MODE_SMOOTH_TACKING);
        mHead.setPitchAngularVelocity(0.1f);
        mHead.setYawAngularVelocity(0.1f);
        mHead.setWorldPitch((float) Math.PI);
        mHead.setWorldYaw((float) Math.PI);*/
    }
}

