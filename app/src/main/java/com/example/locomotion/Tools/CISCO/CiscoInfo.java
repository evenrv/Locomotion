package com.example.locomotion.Tools.CISCO;

import org.json.JSONArray;
import org.json.JSONObject;

public class CiscoInfo {

    static JSONObject posArr = new JSONObject();
    JSONObject lastUpdate = new JSONObject();

    float posX;
    float posY;
    float posZ;

    static JSONObject geoCoords = new JSONObject();
    float latitude;
    float longitude;

    static JSONObject timeInfo = new JSONObject();
    float currentTime;
    float timeFirstUpdate;
    float timeLastUpdate;

    JSONObject confFactor = new JSONObject();
    int confindenceFactor;
    CiscoArr ciscoArr = new CiscoArr();

    /*
        public static void main (String[] args){
        try {
            testJSON = (JSONArray) reader.getJSONArray(args[0]);
            for(int i = 0; i < testJSON.length(); i++){
                Arr arr = new Arr();
                JSONObject tempObj= (JSONObject) testJSON.get(i);
                 =  getInfo(tempObj);
                System.out.println(.posX);
                System.out.println(" , ");
                System.out.println(arr.posY);
                System.out.println(" , ");
                System.out.println(arr.posZ);
                System.out.println(" , ");
                System.out.println(arr.confindenceFactor);
                System.out.println(" , ");
                System.out.println(arr.currentTime);
                System.out.println(" , ");
                System.out.println(arr.timeLastUpdate);
                System.out.println(" , ");
                System.out.println(arr.latitude);
                System.out.println(" , ");
                System.out.println(arr.longitude);
                System.out.println("-----------------------------------------------------------------");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

     */
    public  CiscoArr getInfo(JSONObject lastUpdate) {
        try {
            CiscoArr ciscoArr = new CiscoArr();


            //   lastUpdate = (JSONObject) infoArray.get(0);      // last updated values
            posArr = (JSONObject) lastUpdate.get("mapCoordinate");
            ciscoArr.posX = ((Double) posArr.get("x")) / 3.2808;
            ciscoArr.posY = ((Double) posArr.get("y")) / 3.2808;
            Long z = (Long) posArr.get("z");
            ciscoArr.posZ = (((Long) posArr.get("z"))) / (3.2808);


            geoCoords = (JSONObject) lastUpdate.get("geoCoordinate");
            ciscoArr.latitude = (Double) geoCoords.get("latitude");
            ciscoArr.longitude = (Double) geoCoords.get("longitude");

            timeInfo = (JSONObject) lastUpdate.get("statistics");
            ciscoArr.currentTime = (String) (timeInfo.get("currentServerTime"));
            ciscoArr.timeFirstUpdate = (String) timeInfo.get("firstLocatedTime");
            ciscoArr.timeLastUpdate = (String) timeInfo.get("lastLocatedTime");

            ciscoArr.confindenceFactor = (Long) lastUpdate.get("confidenceFactor");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ciscoArr;
    }

    static JSONArray testJSON = new JSONArray();
    static JSONObject reader = new JSONObject();







/* Example-output for a CISCO-call
    static String testArr = "[\n" +
            "    {\n" +
            "        \"macAddress\": \"ac:cf:85:29:53:5f\",\n" +
            "        \"mapInfo\": {\n" +
            "            \"mapHierarchyString\": \"Nortech Campus>Nortech-1>1st Floor\",\n" +
            "            \"mapHierarchyDetails\": {\n" +
            "                \"campus\": \"Nortech Campus\",\n" +
            "                \"building\": \"Nortech-1\",\n" +
            "                \"floor\": \"1st Floor\",\n" +
            "                \"floorAesUid\": 727035700041482200,\n" +
            "                \"zones\": \"\"\n" +
            "            }\n" +
            "        },\n" +
            "        \"mapCoordinate\": {\n" +
            "            \"x\": 78.017624,\n" +
            "            \"y\": 38.217056,\n" +
            "            \"z\": 0,\n" +
            "            \"unit\": \"FEET\"\n" +
            "        },\n" +
            "        \"currentlyTracked\": true,\n" +
            "        \"confidenceFactor\": 160,\n" +
            "        \"locComputeType\": \"RSSI\",\n" +
            "        \"statistics\": {\n" +
            "            \"currentServerTime\": \"2017-12-06T22:31:17.730-0800\",\n" +
            "            \"firstLocatedTime\": \"2017-12-06T12:57:13.469-0800\",\n" +
            "            \"lastLocatedTime\": \"2017-12-06T12:57:13.469-0800\"\n" +
            "        },\n" +
            "        \"historyLogReason\": \"FLOOR_CHANGE\",\n" +
            "        \"geoCoordinate\": {\n" +
            "            \"latitude\": 37.42248685324713,\n" +
            "            \"longitude\": -121.95989831612766,\n" +
            "            \"unit\": \"DEGREES\"\n" +
            "        },\n" +
            "        \"rawLocation\": null,\n" +
            "        \"networkStatus\": \"ACTIVE\",\n" +
            "        \"changedOn\": 1512593833469,\n" +
            "        \"ipAddress\": [],\n" +
            "        \"userName\": \"\",\n" +
            "        \"ssId\": \"\",\n" +
            "        \"sourceTimestamp\": \"1512593833469\",\n" +
            "        \"band\": \"UNKNOWN\",\n" +
            "        \"apMacAddress\": \"\",\n" +
            "        \"dot11Status\": \"UNKNOWN\",\n" +
            "        \"manufacturer\": \"HUAWEI TECHNOLOGIES CO.,LTD\",\n" +
            "        \"areaGlobalIdList\": null,\n" +
            "        \"detectingControllers\": \"10.22.243.211\",\n" +
            "        \"bytesSent\": 0,\n" +
            "        \"bytesReceived\": 0,\n" +
            "        \"guestUser\": false\n" +
            "    },\n" +
            "    {\n" +
            "        \"macAddress\": \"08:cc:68:b4:1c:1f\",\n" +
            "        \"mapInfo\": {\n" +
            "            \"mapHierarchyString\": \"Nortech Campus>Nortech-1>1st Floor>CMX Clinic\",\n" +
            "            \"mapHierarchyDetails\": {\n" +
            "                \"campus\": \"Nortech Campus\",\n" +
            "                \"building\": \"Nortech-1\",\n" +
            "                \"floor\": \"1st Floor\",\n" +
            "                \"floorAesUid\": 727035700041482200,\n" +
            "                \"zones\": \"CMX Clinic\"\n" +
            "            }\n" +
            "        },\n" +
            "        \"mapCoordinate\": {\n" +
            "            \"x\": 22.515627,\n" +
            "            \"y\": 15.5579815,\n" +
            "            \"z\": 0,\n" +
            "            \"unit\": \"FEET\"\n" +
            "        },\n" +
            "        \"currentlyTracked\": true,\n" +
            "        \"confidenceFactor\": 32,\n" +
            "        \"locComputeType\": \"RSSI\",\n" +
            "        \"statistics\": {\n" +
            "            \"currentServerTime\": \"2017-12-06T22:31:17.731-0800\",\n" +
            "            \"firstLocatedTime\": \"2017-12-06T19:09:30.430-0800\",\n" +
            "            \"lastLocatedTime\": \"2017-12-06T19:09:30.430-0800\"\n" +
            "        },\n" +
            "        \"historyLogReason\": \"NETWORK_STATUS_CHANGE\",\n" +
            "        \"geoCoordinate\": {\n" +
            "            \"latitude\": 37.42242376668243,\n" +
            "            \"longitude\": -121.95969870663463,\n" +
            "            \"unit\": \"DEGREES\"\n" +
            "        },\n" +
            "        \"rawLocation\": null,\n" +
            "        \"networkStatus\": \"ACTIVE\",\n" +
            "        \"changedOn\": 1512616170430,\n" +
            "        \"ipAddress\": [],\n" +
            "        \"userName\": \"\",\n" +
            "        \"ssId\": \"\",\n" +
            "        \"sourceTimestamp\": \"1512616170430\",\n" +
            "        \"band\": \"UNKNOWN\",\n" +
            "        \"apMacAddress\": \"\",\n" +
            "        \"dot11Status\": \"UNKNOWN\",\n" +
            "        \"manufacturer\": \"Cisco Systems, Inc\",\n" +
            "        \"areaGlobalIdList\": null,\n" +
            "        \"detectingControllers\": \"10.22.243.211\",\n" +
            "        \"bytesSent\": 0,\n" +
            "        \"bytesReceived\": 0,\n" +
            "        \"guestUser\": false\n" +
            "    },\n" +
            "    {\n" +
            "        \"macAddress\": \"08:cc:68:b4:1c:1f\",\n" +
            "        \"mapInfo\": {\n" +
            "            \"mapHierarchyString\": \"Nortech Campus>Nortech-1>1st Floor>CMX Clinic\",\n" +
            "            \"mapHierarchyDetails\": {\n" +
            "                \"campus\": \"Nortech Campus\",\n" +
            "                \"building\": \"Nortech-1\",\n" +
            "                \"floor\": \"1st Floor\",\n" +
            "                \"floorAesUid\": 727035700041482200,\n" +
            "                \"zones\": \"CMX Clinic\"\n" +
            "            }\n" +
            "        },\n" +
            "        \"mapCoordinate\": {\n" +
            "            \"x\": 21.428608,\n" +
            "            \"y\": 16.509167,\n" +
            "            \"z\": 0,\n" +
            "            \"unit\": \"FEET\"\n" +
            "        },\n" +
            "        \"currentlyTracked\": false,\n" +
            "        \"confidenceFactor\": 48,\n" +
            "        \"locComputeType\": \"RSSI\",\n" +
            "        \"statistics\": {\n" +
            "            \"currentServerTime\": \"2017-12-06T22:31:17.731-0800\",\n" +
            "            \"firstLocatedTime\": \"2017-12-06T19:09:03.079-0800\",\n" +
            "            \"lastLocatedTime\": \"2017-12-06T19:09:05.082-0800\"\n" +
            "        },\n" +
            "        \"historyLogReason\": \"NETWORK_STATUS_CHANGE\",\n" +
            "        \"geoCoordinate\": {\n" +
            "            \"latitude\": 37.422426413833385,\n" +
            "            \"longitude\": -121.95969480129244,\n" +
            "            \"unit\": \"DEGREES\"\n" +
            "        },\n" +
            "        \"rawLocation\": null,\n" +
            "        \"networkStatus\": \"INACTIVE\",\n" +
            "        \"changedOn\": 1512616145082,\n" +
            "        \"ipAddress\": [],\n" +
            "        \"userName\": \"\",\n" +
            "        \"ssId\": \"\",\n" +
            "        \"sourceTimestamp\": \"1512616145082\",\n" +
            "        \"band\": \"UNKNOWN\",\n" +
            "        \"apMacAddress\": \"\",\n" +
            "        \"dot11Status\": \"UNKNOWN\",\n" +
            "        \"manufacturer\": \"Cisco Systems, Inc\",\n" +
            "        \"areaGlobalIdList\": null,\n" +
            "        \"detectingControllers\": \"10.22.243.211\",\n" +
            "        \"bytesSent\": 0,\n" +
            "        \"bytesReceived\": 0,\n" +
            "        \"guestUser\": false\n" +
            "    }\n" +
            "]";
 */
}