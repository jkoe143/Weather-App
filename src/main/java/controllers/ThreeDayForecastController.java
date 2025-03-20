package controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import api.MyWeatherAPI;
import scenes.Today;
import utils.ForecastIconMatcher;
import utils.WeatherUse;
import weather.Period;
import weather.WeatherAPI;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ThreeDayForecastController implements Initializable, WeatherUse {

    private MyWeatherAPI myWeatherAPI;

    @FXML private HBox forecastDay1;
    @FXML private TableCellForecastController forecastDay1Controller;
    @FXML private HBox forecastDay2;
    @FXML private TableCellForecastController forecastDay2Controller;
    @FXML private HBox forecastDay3;
    @FXML private TableCellForecastController forecastDay3Controller;
    @FXML private Button buttonF;
    @FXML private Button buttonC;

    private boolean isFahrenheit = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Period> periods = myWeatherAPI.getForecast();
        if (periods == null) throw new RuntimeException("forecast periods is null");

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");

        String currentDate = dateFormat.format(new Date());

        int i = 0;
        String date = dateFormat.format(periods.get(i).startTime);

        while (currentDate.equals(date)) {
            i++;
            date = dateFormat.format(periods.get(i).startTime);
        }

        var day1 = periods.get(i);
        var night1 = periods.get(i + 1);
        setForecast(day1, night1, forecastDay1Controller);

        var day2 = periods.get(i + 2);
        var night2 = periods.get(i + 3);
        setForecast(day2, night2, forecastDay2Controller);

        var day3 = periods.get(i + 4);
        var night3 = periods.get(i + 5);
        setForecast(day3, night3, forecastDay3Controller);

        buttonF.setDisable(true);
        buttonC.setDisable(false);


        buttonF.setOnAction(e -> {
            this.isFahrenheit = true;
            buttonF.setDisable(true);
            buttonC.setDisable(false);
            updateControllers();
        });

        buttonC.setOnAction(e -> {
            this.isFahrenheit = false;
            buttonF.setDisable(false);
            buttonC.setDisable(true);
            updateControllers();
        });
    }


    private void updateControllers() {
        updateTemperatures(forecastDay1Controller);
        updateTemperatures(forecastDay2Controller);
        updateTemperatures(forecastDay3Controller);
    }

    private void updateTemperatures(TableCellForecastController forecastController) {
        int dayTemperature = forecastController.getDayTempF();
        int nightTemperature = forecastController.getNightTempF();

        if (!isFahrenheit) {
            dayTemperature = (int) ((dayTemperature - 32) * (5.0 / 9.0));
            nightTemperature = (int) ((nightTemperature - 32) * (5.0 / 9.0));
        }

        String unit = isFahrenheit ? "°F" : "°C";
        forecastController.setDayTemp(dayTemperature + unit);
        forecastController.setNightTemp(nightTemperature + unit);
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
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

            Today today = new Today();

            today.setMyWeatherAPI(myWeatherAPI);
            today.setScene(stage);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setMyWeatherAPI(MyWeatherAPI myWeatherAPI) {
        this.myWeatherAPI = myWeatherAPI;
    }
}
