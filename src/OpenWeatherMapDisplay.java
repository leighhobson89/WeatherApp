import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

public class OpenWeatherMapDisplay implements WeatherDisplay {

    private static final String API_KEY = "1e0cd5d76098e28a884b484fbaae1395";
    @Override
    public void displayCurrentWeatherForCity(String city) {
        String API_ENDPOINT = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;
        try {
            // Send HTTP GET request to OpenWeatherMap API endpoint
            URL url = new URL(API_ENDPOINT);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read response from API endpoint
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse response JSON and extract current weather data
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject currentWeather = jsonResponse.getJSONArray("weather").getJSONObject(0);
            String description = currentWeather.getString("description");
            double temperature = jsonResponse.getJSONObject("main").getDouble("temp");
            String country = jsonResponse.getJSONObject("sys").getString("country");

            // Display current weather data
            System.out.println("Current weather for " + city + ", " + country + ":");
            System.out.println("Description: " + description);
            System.out.println("Temperature: " + String.format("%.1f", (temperature - 273)) + "ºC");

        } catch (Exception e) {
            System.err.println("Error retrieving current weather data for " + city + ": " + e.getMessage());
        }
    }

    public static String inputCity(String prompt) {
        // Create a new Scanner object to read user input
        Scanner scanner = new Scanner(System.in);

        // Print the prompt message
        System.out.print(prompt + " ");

        // Read the user's input as a string
        String userInput = scanner.nextLine();

        // Return the user's input
        return userInput;
    }

}
