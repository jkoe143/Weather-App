package controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import weather.Period;
import weather.WeatherAPI;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControllerOne implements Initializable {

    @FXML
    TextField a;
    @FXML
    TextField b;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Task<ArrayList<Period>> weatherTask = new Task<>() {
            @Override
            protected ArrayList<Period> call() throws Exception {
                var forecast = WeatherAPI.getForecast("LOT", 77, 70);
                if (forecast == null){
                    throw new RuntimeException("Forecast did not load");
                }
                return forecast;
            }
        };

        weatherTask.setOnSucceeded(e -> {
            ArrayList<Period> forecast = weatherTask.getValue();
            if (forecast != null && !forecast.isEmpty()) {
                a.setText("Today's weather: " + forecast.get(0).temperature);
                b.setText(forecast.get(0).shortForecast);
            }
        });

        weatherTask.setOnFailed(e -> {
            a.setText("Error loading weather");
            b.setText("");
        });

        Thread weatherThread = new Thread(weatherTask);
        weatherThread.setDaemon(true);
        weatherThread.start();
    }
}
