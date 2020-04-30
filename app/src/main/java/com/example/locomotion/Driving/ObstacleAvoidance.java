package com.example.locomotion.Driving;

import android.content.Context;

import com.segway.robot.algo.Pose2D;
import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.locomotion.head.Head;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.perception.sensor.Sensor;
import com.segway.robot.sdk.perception.sensor.SensorData;
import com.segway.robot.sdk.vision.Vision;
import com.segway.robot.sdk.vision.stream.StreamInfo;
import com.segway.robot.sdk.voice.Speaker;

import java.util.Arrays;

public class ObstacleAvoidance{


    private void goRight(){


    }

    private void goLeft(){


    }


    public void avoid(Base mBase, Sensor mSensor, Head mHead, Pose2D pose2D, boolean obstacle){

        while (obstacle) {

            SensorData mUltrasonicData = mSensor.querySensorData(Arrays.
                    asList(Sensor.ULTRASONIC_BODY)).get(0);
            float mUltrasonicDistance = mUltrasonicData.getIntData()[0];


            //Checking right side
            float currentx = pose2D.getX();
            float currenty = pose2D.getY();
            float currentTheta = pose2D.getTheta();
            mBase.addCheckPoint(currentx, currenty, (float) (currentTheta + Math.PI / 4));


            if (mUltrasonicDistance > 1300){
                goRight();
                obstacle = false;
            }

            //Checking left side
            pose2D.getTheta();
            mBase.addCheckPoint(pose2D.getX(), pose2D.getY(), (float) (pose2D.getTheta() + -Math.PI / 4));

            if (mUltrasonicDistance < 1300){
                goLeft();
                obstacle = false;
            }
        }


        /*mHead.setMode(Head.MODE_SMOOTH_TACKING);
        mHead.setPitchAngularVelocity(0.1f);
        mHead.setYawAngularVelocity(0.1f);
        mHead.setWorldPitch((float) Math.PI);
        mHead.setWorldYaw((float) Math.PI);*/
    }
}
