package com.example.locomotion.Tools;

// class used to take time, output is in milliseconds
// e.f.
public class SystemTimer {
    public void takeTime(long timeLimit){
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;
        while(elapsedTime<timeLimit){
            elapsedTime = System.currentTimeMillis() - startTime;
        }
    }
}
