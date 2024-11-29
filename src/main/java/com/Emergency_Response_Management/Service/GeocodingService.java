//package com.Emergency_Response_Management.Service;
////
////import com.fasterxml.jackson.databind.JsonNode;
////import com.fasterxml.jackson.databind.ObjectMapper;
////import org.springframework.stereotype.Service;
////
////import java.io.BufferedReader;
////import java.io.InputStreamReader;
////import java.net.HttpURLConnection;
////import java.net.URI;
////import java.net.URL;
////import java.net.http.HttpClient;
////import java.net.http.HttpRequest;
////import java.net.http.HttpResponse;
////
////@Service
////public class GeocodingService {
////
//////        public String getAddressFromCoordinates(Double latitude, Double longitude) {
//////            try {
//////                // Construct the URL for reverse geocoding
//////                URL url = new URL(String.format(
//////                        "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f",
//////                        latitude, longitude
//////                ));
//////
//////                // Open the connection
//////                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//////                conn.setRequestMethod("GET");
//////                System.out.println("Request Method: " + conn.getRequestMethod());
//////                System.out.println("Requesting URL: " + url);
//////
//////                // Check if the response code is 200 (OK)
//////                if (conn.getResponseCode() != 200) {
//////                    System.out.println("Error: Unable to get address. Response code: " + conn.getResponseCode());
//////                    return "Address cannot be found";
//////                }
//////
//////                // Read the response from the connection
//////                BufferedReader reader = new BufferedReader(
//////                        new InputStreamReader(conn.getInputStream())
//////                );
//////                StringBuilder response = new StringBuilder();
//////                String line;
//////                while ((line = reader.readLine()) != null) {
//////                    response.append(line);
//////                }
//////                reader.close();
//////
//////                // Parse the JSON response using Jackson
//////                ObjectMapper mapper = new ObjectMapper();
//////                JsonNode root = mapper.readTree(response.toString());
//////
//////                // Extract the "display_name" field for the address
//////                String address = root.path("display_name").asText("Address is not found");
//////
//////                return address;
//////
//////            } catch (Exception e) {
//////                // Handle exceptions (e.g., network errors, parsing errors)
//////                e.printStackTrace();
//////                return "Address not found";
//////            }
//////        }
////        public String getAddressFromCoordinates(Double latitude, Double longitude) {
////            try {
////                // Log the coordinates to ensure they are correct
////                System.out.println("Latitude: " + latitude + ", Longitude: " + longitude);
////
////                // Create the URL string for the OpenStreetMap API
////                String apiUrl = String.format("https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f", latitude, longitude);
////                System.out.println("Requesting URL: " + apiUrl);
////
////                // Create a connection to the API
////                URL url = new URL(apiUrl);
////                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
////                conn.setRequestMethod("GET");
////                conn.setRequestProperty("User-Agent", "IncidentReportingSystem");
////
////                // Check if the request was successful
////                int responseCode = conn.getResponseCode();
////                System.out.println("Response Code: " + responseCode);
////                if (responseCode != 200) {
////                    System.out.println("Address not found, given response code: " + responseCode);
////                    return "Address cannot be found";
////                }
////
////                // Read the response from the API
////                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
////                StringBuilder response = new StringBuilder();
////                String line;
////                while ((line = reader.readLine()) != null) {
////                    response.append(line);
////                }
////                reader.close();
////
////                // Log the raw JSON response
////                System.out.println("API Response: " + response.toString());
////
////                // Parse the JSON response using Jackson
////                ObjectMapper mapper = new ObjectMapper();
////                JsonNode root = mapper.readTree(response.toString());
////
////                // Retrieve the 'display_name' field from the response
////                String displayName = root.path("display_name").asText("Address not found");
////                System.out.println("Display Name: " + displayName);
////
////                return displayName;
////
////            } catch (Exception e) {
////                e.printStackTrace();
////                return "Address not found";
////            }
////        }
////}
////
////
////
////
////
//
//import org.springframework.stereotype.Service;
//
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.net.http.HttpHeaders;
//import java.util.logging.ConsoleHandler;
//@Service
//public class GeocodingService {
//
//    public String getAddressFromCoordinates(Double latitude, Double longitude) {
//        try {
//            HttpClient client = HttpClient.newHttpClient();
//            String apiUrl = String.format("https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f", latitude, longitude);
//            System.out.println("apiUrl: "+apiUrl);
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(apiUrl))
//                    .header("User-Agent", "IncidentReportingSystem")
//                    .build();
//            System.out.println("request: "+request);
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//System.out.println("response: "+response);
//            if (response.statusCode() != 200) {
//                System.out.println(" YARRR Address cannot be found");
//                return "Address cannot be found";
//            }
//
//            return response.body(); // You can still parse the response with Jackson here
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Address not found";
//        }
//    }
//}
//
