package com.example.locomotion.FindRoom;

import com.example.locomotion.Json.ParseJSON;
import com.example.locomotion.Datatype.RoomInfo;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class ParseRoom {

    public static String setUrl(String theRoom){
        String url = "https://api.mazemap.com/api/pois/" + theRoom + "/?srid=4326";
        System.out.println(url);

        return url;
    }

    public static RoomInfo roomCoor(String url) {
        RoomInfo roomInfo = new RoomInfo();
        ParseJSON parseJSON = new ParseJSON();

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject = parseJSON.readJsonFromUrl(url);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        roomInfo.z =(Double) jsonObject.get("z");
        JSONObject geometryObj = ((JSONObject) jsonObject.get("geometry"));
        JSONArray roomArr = (JSONArray) ((JSONArray) geometryObj.get("coordinates")).get(0);
        // roomCoordinates.addAll(roomArr);

        for (int ii = 0; ii < roomArr.size(); ii++) {                              // loops trough JSONArrat coordinateArr
            JSONArray coordinatePair = (JSONArray) roomArr.get(ii);               // double-array of x-y-pair of coordinates
            Double[] x_y = new Double[2];
            for (int iii = 0; iii < coordinatePair.size(); iii++) {
                x_y[iii] = (Double) coordinatePair.get(iii);
            }
            roomInfo.coords.add(x_y);                           // sends double-array of x-y-pair of coordinates to Arraylist of double-array
        }

        return roomInfo;


    }
}
