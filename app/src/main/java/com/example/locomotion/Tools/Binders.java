package com.example.locomotion.Tools;

import android.content.Context;

import com.example.locomotion.MainActivity;
import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.locomotion.head.Head;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.perception.sensor.Sensor;
import com.segway.robot.sdk.vision.Vision;

public class Binders {
    //initializing the sensor instance and binding the service
    public Sensor mSensor;
    public Base mBase;
    public Head mHead;
    public Vision mVision;
    public Context context;


    // Called at startup
    public void bindAll(){
        bindBase();
        bindHead();
        bindSensor();
        bindVision();
    }

    public void bindBase(){
        //initializing the Base instance and binding the service
        mBase.bindService(context, new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {

            }

            @Override
            public void onUnbind(String reason) {

            }
        });
    }

    public void bindSensor(){
        mSensor.bindService(context, new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {

            }

            @Override
            public void onUnbind(String reason) {

            }
        });
    }


    public void bindHead(){
        mHead = Head.getInstance();
        mHead.bindService(context, new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {

            }

            @Override
            public void onUnbind(String reason) {

            }
        });
    }


    public void bindVision(){

        mVision = Vision.getInstance();
        mVision.bindService(context, new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {

            }

            @Override
            public void onUnbind(String reason) {

            }
        });
    }



}


//TODO jeg la inn alle bindere her fra MainActivity