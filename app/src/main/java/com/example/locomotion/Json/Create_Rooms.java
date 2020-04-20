package com.example.locomotion.Json;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Create_Rooms {

    public String[][] createarrays(InputStream input) throws IOException {

        ArrayList<String> roomArr = new ArrayList<>();
        ArrayList<String> roomIdArr = new ArrayList<>();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(input));

        String line = buffer.readLine();
        StringBuilder stringbuilder = new StringBuilder();

        while (line != null) {
            stringbuilder.append(line).append(",");

            String roomName = line.substring(4, 10); // RoomId
            String roomPoiID = line.substring(16);
            roomIdArr.add(roomPoiID);
            roomArr.add(roomName);
            line = buffer.readLine();

        }


        ArrListToArr arrListToArr = new ArrListToArr();
        String[][] Room = arrListToArr.getArr(roomArr, roomIdArr);
        return Room;
    }

    private class ArrListToArr {
        public String[][] getArr(ArrayList<String> roomArr, ArrayList<String> roomIdArr) {
            String[][] Room = new String[2][roomIdArr.size()];
            for (int i = 0; i < roomArr.size(); i++) {
                Room[0][i] = roomArr.get(i);
                Room[1][i] = roomIdArr.get(i);
            }
            return Room;
        }
    }
}