public class Main {

    public static void main(String[] args) {
        WeatherDisplay weatherDisplay = new OpenWeatherMapDisplay();
        String userInput = OpenWeatherMapDisplay.inputCity("Please enter a city to get its weather:");
        weatherDisplay.displayCurrentWeatherForCity(userInput);
    }
}
