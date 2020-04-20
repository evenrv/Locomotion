package com.example.locomotion.FindRoom;


public class Find_Room {


    String TheRoom;
    String roomNumber;
    String room;


    public String FindRoomID(String[] RoomID, String[] RoomNumber, String userInput, String building, String floor)

    {
        if (userInput.length() == 2)
        {
            roomNumber = "0" + userInput;
        }
        else if (userInput.length() == 3){
            roomNumber = userInput;
        }
        else{

        }

        //Creating the final room number, based on the input from the spinners, and the user input.
        room = (building + floor + " " + roomNumber);


        //Checking if the final romm number is in the array of room numbers. If it is, the
        // corresponding ID will be assigned to TheRoom

        for (int i = 0; i < RoomID.length; i++) {

            if (RoomNumber[i].contains(room)) {
                TheRoom = RoomID[i];
                break;
            }

            else TheRoom = null;
        }




        System.out.println("your final input: " + room);
        System.out.println("The ID of your Room:" + TheRoom);

        return TheRoom;
    }
}
