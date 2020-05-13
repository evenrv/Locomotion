package com.example.locomotion.Driving;

import com.segway.robot.sdk.locomotion.sbv.Base;

public class Elevator {

    public void useElevator(Base mBase){
        //Loomo starts following a person into the elevator.
        mBase.setControlMode(Base.CONTROL_MODE_FOLLOW_TARGET);
    }
}
