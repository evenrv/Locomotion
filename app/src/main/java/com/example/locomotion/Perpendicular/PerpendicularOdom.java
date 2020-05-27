package com.example.locomotion.Perpendicular;

import com.segway.robot.algo.Pose2D;
import com.segway.robot.algo.tf.AlgoTfData;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.perception.sensor.Sensor;
import com.segway.robot.sdk.perception.sensor.SensorData;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.abs;


    public class PerpendicularOdom  {
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

        float y_init;
        float y_prev;

        float w_z = 10;

        float frameL = 0 ; // mm
        float frameR = 50;  //mm
        float kathetY;
        float kathetX = frameR-frameL;
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


            Pose2D odomPoseStart = mBase.getVLSPose(-1);
            Pose2D poseOdom = odomPose;
            mBase.setOriginalPoint(odomPoseStart);
            float dx;
            float dy;
            float angleCal;

            float x_curr;
            float y_curr;
            float r;

            int r_count = 0;
            float irLeft=mSensor.getInfraredDistance().getLeftDistance();
            float irRight=mSensor.getInfraredDistance().getRightDistance();
            usDistance=mSensor.getUltrasonicDistance().getDistance();
            poseOdom = mBase.getOdometryPose(-1);
            x_init = poseOdom.getX();
            y_init = poseOdom.getY();
            //
            //mBase.setUltrasonicObstacleAvoidanceDistance(0.25f);

            int count=1;
            // drive forward, until ultrasonicDistance is < 300 mm
            float x = mBase.getOdometryPose(-1).getX();
            float y = mBase.getOdometryPose(-1).getY();
            float th = mBase.getOdometryPose(-1).getTheta();

            if (usDistance > 500 && irLeft >1000 && irRight> 1000){
                dx = (float) (0.75*(cos(th)));
                dy = (float) (0.75*(sin(th)));
                mBase.addCheckPoint(x+dx,y+dy);

                // find necessary time to wait, assumin
                int wait = (int) floor(Math.pow(10,3)*sqrt(dx*dx+dy*dy)/0.35);

                java.util.concurrent.TimeUnit.SECONDS.sleep(1);

                while(mBase.getOdometryPose(-1).getLinearVelocity()>0.002){
                    // do nothing
                    System.out.println("11111");
                }
            }
            TimeUnit.MILLISECONDS.sleep(2500);
            x = mBase.getOdometryPose(-1).getX();
            y = mBase.getOdometryPose(-1).getY();
            th = mBase.getOdometryPose(-1).getTheta();
            if (usDistance > 500 && irLeft >800 & irRight> 800){
                dx = (float) (0.55*(cos(th)));
                dy = (float) (0.55*(sin(th)));
                mBase.addCheckPoint(x+dx,y+dy);

                // find necessary time to wait, assumin
                int wait = (int) floor(Math.pow(10,3)*sqrt(dx*dx+dy*dy)/0.35);

                java.util.concurrent.TimeUnit.SECONDS.sleep(1);

                while(mBase.getOdometryPose(-1).getLinearVelocity()>0.002){
                    // do nothing
                    System.out.println("11111");
                }
            }
            TimeUnit.MILLISECONDS.sleep(2500);

            x = mBase.getOdometryPose(-1).getX();
            y = mBase.getOdometryPose(-1).getY();
            th = mBase.getOdometryPose(-1).getTheta();
            if (irLeft >700 && irRight> 700){
                dx = (float) (0.45*(cos(th)));
                dy = (float) (0.45*(sin(th)));
                mBase.addCheckPoint(x+dx,y+dy);

                // find necessary time to wait, assumin
                int wait = (int) floor(Math.pow(10,3)*sqrt(dx*dx+dy*dy)/0.35);

                java.util.concurrent.TimeUnit.SECONDS.sleep(1);

                while(mBase.getOdometryPose(-1).getLinearVelocity()>0.002){
                    // do nothing
                    System.out.println("11111");
                }
            }
            TimeUnit.MILLISECONDS.sleep(2500);


            x = mBase.getOdometryPose(-1).getX();
            y = mBase.getOdometryPose(-1).getY();
            th = mBase.getOdometryPose(-1).getTheta();
            if (usDistance > 400 && irLeft >650 && irRight> 650){
                dx = (float) (0.4*(cos(th)));
                dy = (float) (0.4*(sin(th)));
                mBase.addCheckPoint(x+dx,y+dy);

                // find necessary time to wait, assumin
                int wait = (int) floor(Math.pow(10,3)*sqrt(dx*dx+dy*dy)/0.35);

                java.util.concurrent.TimeUnit.SECONDS.sleep(1);

                while(mBase.getOdometryPose(-1).getLinearVelocity()>0.002){
                    // do nothing
                    System.out.println("11111");
                }
            }
            TimeUnit.MILLISECONDS.sleep(2500);


            x = mBase.getOdometryPose(-1).getX();
            y = mBase.getOdometryPose(-1).getY();
            th = mBase.getOdometryPose(-1).getTheta();
            if (irLeft > 500 && irLeft>500){
                dx = (float) (0.25*(cos(th)));
                dy = (float) (0.25*(sin(th)));
                mBase.addCheckPoint(x+dx,dy+y);

                java.util.concurrent.TimeUnit.SECONDS.sleep(1);
                while(mBase.getOdometryPose(-1).getLinearVelocity()>0.002){
                    System.out.println("55555");
                }
            }
            TimeUnit.MILLISECONDS.sleep(500);
            if (usDistance > 300 && irLeft > 400 && irLeft>400){
                dx = (float) (0.25*(cos(th)));
                dy = (float) (0.25*(sin(th)));
                mBase.addCheckPoint(x+dx,y+dy);
                java.util.concurrent.TimeUnit.SECONDS.sleep(1);
                while(mBase.getOdometryPose(-1).getLinearVelocity()>0.002){
                    System.out.println("0000");
                }
            }
            TimeUnit.MILLISECONDS.sleep(500);

            /*


             */
            // now, Loomo is in approximity of an obstacle, and is ready to turn

            irLeft = mSensor.getInfraredDistance().getLeftDistance();
            irRight = mSensor.getInfraredDistance().getRightDistance();
            theta = calAngle(irRight,irLeft);
            // rotate Loomo to be aligned perpendicular to the obstacle



            System.out.println("irLeft= " + irLeft);
            System.out.println("irRight = "+irRight);
            th =mBase.getVLSPose(-1).getTheta();

            // if th is in range [-pi 0]:
            if (th < 0 ){
                th = 2*3.1415f+th;
            }
            if (irRight > irLeft ){
                newAngle = (float) (-(Math.PI*0.5-theta)+th);
            }
            else if(irLeft>irRight){
                // theta is negative here
                newAngle = (float) ((-theta)+th);
            }
            // convert back from full angle
            if (newAngle > Math.PI){
                newAngle = (float) (-2*Math.PI+newAngle);
            }

            mBase.addCheckPoint(mBase.getOdometryPose(-1).getX(),mBase.getOdometryPose(-1).getY(), newAngle);
            java.util.concurrent.TimeUnit.SECONDS.sleep(5);

            System.out.println("theta =  " + theta);
            System.out.println("current angle = " + th);
            System.out.println("new angle  = " + newAngle);

            ;
            //        mBase.addCheckPoint(0,0,-theta);
            //mBase.addCheckPoint(0,0,-theta+th);
        /*
        boolean drivingRotate =true;
        while(abs(irRight - irLeft) >   15){

            // calculate magniutude and direction for rotation
            itAngle = 0.5f*calAngle(irLeft, irRight);
            System.out.println("it_angle = " + itAngle);
            //  mBase.addCheckPoint(poseOdom.getX(), poseOdom.getY(), poseOdom.getTheta()+pi/2);
            mBase.addCheckPoint(0, 0, itAngle);
            while (drivingRotate==true){
                drivingRotate=checkMovement(mSensor,mBase);
            }
        }
         */






        /*

        float  theta = (float) atan2(frameL-frameR,irLeft-irRight);
            mBase.addCheckPoint(poseOdom.getX(), poseOdom.getY(), poseOdom.getTheta()-theta);
        imuPose = mSensor.getRobotAllSensors().getPose2D();
         */

        }

        public  float calAngle(float right, float left){


            // find the y-component of the triangle
            kathetY = right-left;
            kathetX = frameL-frameR;

            // calculate remaining angle to turn, and its direction
            // atan2 calculates the arc-tangent, and specifies quadrant
            return (float) Math.atan2(kathetY,50);
        }



        // one could also use wheelspeeds or getAngularVelocity(), but as the resolution is low, it was determined to use IMU for
        // evaluating the rotation around the z-axis
        public boolean checkMovement(Sensor mSensor, Base mBase){
            SensorData mBaseImu = mSensor.querySensorData(Arrays.asList(Sensor.BASE_IMU)).get(0);
            mBaseYaw = mBaseImu.getFloatData()[2];

            speed =  mBase.getOdometryPose(-1).getLinearVelocity();

            startTime = System.currentTimeMillis();
            boolean timeLim = false;
            int cnt = 0;
            long timeLoop;
            long timeLoopStart;
            while (mBaseYaw == mBaseImu.getFloatData()[2] && timeLim ==false){
                timeLoopStart = System.currentTimeMillis();
                if ((System.currentTimeMillis()-timeLoopStart)>10){
                    System.out.println("in while-loop baseyaw");
                    System.out.println(mBaseYaw);
                }

                // to not get stuck in while-loop, if there is an update on w_z, but the value not is changes
                // because there is not angular velocity
                if( (System.currentTimeMillis()-startTime)>100){
                    System.out.println(System.currentTimeMillis()*Math.pow(10,-6));
                    long diff = (long) (System.currentTimeMillis()-startTime);
                    System.out.println(diff*Math.pow(10,-6));
                    timeLim = true;
                }
                // do nothing, wait for changed value, or expired time-limit
            }

            float theta = mBaseImu.getFloatData()[2];

            // find change in times between updates
            dt = System.currentTimeMillis()-startTime;


            // if angles are in the 3rd or 4th quadrant, convert to full angle
            if (mBaseYaw<0){
                mBaseYaw = 2*3.1415f+mBaseYaw;
            }
            if (theta<0){
                theta = 2*3.1415f+theta;
            }

            // the angular velocity will be:
            w_z = (mBaseYaw-theta)/dt;

            System.out.println("w_z = " + w_z);
            System.out.println("speed = " + speed);
            // if linear-velocity, and angluar velocity around z is small enough, return ture
            if (abs(speed) > 0.001 || abs(w_z) >0.001){
                return true;
            }

            // if conditions not are met
            return  false;
        }

    }