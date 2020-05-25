package com.example.locomotion;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.locomotion.Datatype.ParseInfo;
import com.example.locomotion.Driving.Drive;
import com.example.locomotion.FindRoom.FindRoom;
import com.example.locomotion.Json.CreateRooms;
import com.example.locomotion.Json.RouteFinder;
import com.example.locomotion.Tools.Binders;
import com.example.locomotion.Tools.Calibrate;
import com.example.locomotion.Tools.CISCO.Cisco;
import com.example.locomotion.Tools.CISCO.CiscoArr;
import com.example.locomotion.Tools.CISCO.CiscoInfo;
import com.google.android.material.snackbar.Snackbar;
import com.segway.robot.sdk.locomotion.head.Head;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.perception.sensor.Sensor;
import com.segway.robot.sdk.vision.Vision;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Creating objects to control the base and the sensors of Loomo. 
    //Creates checkpoint object to add checkpoints.
    //urlPath is a global variable containing a link to the path-information.

    // Creating a converter object to convert the global coordinates in degrees
    // to local coordinates in metres. "Output" is an array with
    // two elements: x-coordinates and y-coordinates

    //Creating a createrooms object to create two arrays from rooms.txt in the res -> raw folder. 
    // The first array contains all room numbers and the second contains 
    // all IDs for the room numbers. theRoom will contain the ID for the selected room.

    Dialog myDialog;

    // Create array of CiscoArr-class
    ArrayList<CiscoArr> ciscoArr= new ArrayList<>();



    Cisco cisco;
    public Context context;

    // make instances of Loomo public
    public Base mBase;
    public Head mHead ;
    public Sensor mSensor;
    public Vision mVision;
    public CreateRooms createrooms = new CreateRooms();
    String[][] Room;
    public String[] RoomID;
    public String[] roomNumber;

    Binders binders = new Binders();


    public FindRoom findroom = new FindRoom();

    String building;
    String floor;

    ParseInfo parseInfo = new ParseInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this.getApplicationContext();
        mBase = Base.getInstance();
        mHead = Head.getInstance();
        mSensor = Sensor.getInstance();
        mVision = Vision.getInstance();

        //binders.bindAll();  // bind head, base, vision and sensor
       // binders.bindBase();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDialog = new Dialog(this);


        //Fetching the rooms.txt document.
        InputStream input;
        input = getResources().openRawResource(R.raw.rooms);


        //"Room" is a double[][] array containing the IDs in one array, and the corresponding
        // roomNumbers in the other. This means each element i in the ID array will fit each element
        //i in the roomNumbers array

        try {
            /*
                   Cisco cisco = new Cisco();
            CiscoInfo ciscoInfo = new CiscoInfo();
            String stringUrl = "https://cmx.uia.no/api/location/v1/clients/f4:4d:30:c3:18:07";

            ciscoArr.add(0, cisco.execute(stringUrl).get());
             */


            Room = createrooms.createarrays(input);

        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (InterruptedException e) {

            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/


        //Assigning roomNumber as the first element
        //and RoomID as the second element of the Room array
        roomNumber = Room[0];
        RoomID = Room[1];


/*
        //initializing the sensor instance and binding the service
        mSensor = Sensor.getInstance();
        mSensor.bindService(getApplicationContext(), new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {

            }

            @Override
            public void onUnbind(String reason) {

            }
        });

        //initializing the Base instance and binding the service
        mBase = Base.getInstance();
        mBase.bindService(getApplicationContext(), new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {

            }

            @Override
            public void onUnbind(String reason) {

            }
        });

        mHead = Head.getInstance();
        mHead.bindService(getApplicationContext(), new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {

            }

            @Override
            public void onUnbind(String reason) {

            }
        });

        mVision = Vision.getInstance();
        mVision.bindService(getApplicationContext(), new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {

            }

            @Override
            public void onUnbind(String reason) {

            }
        });
 */
        //TODO: Vi bør legge alt dette i en egen klasse


        //Loomo needs to be calibrated first thing after startup.
        // A popup will appear, and asks if you want to calibrate Loomo now.
        //There will be to buttons in the popup: One for yes, and one for no.
        //Then an OnCLickListener is set for both buttons, so that when either is pressed
        //a following instruction will take place.

        Button yesButton;
        Button noButton;
        myDialog.setContentView(R.layout.popup);
        yesButton = myDialog.findViewById(R.id.YesButton);
        noButton = myDialog.findViewById(R.id.NoButton);
        myDialog.show();

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calibrate calibrate = new Calibrate();

                float[] calibinfo = calibrate.calibrate(mBase, mSensor);

                parseInfo.angle = calibinfo[0];
                parseInfo.mPerLong = calibinfo[1];
                parseInfo.mPerLat = calibinfo[2];
                myDialog.dismiss();

            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
    }

    //This function will be called by the calibrate button, and will calibrate Loomo.
    //When pressed, a popup will appear, equal to the pop up in the OnCreate function.


    public void calibrate(View view) {
        Button yesButton;
        Button noButton;
        myDialog.setContentView(R.layout.popup);
        yesButton = myDialog.findViewById(R.id.YesButton);
        noButton = myDialog.findViewById(R.id.NoButton);
        myDialog.show();


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calibrate calibrate = new Calibrate();

                float[] calibinfo = calibrate.calibrate(mBase, mSensor);

                parseInfo.angle = calibinfo[0];
                parseInfo.mPerLong = calibinfo[1];
                parseInfo.mPerLat = calibinfo[2];
                myDialog.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
    }


    //When pressing the Start Navigation button (id: changeview), select_room_page will open.
    //Two spinners are created
    public void startnavigation(View view) {

        setContentView(R.layout.select_room_page);

        Spinner spinner1 = findViewById(R.id.selectBuilding);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.selectBuilding, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);

        Spinner spinner2 = findViewById(R.id.selectFloor);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.selectFloor, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

    }


    //Drives Loomo by sending in the previously made coordinate arrays.
    //The function starts driving loomo by running the "drive"-function from the Drive
    //class if a valid room number is entered and a path is found.
    //If theRoom == 0, then a snackbar will appear, telling the user to  enter a room.

    public void navigate(View view) {

        if (parseInfo.room != null) {
            System.out.println(parseInfo.xcoords[1]);
            Drive checkpoint = new Drive();
            checkpoint.drive(mBase, mSensor, parseInfo);

        } else {
            Snackbar.make(view, "Please enter a room first", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    //This function fetches the written room number in the text box and makes it a string.
    // If the theRoom == 0, you will be asked to try another room, because it does not exist.
    //If theRoom !== 0, the function will call GetRoom(), and download the room information.
    // The find the center of the selected room, and use these
    //coordinates as well as the cisco output for Loomos location to create the urlPath string.

    public void getroom(View view) throws ExecutionException, InterruptedException {

        TextView room = findViewById(R.id.roomNumber);
        String userInput = room.getText().toString();

        parseInfo.room = findroom.FindRoomID(RoomID, roomNumber, userInput, building, floor);


        if (parseInfo.room == null) {
            Snackbar.make(view, "The entered room does not exist. Please try another room", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        } else {

            parseInfo = new RouteFinder().execute(parseInfo).get();


            Snackbar.make(view, "Finding a path to your room", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }


    //If the questionmark-button is pressed, then a new layout will appear,
    // explaining how the application works
    //mtitlewindow will contain the title, and minstructions will contain the instruction text.
    public void questions(View view) {


        setContentView(R.layout.questions);
        TextView mtitlewindow = findViewById(R.id.titlewindow);
        TextView minstructions = findViewById(R.id.instructions);

        //Importing resource string instructionsHeader and instructions
        mtitlewindow.setText(getResources().getString(R.string.instructionsHeader));
        StringBuilder stringBuilder = new StringBuilder();
        String melding = getResources().getString(R.string.instructions);

        stringBuilder.append(melding);
        minstructions.setText(stringBuilder.toString());


    }

    public void gohome(View view) {
        setContentView(R.layout.front_page);
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


    //This listener fetches the chosen values from the spinboxes, and assigns the values
    // to "building" and "floor".
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String text = parent.getItemAtPosition(position).toString();


        if (parent.getId() == R.id.selectBuilding) {
            building = text;
        } else if (parent.getId() == R.id.selectFloor) {
            floor = text;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
