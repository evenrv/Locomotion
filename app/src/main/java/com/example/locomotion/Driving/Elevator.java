package com.example.locomotion.Driving;

import com.example.locomotion.Datatype.ParseInfo;
import com.example.locomotion.Json.RouteFinder;
import com.example.locomotion.R;
import com.segway.robot.sdk.locomotion.sbv.Base;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

public class Elevator extends AppCompatActivity{
    public void  resumeNavigation(ParseInfo parseInfo){

    }

    public void useElevator(Base mBase, ParseInfo parseInfo) throws ExecutionException, InterruptedException {

        mBase.clearCheckPointsAndStop();
        mBase.cleanOriginalPoint();
        //mBase.setOriginalPoint(pose2D);

        //Loomo starts following a person into the elevator.
        mBase.setControlMode(Base.CONTROL_MODE_FOLLOW_TARGET);
        parseInfo = new RouteFinder().execute(parseInfo).get();

        setContentView(R.layout.activity_main);
        //When button is pressed: Routefinder.findRoute(parseInfo);


        resumeNavigation(parseInfo);
    }
}
