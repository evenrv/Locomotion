package com.example.locomotion.Driving;

import com.example.locomotion.Datatype.ParseInfo;
import com.example.locomotion.Json.RouteFinder;
import com.segway.robot.sdk.locomotion.sbv.Base;

public class Elevator {

    public void useElevator(Base mBase, ParseInfo parseInfo){
        //Loomo starts following a person into the elevator.
        mBase.setControlMode(Base.CONTROL_MODE_FOLLOW_TARGET);
        RouteFinder routeFinder = new RouteFinder();
        //When button is pressed: Routefinder.findRoute(parseInfo);
    }
}
