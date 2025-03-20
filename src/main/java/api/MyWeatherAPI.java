package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hourlyWeather.hourlyPeriod;
import hourlyWeather.hourlyRoot;
import point.PointResponse;
import weather.Period;
import weather.Root;
import weather.WeatherAPI;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;

public class MyWeatherAPI extends WeatherAPI {

    private double latitude = 41.8781;
    private double longitude = -87.6298;

    private String forecastURL = "https://api.weather.gov/gridpoints/LOT/77,70/forecast";
    private String hourlyForecastURL = "https://api.weather.gov/gridpoints/LOT/77,70/forecast/hourly";

    public String city = "Cicero";
    public String state = "IL";
    private String timezone = "America/Chicago";

    public void changeLocation(double latitude, double longitude) throws RuntimeException {

        latitude = latitude * 10000;
        latitude = Math.round(latitude);
        latitude = latitude / 10000;

        longitude = longitude * 10000;
        longitude = Math.round(longitude);
        longitude = longitude / 10000;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.gov/points/" + latitude + "," + longitude))
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException("Request failed", e);
        }

        if(response.statusCode() != 200) throw new RuntimeException("Please click on a valid area of the US");

        PointResponse r = getPointResponse(response.body());

        if(r == null) throw new RuntimeException("Response is null");

        this.forecastURL = r.properties.forecast;
        this.hourlyForecastURL = r.properties.forecastHourly;
        this.city = r.properties.relativeLocation.properties.city;
        this.state = r.properties.relativeLocation.properties.state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = r.properties.timeZone;
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

    public ArrayList<hourlyPeriod> getHourlyForecast() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(hourlyForecastURL))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        hourlyRoot r = getHourlyObject(response.body());
        if(r == null){
            System.err.println("Failed to parse JSon");
            return null;
        }
        return r.properties.periods;
    }

    private static hourlyRoot getHourlyObject(String json){
        ObjectMapper om = new ObjectMapper();
        hourlyRoot toRet = null;
        try {
            toRet = om.readValue(json, hourlyRoot.class);
            ArrayList<hourlyPeriod> p = toRet.properties.periods;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return toRet;
    }

    public static PointResponse getPointResponse(String json) {
        ObjectMapper om = new ObjectMapper();

        try {
            return om.readValue(json, PointResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTimezone() {
        return timezone;
    }
}
