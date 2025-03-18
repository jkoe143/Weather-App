import weather.Period;
import weather.Root;
import weather.WeatherAPI;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class MyWeatherAPI extends WeatherAPI {

    private double latitude = 41.8781;
    private double longitude = -87.6298;

    private String forecastURL = "https://api.weather.gov/gridpoints/LOT/77,70/forecast";
    private String city = "Cicero";
    private String state = "IL";

    public boolean changeLocation(double latitude, double longitude){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.gov/points/" + latitude + "%2C" + longitude))
                .build();

        return true;
    }

    public ArrayList<Period> getForecast() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(forecastURL))
                //.method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Root r = getObject(response.body());

        if(r == null){
            System.err.println("Failed to parse JSon");
            return null;
        }

        return r.properties.periods;
    }

}
