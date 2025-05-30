package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TableCellForecastController {


    @FXML private Label day;
    @FXML private Label date;

    @FXML private Label dayTemperature;
    @FXML private Label nightTemperature;

    @FXML private ImageView dayIcon;
    @FXML private ImageView nightIcon;

    @FXML private Label forecastDay;
    @FXML private Label forecastNight;

    @FXML private Label WindDirectionDay;
    @FXML private Label WindDirectionNight;

    @FXML private Label dayPrecipitation;
    @FXML private Label nightPrecipitation;

    private int dayTemperatureF;
    private int nightTemperatureF;

    public void setForecast(String day,
                            String date,
                            String forecastDay,
                            int dayTemperature,
                            int dayPrecipitation,
                            String dayWindDirection,
                            String dayIconName,
                            String forecastNight,
                            int nightTemperature,
                            int nightPrecipitation,
                            String nightWindDirection,
                            String nightIconName) {
        this.day.setText(day);
        this.date.setText(date);

        this.dayTemperatureF = dayTemperature;
        this.nightTemperatureF = nightTemperature;

        this.dayTemperature.setText(dayTemperature + "°F");
        this.nightTemperature.setText(nightTemperature + "°F");

        this.forecastDay.setText("Day " + forecastDay);
        this.forecastNight.setText("Night " + forecastNight);

        this.WindDirectionDay.setText(dayWindDirection);
        this.WindDirectionNight.setText(nightWindDirection);

        this.dayPrecipitation.setText(dayPrecipitation + "%");
        this.nightPrecipitation.setText(nightPrecipitation + "%");

        try {
            // Load day icon from URL
            Image dayImage = new Image("./icons/" + dayIconName, true);
            dayIcon.setImage(dayImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // Load night icon from URL
            Image nightImage = new Image("./icons/" + nightIconName, true);
            nightIcon.setImage(nightImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getDayTempF() {
        return dayTemperatureF;
    }

    public int getNightTempF() {
        return nightTemperatureF;
    }

    public void setDayTemp(String temperature) {
        this.dayTemperature.setText(temperature);
    }

    public void setNightTemp(String temperature) {
        this.nightTemperature.setText(temperature);
    }

}
