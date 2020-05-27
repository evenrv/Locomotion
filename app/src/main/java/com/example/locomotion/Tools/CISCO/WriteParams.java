package com.example.locomotion.Tools.CISCO;


import com.segway.robot.algo.Pose2D;
import com.segway.robot.algo.PoseVLS;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.perception.sensor.RobotTotalInfo;
import com.segway.robot.sdk.perception.sensor.Sensor;

public class WriteParams {
    public Base mBase;
    public Sensor mSensor;


    public void writeValues(Base mBase, Sensor mSensor, float x_value, float y_value, float angle) {

        Pose2D odomPose;
        PoseVLS poseVLS;
        RobotTotalInfo robotTotalInfo;
        Pose2D basePose;

        odomPose= mBase.getOdometryPose(-1);
        poseVLS = mBase.getVLSPose(-1);
        robotTotalInfo =  mSensor.getRobotTotalInfo();
        basePose = robotTotalInfo.getPose2D();

        System.out.println("VLS = " + poseVLS.getX() + ", " + poseVLS.getY()+ ", "+poseVLS.getTheta() + ", " +poseVLS.getTimestamp());
        System.out.println("Odom = " + odomPose.getX() + ", " + odomPose.getY()+ ", "+odomPose.getTheta() + ", " +odomPose.getTimestamp());
        System.out.println("IMU = " + basePose.getX() + ", " + basePose.getY()+ ", "+basePose.getTheta() + ", " +basePose.getTimestamp());


    }
}