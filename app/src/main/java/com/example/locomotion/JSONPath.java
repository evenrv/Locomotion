package com.example.locomotion;

import com.example.locomotion.Json.ParseJSON;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class JSONPath {

    public static ArrayList<Integer> elevatorIndex = new ArrayList<>(); // marks elements which enter Elevator
    public static ArrayList<Double[]> coordinateArr = new ArrayList<>();
    static ParseJSON parseJson = new ParseJSON();

    //   public static void main(String args[]) throws IOException, ParseException, JSONException {    }
    public static JSONObject parseURL(String url) throws JSONException, ParseException, IOException {
        JSONObject jsonObject = parseJson.readJsonFromUrl(url);
        return jsonObject;
    }
    /*
    public static void printCoordinates(ArrayList<Double[]> coordinateArr, ArrayList<Integer> elevatorIndex) {
        for (int i = 0, cnt = 0; i < coordinateArr.size(); i++) {


            try {
                System.out.println(coordinateArr.get(i)[0]);
                System.out.println(coordinateArr.get(i)[1]);
                System.out.println(i);
                if (elevatorIndex.get(cnt) == i && cnt < elevatorIndex.size()) {
                    System.out.println("elevator @" + i);
                    cnt++;
                }
            }
             catch(IndexOutOfBoundsException e){
                    System.out.println(coordinateArr.get(i)[0]);
                    System.out.println(coordinateArr.get(i)[1]);
                    System.out.println(i);
                }
            }
        }*/


    public static PathInfo pathData (JSONObject jsonObject)
    {            // Returns instance of PathInfo-class
        PathInfo pathInfo = new PathInfo();                             // new instance of PathInfo

        try {
            JSONArray fullCoorArr = new JSONArray();

            JSONArray featuresArray = (JSONArray) ((JSONObject) jsonObject.get("path")).get("features");
            for (int i = 0; i < featuresArray.size(); i++) {
                JSONObject featuresObj = (JSONObject) featuresArray.get(i);
                JSONArray coorArr = (JSONArray) ((JSONObject) featuresObj.get("geometry")).get("coordinates");
                JSONObject propertiesObj = (JSONObject) featuresObj.get("properties");
                JSONArray flagArr = ((JSONArray) propertiesObj.get("flags"));
                boolean elevator = ((flagArr.toString()).toUpperCase()).contains("ELEVATOR");
                JSONArray temp_0 = (JSONArray) coorArr.get(0);
                JSONArray temp_1 = (JSONArray) coorArr.get(1);
                Double[] xy_0 = {(Double) temp_0.get(0), (Double) temp_0.get(1)};
                Double[] xy_1 = {(Double) temp_1.get(0), (Double) temp_1.get(1)};

                if (i == 0) {
                    if (elevator == true) {           // adds index of coordinate-pair to enter Elevator into elevatorIndex
                        pathInfo.flagIndexes.add(i + 1);
                    }
                    PathInfo.coordinateArray.add(xy_0);
                    PathInfo.coordinateArray.add(xy_1);
                } else {
                    if (elevator == true) {           // adds index of coordinate-pair to enter Elevator into elevatorIndex
                        pathInfo.flagIndexes.add(i + 1);
                    }
                    PathInfo.coordinateArray.add(xy_1);
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        printIndex(pathInfo.flagIndexes);
        // printCoordinates(pathInfo.coordinateArray, pathInfo.flagIndexes);
        return pathInfo;                                                        // returns instance of pathInfo-class
    }
    public static void printIndex (ArrayList< Integer > flagIndex) {
        for (int jj = 0; jj < flagIndex.size(); jj++) {

        }
    }
}