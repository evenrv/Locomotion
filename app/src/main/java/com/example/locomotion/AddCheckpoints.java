package com.example.locomotion;

import com.segway.robot.algo.Pose2D;
import com.segway.robot.algo.minicontroller.CheckPoint;
import com.segway.robot.algo.minicontroller.CheckPointStateListener;
import com.segway.robot.algo.minicontroller.ObstacleStateChangedListener;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.locomotion.sbv.LinearVelocity;
import com.segway.robot.sdk.perception.sensor.Sensor;
import com.segway.robot.sdk.perception.sensor.SensorData;

import java.util.Arrays;

public class AddCheckpoints {

    // Creating objects to control the base and the sensors of Loomo. Creates checkpoint object to
    // add checkpoints later, and new_url object to create an  URL.

    private boolean driving;
    private boolean obstacle = false;
    float sum_x;
    float sum_y;
    float mUltrasonicDistance;
    float mLinearVelocity;
    ObstacleAvoidance obstacleAvoidance = new ObstacleAvoidance();


    //The function that adds checkpoints for Loomo
    public void add(Base mBase, Sensor mSensor, double[][] output) {

        double[] coordx = output[0];
        double[] coordy = output[1];


        //Activating mBase to control Loomo
        mBase.cleanOriginalPoint();
        Pose2D pose2D = mBase.getOdometryPose(-1);
        mBase.setOriginalPoint(pose2D);

        float linearVelocity = 3;
        mBase.setLinearVelocity(linearVelocity);



        //Iterating through both coordinate-lists and adding one checkpoint each iteration.
        //"driving" is set to true each iteration
        //i starts at 1 because the first point in the array is the position of Loomo
        for (int i = 1; i < coordx.length; ++i) {


            //x and y are the coordinates that will be used in mBase.addcheckPoint.
            float x = (float) coordx[i];
            float y = (float) coordy[i];
            mBase.addCheckPoint(x, y);

            driving = true;




            //While "driving" is true, check if
            //linear velocity < 0.1.
            // If true, then driving is false, and the program will exit the loop, and do another
            // iteration in the for-loop.
            while (driving) {


                SensorData mUltrasonicData = mSensor.querySensorData(Arrays.
                        asList(Sensor.ULTRASONIC_BODY)).get(0);
                mUltrasonicDistance = mUltrasonicData.getIntData()[0]; //Fetches the distance in a loop


                SensorData mPose2DData = mSensor.querySensorData(Arrays.asList(Sensor.POSE_2D)).get(0);
                Pose2D pose2D1 = mSensor.sensorDataToPose2D(mPose2DData);
                mLinearVelocity = pose2D1.getLinearVelocity();


                //This function checks if loomo has reached its current checkpoint.
                //If it has, then driving = false, and a new iteration will start in the fort loop
                mBase.setOnCheckPointArrivedListener(new CheckPointStateListener() {

                    @Override
                    public void onCheckPointArrived(CheckPoint checkPoint, Pose2D realPose, boolean isLast) {

                        driving = false;

                    }

                    @Override
                    public void onCheckPointMiss(CheckPoint checkPoint, Pose2D realPose, boolean isLast, int reason) {

                    }
                });

                if (mUltrasonicDistance < 800)
                {
                    mBase.clearCheckPointsAndStop();
                    obstacle = true;
                    driving = false;
                    obstacleAvoidance.avoid(mBase);
                }
            }

            if (obstacle)                   //Loomo needs to break the current for-loop,
                                            // so that it doesn't follow the list,
            {                               //as it would be wrong after avoiding a checkpoint
                break;                      //Therefore, we need to find a new route to theRoom, and
            }                               //Start this function again.
        }
    }
}
