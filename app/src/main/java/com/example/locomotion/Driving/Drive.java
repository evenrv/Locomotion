package com.example.locomotion.Driving;

import com.example.locomotion.Datatype.ParseInfo;
import com.segway.robot.algo.Pose2D;
import com.segway.robot.algo.minicontroller.CheckPoint;
import com.segway.robot.algo.minicontroller.CheckPointStateListener;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.perception.sensor.Sensor;
import com.segway.robot.sdk.perception.sensor.SensorData;
import java.util.Arrays;

public class Drive {

    // Creating objects to control the base and the sensors of Loomo. Creates checkpoint object to
    // add checkpoints later, and new_url object to create an  URL.

    private boolean driving;
    private boolean obstacle = false;
    private ObstacleAvoidance obstacleAvoidance = new ObstacleAvoidance();
    private float correctedY;
    private float rotationAngle;
    private int timerValue = 0;


    //The function that adds checkpoints for Loomo
    public void drive(final Base mBase, Sensor mSensor, ParseInfo parseInfo){


        mBase.setControlMode(Base.CONTROL_MODE_NAVIGATION);

        double[] coordx = parseInfo.xcoords;
        double[] coordy = parseInfo.ycoords;


        //Activating mBase to control Loomo
        mBase.cleanOriginalPoint();
        Pose2D pose2D = mBase.getOdometryPose(-1);
        mBase.setOriginalPoint(pose2D);

        //Iterating through both coordinate-lists and adding one checkpoint each iteration.
        //"driving" is set to true each iteration
        //i starts at 1 because the first point in the array is the position of Loomo
        for (int i = 1; i < coordx.length; ++i) {


            //x and y are the coordinates that will be used in mBase.addcheckPoint.
            float x = (float) coordx[i];
            float y = (float) coordy[i];


            mBase.addCheckPoint(x, y);
            correctedY = y;
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


                //In each iteration, if the right infrared sensor is closer than 1250 mm from a
                //wall, an increment will be added to the y coordinate.
                //(y is positive to the left while looking forward).
                if (mInfraredDistanceRight < 1250 && mInfraredDistanceRight < mInfraredDistanceLeft ){


                    //How much Loomo will turn each iteration
                    correctedY += 0.0005f;
                    float rotationAngle;

                    //(x - currentX) is the distance to the point in x-direction
                    //(correctedY - currentY) is the distance to the point in y-direction

                    float currentX = pose2D.getX();
                    float currentY = pose2D.getY();

                    rotationAngle = (float)( (180/Math.PI) * Math.atan((correctedY - currentY) / (x - currentX)) );
                    mBase.addCheckPoint(x,correctedY);
                    System.out.println("--------  " + "NEW y: " + correctedY + "With this rotation angle: " + rotationAngle + " degrees  --------");
                }

                //If the left sensor is less than 1250 mm away from a wall, then a decrement will
                //be subtracted from y. This will make loomo turn right.
                if (mInfraredDistanceLeft < 1250 && mInfraredDistanceLeft < mInfraredDistanceRight ){


                    //How much Loomo will turn each iteration
                    correctedY -= 0.0005f;


                    //(x - currentX) is the distance to the point in x-direction
                    //(correctedY - currentY) is the distance to the point in y-direction

                    float currentX = pose2D.getX();
                    float currentY = pose2D.getY();

                    rotationAngle = (float)( (180/Math.PI) * Math.atan((correctedY - currentY) / (x - currentX)) );
                    mBase.addCheckPoint(x,correctedY);
                    System.out.println("--------  " + "NEW y: " + correctedY + "With this rotation angle: " + rotationAngle + " degrees  --------");
                }

                //Printing the distances.
                System.out.println("left: " + mInfraredDistanceLeft);
                System.out.println("Right: " + mInfraredDistanceRight);


                //This function checks if loomo has reached its current checkpoint.
                //If it has, then driving = false, and a new iteration will start in the for loop.
                mBase.setOnCheckPointArrivedListener(new CheckPointStateListener() {

                    @Override
                    public void onCheckPointArrived(CheckPoint checkPoint, Pose2D realPose, boolean isLast) {

                        //Check if this checkpoint is a elevator checkpoint

                        /*if (elevator at (x,y)){
                        
                        Elevator elevator = new Elevator(parseInfo);

                        elevator.useElevator(mBase);

                        }
                        */

                        driving = false;
                    }

                    @Override
                    public void onCheckPointMiss(CheckPoint checkPoint, Pose2D realPose, boolean isLast, int reason) {

                    }
                });


                //The ultrasonic sensor looks for obstacles in the front of Loomo. If an obstacle
                //is detected less than 900mm away from Loomo, then Loomo will stop. It will wait
                //for 3 seconds before either continuing the route, or exiting the while(driving)
                //loop, and try to avoid the obstacle.
                //TimerValue counts iteration, and 3500 iterations gives a fair time for the
                //obstacle to move.
                // It is also possible to use Loomos camera to detect what the obstacle might be.
                // If it is a human, for instance, Loomo could tell it to move out if its path.
                 if (mUltrasonicDistance < 900){

                     mBase.clearCheckPointsAndStop();
                     boolean waitForObstacle = true;
                     timerValue = 0;

                    while(waitForObstacle){
                        SensorData mUltrasonicData1 = mSensor.querySensorData(Arrays.
                                asList(Sensor.ULTRASONIC_BODY)).get(0);
                        float mUltrasonicDistance1 = mUltrasonicData1.getIntData()[0];

                        System.out.println("mUltrasonicDistance1:  " + mUltrasonicDistance1);
                        timerValue++;
                        System.out.println("Timer value: " + "----------------" + timerValue);

                        //If the obstacle moves, Loomo will add a checkpoint at the previous
                        //checkpoint ( x, correctedY )
                        if(mUltrasonicDistance1 > 900){
                            waitForObstacle = false;
                            mBase.addCheckPoint(x,correctedY);
                        }

                        else if (timerValue == 3500){
                            obstacle = true;
                            driving = false;
                            waitForObstacle = false;
                        }
                    }

                     if (obstacle) {
                         System.out.println("Starting avoidance function");
                         obstacleAvoidance.avoid(mBase, mSensor);
                         mBase.clearCheckPointsAndStop();
                         mBase.addCheckPoint(x,correctedY);
                     }
                }
            }
        }
    }
}