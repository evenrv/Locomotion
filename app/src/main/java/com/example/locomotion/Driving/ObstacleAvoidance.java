package com.example.locomotion.Driving;

import android.content.Context;

import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.vision.Vision;
import com.segway.robot.sdk.vision.stream.StreamInfo;
import com.segway.robot.sdk.voice.Speaker;

public class ObstacleAvoidance{

    Vision mVision;
    void avoid(Base mBase){
        ServiceBinder.BindStateListener mBindStateListener = new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {

            }

            @Override
            public void onUnbind(String reason) {
            }
        };


        /*Flyt-diagram:
        Roter til høyre. Bruk dybdekamera?
            - Hvis det ikke er hindringer, start KJØR.
            - Hvis ikke, snu til venstre.
                - Hvis det er hindringer her, spørr om hjelp.
                - Hvis ikke det er hindringer, start KJØR.
        KJØR: kjører rundt objektet.
        Stanser når banen ser klar ut.
        Parser, og lager ny rute.
        Kjører etter ny rute.
         */
    }
}
