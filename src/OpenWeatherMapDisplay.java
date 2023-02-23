import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.*;
import javax.swing.border.Border;

import org.json.JSONObject;

public class OpenWeatherMapDisplay extends JFrame implements WeatherDisplay {

    private static final String API_KEY = "1e0cd5d76098e28a884b484fbaae1395";
    private JTextField cityField;
    private JLabel cityLabel;
    private JLabel descriptionLabel;
    private JLabel temperatureLabel;

    public OpenWeatherMapDisplay() {
        // Create UI components
        JPanel inputPanel = new JPanel();
        cityField = new JTextField(20);
        inputPanel.add(new JLabel("Enter city name:"));
        inputPanel.add(cityField);

        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));

        cityLabel = new JLabel();
        descriptionLabel = new JLabel();
        temperatureLabel = new JLabel();

        // Create an instance of EmptyBorder with a left padding of 10 pixels
        Border paddingBorder = BorderFactory.createEmptyBorder(0, 43, 0, 0);
        Border paddingBorderCity = BorderFactory.createEmptyBorder(20, 43, 0, 0);

        // Add the paddingBorder to each JLabel in the displayPanel
        cityLabel.setBorder(paddingBorderCity);
        descriptionLabel.setBorder(paddingBorder);
        temperatureLabel.setBorder(paddingBorder);

        displayPanel.add(cityLabel);
        displayPanel.add(descriptionLabel);
        displayPanel.add(temperatureLabel);

        JButton button = new JButton("Get Weather");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get weather data for the entered city
                String city = cityField.getText();
                displayCurrentWeatherForCity(city);
            }
        });

        // Add a KeyListener to the cityField
        cityField.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                // If the Enter key is pressed, simulate a button click
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    button.doClick();
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        // Add components to the frame
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(displayPanel, BorderLayout.CENTER);
        getContentPane().add(button, BorderLayout.SOUTH);

        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setTitle("OpenWeatherMap Display");
        setVisible(true);
    }

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
            cityLabel.setText(city + ", " + country);
            descriptionLabel.setText("Weather: " + description);
            temperatureLabel.setText("Temperature: " + String.format("%.1f", (temperature - 273)) + "ºC");

        } catch (Exception e) {
            System.err.println("Error retrieving current weather data for " + city + ": " + e.getMessage());
        }
    }
}
