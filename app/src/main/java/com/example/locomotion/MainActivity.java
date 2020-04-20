package com.example.locomotion;

import android.app.Dialog;
import android.os.AsyncTask;
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

import com.example.locomotion.Datatype.ListInteger;
import com.example.locomotion.Datatype.RoomInfo;
import com.example.locomotion.FindRoom.Find_Room;
import com.example.locomotion.FindRoom.ParseRoom;
import com.example.locomotion.Json.Create_Rooms;
import com.example.locomotion.Json.ParseJSON;
import com.example.locomotion.Tools.Point_converter;
import com.example.locomotion.Tools.RoomCenter;
import com.example.locomotion.Tools.UrlMaker;
import com.google.android.material.snackbar.Snackbar;
import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.perception.sensor.Sensor;
import com.segway.robot.sdk.vision.Vision;


import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


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

    Base mBase;
    AddCheckpoints checkpoint = new AddCheckpoints();
    Sensor mSensor;
    Vision mVision;
    String urlPath;

    Point_converter converter = new Point_converter();
    double[][] output;
    public double[] coordx;
    public double[] coordy;
    float angle;

    public Create_Rooms createrooms = new Create_Rooms();
    String[][] Room;
    public String[] RoomID;
    public String[] roomNumber;

    public Find_Room findroom = new Find_Room();
    String theRoom;

    String building;
    String floor;

    Double[] CiscoCoords;

    //Boolean for when loomo is ready to drive.
    boolean ready = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDialog = new Dialog(this);



        //Fetching the rooms.txt document.
        InputStream input;
        input = getResources().openRawResource(R.raw.rooms);

        //Room is a 2x array containing the IDs in one array, and roomNumbers in the other.

        try {

            Room = createrooms.createarrays(input);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Assigning roomNumber as the first element
        //and RoomID as the second element of the Room array
        roomNumber = Room[0];
        RoomID = Room[1];




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

        mVision = Vision.getInstance();
        mVision.bindService(getApplicationContext(), new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {

            }

            @Override
            public void onUnbind(String reason) {

            }
        });

    }




    //This function will be called by the calibrate button, and will calibrate Loomo, so that it's
    // x axis is pointed towards east. This is because longitude and latitude values are considered
    // a good approximation to a cartesian coordinate-system on a small scale.

    //test: This function may be implemented in the on create function alone, so that the loomo
    //automatically calibrates as the application is opened.

    public void calibrate(View view){
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
                angle = calibrate.calibrate(mBase, mSensor);
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

    public void startnavigation(View view){

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

    //The function to the GetPath button. This function calls the asynctask Getroute,
    // and finds a route based on previous inputs. The boolean ready is true when this is ran,
    // but only if a valid room is selected. Ready is returned.
    public void getpath(View view){


        if (theRoom != null) {
          new Getroute().execute();
          ready = true;

            Snackbar.make(view, "Finding a path to your room", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }

        else {
            Snackbar.make(view, "Please enter a valid room first", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            ready = false;
        }

    }


    //Drives Loomo by sending in the previously made coordinate arrays.
    public void navigate(View view) {

        //Creating checkpoints
        if (theRoom != null && ready) {

            checkpoint.add(mBase, mSensor, output);
        }

        else{
            Snackbar.make(view, "Please enter a room first", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    //This function fetches the written room number in the text box and makes it a string.
    // If the theRoom == 0, you will be asked to try another room, because it does not exist.
    //If theRoom !== 0, the function will call GetRoom(), and download the room information.
    // The find the center of the selected room, and use these
    //coordinates as well as the cisco output for Loomos location to create the urlPath string.

    public void getroom(View view){

        TextView room = findViewById(R.id.roomNumber);
        String userInput = room.getText().toString();

        theRoom = findroom.FindRoomID(RoomID, roomNumber, userInput, building, floor);

        if (theRoom == null) {
            Snackbar.make(view, "The entered room does not exist. Please try another room", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }

        else {

            new GetRoom().execute();

            Snackbar.make(view, "Finding the location of the entered room", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    public void questions(View view){

        setContentView(R.layout.questions);
        TextView mtitlewindow = findViewById(R.id.titlewindow);
        TextView minstructions =findViewById(R.id.instructions);

        mtitlewindow.setText("How to use the application");
        StringBuilder stringBuilder= new StringBuilder();
        String melding = "Calibration: Loomo will drive 1 meter forward, and back again. Then Loomo will rotate towards east \n\n" +
                "This is an example of a room number: C2 036. Where C is the building name, " +
                "2 is what floor the room is located at, and 036 is the room number.\n\n" +
                "1. You can always press the logo to return to the front page\n" +
                "2. When you press the 'Start Navigation'-button, you get redirected to " +
                "the page where you select what room you are going to. \n" +
                "3. Start by selecting what building the room is in, then select what floor" +
                " the room is at, and finally, type in the ending number of your room.\n " +
                "4. Now press 'fetch room ID'. Loomo will now find where the room is located.\n" +
                "5. Press 'Get Path'. Now, Loomo will download the route to the room you selected\n" +
                "6. Press the play button to make loomo start navigating.";

        stringBuilder.append(melding);
        minstructions.setText(stringBuilder.toString());


    }

    public void gohome(View view){
        setContentView(R.layout.activity_main);
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


        if(parent.getId() == R.id.selectBuilding)
        {
            building = text;
        }

        else if (parent.getId() == R.id.selectFloor)
        {
            floor = text;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }



    private class GetRoom extends AsyncTask<URL, ListInteger, RoomInfo>

    {

        @Override
        protected RoomInfo doInBackground(URL... urls) {

            ParseRoom parseRoom = new ParseRoom();
            String url = ParseRoom.setUrl(theRoom);
            RoomInfo roomInfo;

            //This function downloads the roominformation and stores it as a json object.
            roomInfo = ParseRoom.roomCoor(url);


            return roomInfo;
        }

        protected void onPostExecute(RoomInfo roomInfo) {


            RoomCenter roomCenter = new RoomCenter();
            Double[] avgCoord = RoomCenter.calculateAvg(roomInfo.coords);               // calculates "center" of room based on the coordinates of the corners

            System.out.println("Center of Room: " + avgCoord[0] + " " + avgCoord[1] + ", at floor: " + roomInfo.z);

            UrlMaker urlMaker = new UrlMaker();
            //CiscoPos ciscoPos = new CiscoPos(); --> currentCoords + muligens current_z her
            CiscoCoords = new Double[]{8.576590048403403, 58.33423511424411};

            urlPath = urlMaker.makeUrl(CiscoCoords, 2.0, avgCoord, roomInfo.z);

        }
    }


    private class Getroute extends AsyncTask<URL, ListInteger, PathInfo> {
        @Override
        protected PathInfo doInBackground(URL... urls) {
            JSONObject jsonObjectPath;
            JSONPath jsonPath = new JSONPath();
            PathInfo pathInfo = new PathInfo();
            ParseJSON parseJSON = new ParseJSON();


                try {
                    jsonObjectPath = ParseJSON.readJsonFromUrl(urlPath);
                    pathInfo = JSONPath.pathData(jsonObjectPath);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


        return pathInfo;
        }


        protected void onPostExecute(PathInfo pathInfo) {


                ArrayList<Double[]> checkPoints = (PathInfo.coordinateArray);
                ArrayList<Integer> indexes = (PathInfo.flagIndexes);


                output = converter.convert(checkPoints, CiscoCoords, angle);

        }
    }
}