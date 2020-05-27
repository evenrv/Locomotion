package com.example.locomotion.Json;

import android.os.AsyncTask;

import com.example.locomotion.Datatype.ParseInfo;
import com.example.locomotion.Datatype.PathInfo;
import com.example.locomotion.Datatype.RoomInfo;
import com.example.locomotion.FindRoom.ParseRoom;
import com.example.locomotion.Tools.PointConverter;
import com.example.locomotion.Tools.RoomCenter;
import com.example.locomotion.Tools.UrlMaker;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;


public class RouteFinder extends AsyncTask<ParseInfo, Void, ParseInfo>   {

        @Override
        protected ParseInfo doInBackground(ParseInfo... params){

            ParseInfo parseInfo = params[0];


            String theRoom = parseInfo.room;

            String url = ParseRoom.setUrl(theRoom);
            RoomInfo roomInfo;

            //This function downloads the roominformation and stores it as a json object.
            roomInfo = ParseRoom.roomCoor(url);

            // calculates "center" of room based on the coordinates of the corners
            Double[] avgCoord = RoomCenter.calculateAvg(roomInfo.coords);

            System.out.println("Center of Room: " + avgCoord[0] + " " + avgCoord[1] + ", at floor: " + roomInfo.z);

            UrlMaker urlMaker = new UrlMaker();
            //Cisco coordinates are fetched in the async task
            Double[] ciscoCoords;

            //Cisco returns the following values for location:
            ciscoCoords = new Double[]{8.576170839175433, 58.334475507774954, 3.0};
            String urlPath;
            urlPath = urlMaker.makeUrl(ciscoCoords, avgCoord, roomInfo.z);



            JSONObject jsonObjectPath;
            PathInfo pathInfo = new PathInfo();

            //Clearing previous checkpoints
            pathInfo.coordinateArray.clear();

            try {
                jsonObjectPath = ParseJSON.readJsonFromUrl(urlPath);
                pathInfo = JSONPath.pathData(jsonObjectPath);


            } catch (IOException | ParseException | JSONException e) {
                e.printStackTrace();
            }


            ArrayList<Double[]> checkPoints = (pathInfo.coordinateArray);
            ArrayList<Integer> indexes = (PathInfo.flagIndexes);

            PointConverter converter = new PointConverter();
            double[][] output;
            output = converter.convert(checkPoints, ciscoCoords, parseInfo);

            parseInfo.xcoords = output[0];
            parseInfo.ycoords = output[1];


            for(int coordinate = 0; coordinate < parseInfo.xcoords.length; coordinate++){
                System.out.println(parseInfo.xcoords[coordinate]);
                System.out.println(parseInfo.ycoords[coordinate]);
            }



            return parseInfo;
        }
    }