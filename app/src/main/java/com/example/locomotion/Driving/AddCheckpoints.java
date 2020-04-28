package com.example.locomotion.Driving;

import com.segway.robot.algo.Pose2D;
import com.segway.robot.algo.minicontroller.CheckPoint;
import com.segway.robot.algo.minicontroller.CheckPointStateListener;
import com.segway.robot.sdk.locomotion.head.Head;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.perception.sensor.Sensor;
import com.segway.robot.sdk.perception.sensor.SensorData;

import java.util.Arrays;


public class AddCheckpoints {

    // Creating objects to control the base and the sensors of Loomo. Creates checkpoint object to
    // add checkpoints later, and new_url object to create an  URL.

    private boolean driving;
    private boolean obstacle = false;
    boolean turnLeft;
    private ObstacleAvoidance obstacleAvoidance = new ObstacleAvoidance();


    //The function that adds checkpoints for Loomo
    public void drive(Base mBase, Sensor mSensor, Head mHead, double[][] output){

        mBase.setControlMode(Base.CONTROL_MODE_NAVIGATION);
        mBase.setLinearVelocity(0.4f);

        double[] coordx = output[0];
        double[] coordy = output[1];


        //Activating mBase to control Loomo
        mBase.cleanOriginalPoint();
        Pose2D pose2D = mBase.getOdometryPose(-1);
        mBase.setOriginalPoint(pose2D);



        //Iterating through both coordinate-lists and adding one checkpoint each iteration.
        //"driving" is set to true each iteration
        //i starts at 1 because the first point in the array is the position of Loomo
        for (int i = 1; i < coordx.length; ++i) {


            //x and y are the coordinates that will be used in mBase.addcheckPoint.
            /*float x = (float) coordx[i];
            float y = (float) coordy[i];*/

            float x = (float) 2.0;
            float y = (float) 0;
            mBase.addCheckPoint(x, y);

            driving = true;




            //Everything happening while Loomo is driving will be in this while loop.
            while (driving) {

                //Fetching the ultrasonic distance
                SensorData mUltrasonicData = mSensor.querySensorData(Arrays.
                        asList(Sensor.ULTRASONIC_BODY)).get(0);
                float mUltrasonicDistance = mUltrasonicData.getIntData()[0];


                //Fetching distance on left and right side with IR sensors to determine the
                // distance to the walls on the left- and right side.


                SensorData mInfraredData = mSensor.querySensorData(Arrays.asList(Sensor.INFRARED_BODY)).get(0);
                float mInfraredDistanceLeft = mInfraredData.getIntData()[0];
                float mInfraredDistanceRight = mInfraredData.getIntData()[1];

                if (mInfraredDistanceRight < 1300 && mInfraredDistanceRight < mInfraredDistanceLeft ){


                    //How much Loomo will turn each iteration
                    y += 0.001f;
                    mBase.clearCheckPointsAndStop();
                    mBase.addCheckPoint(x,y);
                    System.out.println("NY Y: " + y);
                }




                System.out.println("left: " + mInfraredDistanceLeft);
                System.out.println("Right: " + mInfraredDistanceRight);





                //This function checks if loomo has reached its current checkpoint.
                //If it has, then driving = false, and a new iteration will start in the fort loop
                mBase.setOnCheckPointArrivedListener(new CheckPointStateListener() {

                    @Override
                    public void onCheckPointArrived(CheckPoint checkPoint, Pose2D realPose, boolean isLast) {

                        driving = false;

                    }

                    @Override
                    public void onCheckPointMiss(CheckPoint checkPoint, Pose2D realPose, boolean isLast, int reason) {
                    //driving = false?
                    }
                });


                 if (mUltrasonicDistance < 400)
                {
                    mBase.clearCheckPointsAndStop();
                    obstacle = true;
                    driving = false;
                }
            }

            //Loomo needs to break the current for-loop, so that it doesn't follow the list, as it
            // would be wrong after avoiding a checkpoint. Therefore, we need to find a new route to
            // theRoom, and Start this function again.

            if (obstacle)

            {
                obstacleAvoidance.avoid(mBase, mSensor, mHead);
                break;
            }
        }
    }
}