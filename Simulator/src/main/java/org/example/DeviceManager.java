package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeviceManager {

//    private List<Long> getDeviceIdsFromApi() throws Exception {
//
//        URL url = new URL("http://device.localhost/device/ids");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        String inputLine;
//        StringBuilder content = new StringBuilder();
//
//        while ((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
//        }
//
//        in.close();
//
//        String response = content.toString();
//        return parseDeviceIds(response);
//    }

//    private List<Long> parseDeviceIds(String response) {
//        List<Long> deviceIds = new ArrayList<>();
//
//        String[] ids = response.replace("[", "").replace("]", "").replace("\"", "").split(",");
//
//        for (String id : ids) {
//            try {
//                deviceIds.add(Long.parseLong(id.trim()));
//            } catch (NumberFormatException e) {
//                System.err.println("Invalid ID format: " + id);
//            }
//        }
//
//        return deviceIds;
//    }
    public boolean existDeviceId(Long deviceId) throws Exception {
//        List<Long> idDevices= getDeviceIdsFromApi();
//        if(idDevices.contains(deviceId))
//            return true;
//        return false;
        return true;

    }
}
