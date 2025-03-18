package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import utils.ForecastIconMatcher;
import weather.Period;
import weather.WeatherAPI;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ThreeDayForecastController implements Initializable {


    @FXML private HBox forecastDay1;
    @FXML private TableCellForecastController forecastDay1Controller;
    @FXML private HBox forecastDay2;
    @FXML private TableCellForecastController forecastDay2Controller;
    @FXML private HBox forecastDay3;
    @FXML private TableCellForecastController forecastDay3Controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Period> periods = WeatherAPI.getForecast("LOT", 77, 70);
        if (periods == null) throw new RuntimeException("forecast periods is null");

        var day1 = periods.get(1);
        var night1 = periods.get(2);
        setForecast(day1, night1, forecastDay1Controller);

        var day2 = periods.get(3);
        var night2 = periods.get(4);
        setForecast(day2, night2, forecastDay2Controller);

        var day3 = periods.get(5);
        var night3 = periods.get(6);
        setForecast(day3, night3, forecastDay3Controller);

    }

    private void setForecast(Period day, Period night, TableCellForecastController forecast) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");

        int dayPrecipitation = day.probabilityOfPrecipitation.value;
        int nightPrecipitation = night.probabilityOfPrecipitation.value;

        int dayTemperature = day.temperature;
        int nightTemperature = night.temperature;

        String dayWindDirection = day.windDirection;
        String nightWindDirection = night.windDirection;

        String dayForecast = createForecastString(day);
        String nightForecast = createForecastString(night);

        String iconDay = ForecastIconMatcher.matchShortForecastToIcon(day.shortForecast, true);
        String iconNight = ForecastIconMatcher.matchShortForecastToIcon(night.shortForecast, false);

        String date = dateFormat.format(day.startTime);
        forecast.setForecast(
                day.name.substring(0, 3),
                date,
                dayForecast,
                dayTemperature,
                dayPrecipitation,
                dayWindDirection,
                iconDay,
                nightForecast,
                nightTemperature,
                nightPrecipitation,
                nightWindDirection,
                iconNight
        );
    }

    private String createForecastString(Period period) {
        String windSpeedText = period.windSpeed.replace(" to ", "-");
        return "Wind: " + windSpeedText;
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/a.fxml"));
            Scene newScene = new Scene(loader.load());

            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
            // Add additional error handling as needed
        }
    }
}
