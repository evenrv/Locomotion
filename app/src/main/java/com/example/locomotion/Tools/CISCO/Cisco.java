package com.example.locomotion.Tools.CISCO;
import android.os.AsyncTask;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class Cisco extends AsyncTask<String, Void, CiscoArr>   {


    @Override
    protected CiscoArr doInBackground(String... params) {
        CiscoInfo ciscoInfo=new CiscoInfo();
        JSONObject jsonResult = new JSONObject();
        final String REQUEST_METHOD = "GET";
        // Read first element of sent parameters
        String stringUrl = params[0];

        String upw = "loomo:mas306-2020";

        //Declare the result of the Get-call as a string
        String result;

        // Prints out the url of the Rest-Api
        System.out.println(stringUrl);

        String inputLine;
        JSONObject lastUpdate = null;
        try {


            URL url = new URL(stringUrl);

            HttpsURLConnection connection = (HttpsURLConnection)
                    url.openConnection();
            final String basicAuth = "Basic " + new String(Base64.encode(upw.getBytes(), Base64.NO_WRAP));
            connection.setRequestProperty("Authorization", basicAuth);

            // final String basicAuth = "Basic " + Base64.encodeToString(("learning:learning").getBytes(), Base64.NO_WRAP);
            System.out.println("auth = " + basicAuth);

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);

            // Open the created HTTPS-url-connection
            connection.connect();
            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());
            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            //Check if the line we are reading is not null
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            reader.close();
            streamReader.close();
            result = stringBuilder.toString();
            lastUpdate = new JSONObject();
            lastUpdate.getJSONObject(result);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
            result = null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject reader = new JSONObject();
        org.json.simple.JSONObject last;

        CiscoArr ciscoArr = ciscoInfo.getInfo(lastUpdate);
        return ciscoArr;
    }
}