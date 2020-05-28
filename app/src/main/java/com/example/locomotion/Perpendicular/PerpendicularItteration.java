package com.example.locomotion.Perpendicular;


// Class for comparing diverse Odom- and VLS-parameters

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.locomotion.Tools.SystemTimer;
import com.segway.robot.algo.Pose2D;
import com.segway.robot.algo.PoseVLS;
import com.segway.robot.algo.tf.AlgoTfData;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.locomotion.sbv.BasePose;
import com.segway.robot.sdk.perception.sensor.Sensor;
import com.segway.robot.sdk.perception.sensor.SensorData;
import com.segway.robot.sdk.vision.Vision;
import com.segway.robot.sdk.vision.frame.DepthFrameInfo;
import com.segway.robot.sdk.vision.stream.Resolution;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.log;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.abs;

public class PerpendicularItteration {
    public Sensor mSensor;
    public Base mBase;
    float itAngle;
    float dx;
    float dy;
    float angleCal;
    float irLeft;
    float irRight;
    float usDistance;
    float newAngle = 0;
    float x_init;
    float x_prev;
    float dTheta = 0.01f; //[rad]

    float y_init;
    float y_prev;

    float w_z = 10;

    float frameL = 0; // mm
    float frameR = 50;  //mm
    float kathetY;
    float kathetX = frameR - frameL;
    public boolean driving = true;
    private float dt;

    float mBaseYaw;
    float speed;
    float startTime;
    float theta;
    float y_goal;
    float x_goal;

    public void startNavigation(Base mBase, Sensor mSensor, float x_value, float y_value, float angle) throws InterruptedException {
        AlgoTfData Tf;
        mBase.cleanOriginalPoint();
        Pose2D odomPose = mBase.getOdometryPose(-1);
        // Object of timer-class
        SystemTimer systemTimer = new SystemTimer();

        systemTimer.takeTime(1000);

        Pose2D odomPoseStart = mBase.getOdometryPose(-1);
        Pose2D poseOdom = odomPose;
        mBase.setOriginalPoint(odomPoseStart);
        float dx;
        float dy;
        float angleCal;
        systemTimer.takeTime(1000);                                 // wait 3s  after button is pushed

        float x_curr;
        float y_curr;
        float r;

        int r_count = 0;
        float irLeft = mSensor.getInfraredDistance().getLeftDistance();
        float irRight = mSensor.getInfraredDistance().getRightDistance();
        usDistance = mSensor.getUltrasonicDistance().getDistance();
        poseOdom = mBase.getOdometryPose(-1);
        x_init = poseOdom.getX();
        y_init = poseOdom.getY();
        //
        //mBase.setUltrasonicObstacleAvoidanceDistance(0.25f);

        int count = 1;
        // drive forward, until ultrasonicDistance is < 300 mm



        float theta = (float) atan2(frameL - frameR, irLeft - irRight);
        mBase.addCheckPoint(mBase.getOdometryPose(-1).getX(), mBase.getOdometryPose(-1).getY(), mBase.getOdometryPose(-1).getTheta());
        java.util.concurrent.TimeUnit.SECONDS.sleep(5);
        mBase.addCheckPoint(mBase.getOdometryPose(-1).getX(), mBase.getOdometryPose(-1).getY(),mBase.getOdometryPose(-1).getTheta()+0.06f);
        if (irLeft > 1500 && irRight < 1500) {
            mBase.addCheckPoint(mBase.getOdometryPose(-1).getX(), mBase.getOdometryPose(-1).getY(), (float) (Math.PI * 0.5 + mBase.getOdometryPose(-1).getTheta()));
            java.util.concurrent.TimeUnit.SECONDS.sleep(1);
        }
        if (irRight > 1500 && irLeft < 1500) {
            mBase.addCheckPoint(mBase.getOdometryPose(-1).getX(), mBase.getOdometryPose(-1).getY(), (float) (Math.PI * 0.5 + mBase.getOdometryPose(-1).getTheta()));
            java.util.concurrent.TimeUnit.SECONDS.sleep(1);

        }
        while (abs(irLeft - irRight) > 5) {
            irLeft = mSensor.getInfraredDistance().getLeftDistance();
            irRight = mSensor.getInfraredDistance().getRightDistance();
            float currentTheta = mBase.getOdometryPose(-1).getTheta();
            if (currentTheta <0){
                dTheta = dTheta*-1;
            }
            if (irLeft < irRight) {

                mBase.addCheckPoint(mBase.getOdometryPose(-1).getX(), mBase.getOdometryPose(-1).getY(), (float) (dTheta + mBase.getOdometryPose(-1).getTheta()));
                java.util.concurrent.TimeUnit.SECONDS.sleep(1);
                System.out.println("currentAngle = " + mBase.getOdometryPose(-1).getTheta());
            } else {
                mBase.addCheckPoint(mBase.getOdometryPose(-1).getX(), mBase.getOdometryPose(-1).getY(), (float) (-dTheta + mBase.getOdometryPose(-1).getTheta()));
                java.util.concurrent.TimeUnit.SECONDS.sleep(1);
                System.out.println("currentAngle = " + mBase.getOdometryPose(-1).getTheta());

            }
        }

    }
}