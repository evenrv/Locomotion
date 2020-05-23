package com.example.locomotion.usb;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Handler;
/*
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
 */
//TODO: muligens ikke nodvendig
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locomotion.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.segway.robot.sdk.emoji.module.base.Base;

import me.aflak.arduino.Arduino;
import me.aflak.arduino.ArduinoListener;

public class SerialArduino extends AppCompatActivity implements ArduinoListener {

        FloatingActionButton fab;
        TextView textView;
        Button btn_send, btn_0, btn_1;
        EditText editText;
        Arduino arduino;
        public Base mBase;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: lag ny layout for USB_serial
/*
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        fab = findViewById(R.id.fab);
        textView = findViewById(R.id.textView);
        btn_send = findViewById(R.id.btn_send);
        btn_0 = findViewById(R.id.btn_0);
        btn_1 = findViewById(R.id.btn_1);
        editText = findViewById(R.id.editText2);

        textView.setText("");
        textView.setMovementMethod(new ScrollingMovementMethod());
        fab.setOnClickListener(fab_OnClickListener);
        btn_send.setOnClickListener(btn_send_OnClickListener);
        btn_0.setOnClickListener(btn_0_OnClickListener);
        btn_1.setOnClickListener(btn_1_OnClickListener);

 */
        arduino = new Arduino(this);
        display("Please plug an Arduino via OTG.\nOn some devices you will have to enable OTG Storage in the phone's settings.\n\n");

        //setEnabledUi(false);
        }

        protected void onStart(){
        super.onStart();
        arduino.setArduinoListener(this);
        }

        protected void onDestroy(){
        super.onDestroy();
        arduino.unsetArduinoListener();
        arduino.close();
        }

        View.OnClickListener fab_OnClickListener = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
        });

        View.OnClickListener btn_send_OnClickListener = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        String str = editText.getText().toString()+"\r\n";
        byte[] b = str.getBytes();
        arduino.send(b);
        Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
        //onClickWrite(view, str);
        }
        });

        View.OnClickListener btn_0_OnClickListener = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        String str = "0";
        byte[] b = str.getBytes();
        arduino.send(b);
        Toast.makeText(getApplicationContext(),"0",Toast.LENGTH_SHORT).show();
        //onClickWrite(view, str);
        }
        });

        View.OnClickListener btn_1_OnClickListener = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        String str = "1";
        byte[] b = str.getBytes();
        arduino.send(b);
        Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
        //onClickWrite(view, str);
        }
        });

        public void dataFrom(byte[] b){                            // Flatland/Vehus
        b.toString();

        arduino.send(b);
        }

        public void dataToArduino(int phase, float angle){      // Flatland/Vehus
        String info = phase+ "  " + angle;
        byte[] b = info.getBytes();
        arduino.send(b);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
        return true;
        }

        return super.onOptionsItemSelected(item);
        }

        Handler mHandler = new Handler();
        private void append(TextView tv, CharSequence text) {
        final TextView ftv = tv;
        final CharSequence ftext = text;
        mHandler.post(new Runnable() {
        @Override
        public void run() {
        ftv.append(ftext); 	// add text to Text view
        }
        });
        }

    /*public void onClickWrite(View v, String str) { 	// when send button is prressed

    }*/


        //setEnabledUi method to set UI elements on screen
        private void setEnabledUi(boolean on) {
        if(on) {                            // if connected to device
        textView.setEnabled(true);       //hide open button (already opened)
        btn_1.setEnabled(true);       //hide baudrate selector
        btn_0.setEnabled(true); // hide autoscroll
        btn_send.setEnabled(true);       // display close button
        editText.setEnabled(true);       // display send button
        fab.setEnabled(true);       // display edittext field
        } else {                            // if not connected to device
        textView.setEnabled(false);       //hide open button (already opened)
        btn_1.setEnabled(false);       //hide baudrate selector
        btn_0.setEnabled(false); // hide autoscroll
        btn_send.setEnabled(false);       // display close button
        editText.setEnabled(false);       // display send button
        fab.setEnabled(false);       // display edittext field
        }
        }

        @Override
        public void onArduinoAttached(UsbDevice device) {
        Toast.makeText(getApplicationContext(),"Device Attached",Toast.LENGTH_SHORT).show();
        //display("Device Attached");
        arduino.open(device);
        }

        @Override
        public void onArduinoDetached() {
        //display("Device Detached");
        Toast.makeText(getApplicationContext(),"Device Detached",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onArduinoMessage(byte[] bytes) {
        display("Received: "+new String(bytes));
//        Toast.makeText(getApplicationContext(),new String(bytes),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onArduinoOpened() {
        String str = "0";
        arduino.send(str.getBytes());
        Toast.makeText(getApplicationContext(),"0",Toast.LENGTH_SHORT).show();
        }

        public void display(final String message){
        runOnUiThread(new Runnable() {
        @Override
        public void run() {
        textView.append(message+"\n");
        }
        });
        }
        }
