package com.example.locomotion.Tools;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.segway.robot.algo.VLSPoseListener;
import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.locomotion.sbv.StartVLSListener;



// class tbat enables all possible navigation and control-modes for mBase
public class BaseBinder extends AppCompatActivity {
    public Base mBase;
    public void bindOdomTarget(){
        mBase.bindService(this.getApplicationContext(), new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {
                        System.out.println("onbind called");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBase.setControlMode(Base.CONTROL_MODE_FOLLOW_TARGET);
                        mBase.setNavigationDataSource(Base.NAVIGATION_SOURCE_TYPE_ODOM);
                    }
                });
            }
            @Override
            public void onUnbind(String reason) {
            }
        });
    }
    public void bindOdomRaw(){
        mBase.bindService(this.getApplicationContext(), new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {
                System.out.println("onbind called");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBase.setControlMode(Base.CONTROL_MODE_RAW);
                        mBase.setNavigationDataSource(Base.NAVIGATION_SOURCE_TYPE_ODOM);
                    }
                });
            }
            @Override
            public void onUnbind(String reason) {
            }
        });
    }
    public void bindOdomNav(){
        mBase.bindService(this.getApplicationContext(), new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {
                System.out.println("onbind called");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBase.setControlMode(Base.CONTROL_MODE_NAVIGATION);
                        mBase.setNavigationDataSource(Base.NAVIGATION_SOURCE_TYPE_ODOM);
                    }
                });
            }
            @Override
            public void onUnbind(String reason) {
            }
        });
    }
    public void bindVls(Context context){
        mBase.bindService(context, new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {
                System.out.println("onbind called");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBase.setControlMode(Base.CONTROL_MODE_NAVIGATION);
                        startVLSNavigation(mBase);
                    }
                });
            }
            @Override
            public void onUnbind(String reason) {
                mBase.stopVLS();
                mBase.stop();
            }
        });
    }
    public void startVLSNavigation(Base mBase) {
        if (this.mBase.isVLSStarted()) {
            this.mBase.stopVLS();
        } else
            this.mBase.startVLS(true, true, new StartVLSListener() {
                @Override
                public void onOpened() {
                    System.out.println("VLS started");
                    BaseBinder.this.mBase.setNavigationDataSource(Base.NAVIGATION_SOURCE_TYPE_VLS);
                    setVLSPoseListener(BaseBinder.this.mBase);
                }
                @Override
                public void onError(String errorMessage) {
                }
            });
    }
    public void setVLSPoseListener(Base mBase) {
        mBase.setVLSPoseListener(new VLSPoseListener() {
            @Override
            public void onVLSPoseUpdate(long timestamp, float pose_x, float pose_y, float pose_theta, float v, float w) {
            }
        });
    }
}