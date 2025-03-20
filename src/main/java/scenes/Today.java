package scenes;

import hourlyWeather.hourlyPeriod;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import api.MyWeatherAPI;
import utils.ForecastIconMatcher;
import utils.WeatherUse;
import weather.Period;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Today implements WeatherUse {

    private MyWeatherAPI myWeatherAPI;

    TextField temperature, weather, heading, wind;
    VBox vbox1;
    HBox hbox1, myButtons, hourlyHBox, hourlyLabelBox, iconHBox;
    Button buttonF;
    Button buttonC;
    Button scene2Button;

    public void setScene(Stage primaryStage) throws Exception {
        ArrayList<Period> forecast = myWeatherAPI.getForecast();
        if (forecast == null) {
            throw new RuntimeException("Forecast did not load");
        }
        ArrayList<hourlyPeriod> hourlyForecast = myWeatherAPI.getHourlyForecast();
        if (hourlyForecast == null) {
            throw new RuntimeException("Hourly forecast did not load");
        }

        heading = new TextField();
        temperature = new TextField();
        weather = new TextField();
        wind = new TextField();
        buttonF = new Button("°F");
        buttonF.setDisable(true);
        buttonC = new Button("°C");

        scene2Button = new Button("See the next 3-Day Forecast");

        temperature.setEditable(false);
        weather.setEditable(false);
        heading.setEditable(false);
        wind.setEditable(false);

        heading.setFont(Font.font("Lucida Sans Unicode", 18));
        temperature.setFont(Font.font("Lucida Sans Unicode", 16));
        weather.setFont(Font.font("Lucida Sans Unicode", 15));
        wind.setFont(Font.font("Lucida Sans Unicode", 14));
        buttonF.setFont(Font.font("Lucida Sans Unicode", 14));
        buttonC.setFont(Font.font("Lucida Sans Unicode", 14));
        scene2Button.setFont(Font.font("Lucida Sans Unicode", 16));

        heading.setPrefSize(500, 50);
        temperature.setPrefSize(115, 50);
        weather.setPrefSize(285, 50);
        wind.setPrefSize(200, 50);

        heading.setAlignment(javafx.geometry.Pos.CENTER);
        temperature.setAlignment(javafx.geometry.Pos.CENTER);
        weather.setAlignment(javafx.geometry.Pos.CENTER);
        wind.setAlignment(javafx.geometry.Pos.CENTER);

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formattedTime = DateTimeFormatter.ofPattern("EEEE, M/d");

        heading.setText("Today's Weather, " + formattedTime.format(currentTime) + " (" + myWeatherAPI.city + ", " + myWeatherAPI.state + ")");
        temperature.setText(" Temp: " + forecast.get(0).temperature + "°F");
        weather.setText(forecast.get(0).shortForecast + "!");
        wind.setText("Wind: " + forecast.get(0).windSpeed + "(" + forecast.get(0).windDirection + ")");

        buttonF.setOnAction(e -> {
            int tempFahrenheit = forecast.get(0).temperature;
            temperature.setText("Temp: " + tempFahrenheit + "°F");
            buttonF.setDisable(true);
            buttonC.setDisable(false);

            for (int i = 0; i < 10; i++) {
                VBox hourlyVBox = (VBox) hourlyHBox.getChildren().get(i);
                TextField tempField = (TextField) hourlyVBox.getChildren().get(2);
                int hourlyTempFahrenheit = hourlyForecast.get(i).temperature;
                tempField.setText(hourlyTempFahrenheit + "°F");

                tempField.setEditable(false);
                tempField.setFont(Font.font("Lucida Sans Unicode", FontPosture.REGULAR, 14));
                tempField.setStyle("-fx-text-fill: black; -fx-border-width: 1px;");
                tempField.setPrefSize(100, 50);
                tempField.setAlignment(javafx.geometry.Pos.CENTER);
            }
        });

        buttonC.setOnAction(e -> {
            int tempCelsius = (int) ((forecast.get(0).temperature - 32) * (5.0 / 9.0));
            temperature.setText("Temp: " + tempCelsius + "°C");
            temperature.deselect();
            buttonC.setDisable(true);
            buttonF.setDisable(false);

            for (int i = 0; i < 10; i++) {
                VBox hourlyVBox = (VBox) hourlyHBox.getChildren().get(i);
                TextField tempField = (TextField) hourlyVBox.getChildren().get(2);
                int hourlyTempCelsius = (int) ((hourlyForecast.get(i).temperature - 32) * (5.0 / 9.0));
                tempField.setText(hourlyTempCelsius + "°C");

                tempField.setEditable(false);
                tempField.setFont(Font.font("Lucida Sans Unicode", FontPosture.REGULAR, 14));
                tempField.setStyle("-fx-text-fill: black; -fx-border-width: 1px;");
                tempField.setPrefSize(100, 50);
                tempField.setAlignment(javafx.geometry.Pos.CENTER);
            }
            vbox1.requestFocus();
        });


        hourlyHBox = new HBox(10);

        for (int i = 0; i < 10; i++) {
            Date time = hourlyForecast.get(i).startTime;
            SimpleDateFormat formattedHours = new SimpleDateFormat("ha");
            formattedHours.setTimeZone(TimeZone.getTimeZone(myWeatherAPI.getTimezone()));
            String formattedHour = formattedHours.format(time);

            TextField hourField = new TextField(formattedHour);
            hourField.setEditable(false);
            hourField.setFont(Font.font("Lucida Sans Unicode",  14));
            hourField.setStyle("-fx-text-fill: black; -fx-border-width: 1px; -fx-padding: 2;");
            hourField.setPrefSize(100, 30);
            hourField.setAlignment(javafx.geometry.Pos.CENTER);

            TextField tempField = new TextField();
            tempField.setText(hourlyForecast.get(i).temperature + "°F");
            tempField.setEditable(false);
            tempField.setFont(Font.font("Lucida Sans Unicode", 14));
            tempField.setStyle("-fx-text-fill: black; -fx-border-width: 1px; -fx-padding: 2;");
            tempField.setPrefSize(100, 50);
            tempField.setAlignment(javafx.geometry.Pos.CENTER);

            String iconURL = ForecastIconMatcher.matchShortForecastToIcon(hourlyForecast.get(i).shortForecast, hourlyForecast.get(i).isDaytime);
            Image iconImage = new Image("./icons/" + iconURL, true);
            ImageView viewIcon = new ImageView(iconImage);
            viewIcon.setFitWidth(27);
            viewIcon.setFitHeight(27);
            viewIcon.setPreserveRatio(true);
            viewIcon.setBlendMode(BlendMode.DIFFERENCE);

            HBox iconContainer = new HBox(viewIcon);
            iconContainer.setAlignment(javafx.geometry.Pos.CENTER);
            iconContainer.setStyle("-fx-padding: 5;");

            VBox hourlyVBox = new VBox(5, hourField, iconContainer, tempField);
            hourlyVBox.setAlignment(javafx.geometry.Pos.CENTER);
            hourlyVBox.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 5;");
            hourlyHBox.getChildren().add(hourlyVBox);
        }

        TextField hourlyLabel = new TextField("\uD83D\uDD50 Hourly Forecast:");
        hourlyLabel.setEditable(false);
        hourlyLabel.setFont(Font.font("Lucida Sans Unicode", 18));
        hourlyLabel.setStyle("-fx-text-fill: black; -fx-border-color: black; -fx-border-width: 1px; -fx-padding: 12;");
        hourlyLabel.setAlignment(javafx.geometry.Pos.CENTER);
        hourlyLabel.setPrefSize(200, 50);

        hourlyLabelBox = new HBox(hourlyLabel);

        String iconURL = ForecastIconMatcher.matchShortForecastToIcon(forecast.get(0).shortForecast, forecast.get(0).isDaytime);
        Image iconImage = new Image("./icons/" + iconURL, true);
        ImageView iconView = new ImageView(iconImage);
        iconView.setFitWidth(27);
        iconView.setFitHeight(28);
        iconView.setPreserveRatio(true);
        iconView.setBlendMode(BlendMode.DIFFERENCE);

        iconHBox = new HBox(iconView);
        iconHBox.setAlignment(javafx.geometry.Pos.CENTER);
        iconHBox.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: black; -fx-border-width: 1px;");

        myButtons = new HBox(10, buttonF, buttonC);
        hbox1 = new HBox(iconHBox, temperature, weather, wind);
        HBox.setHgrow(iconHBox, Priority.ALWAYS);
        HBox.setHgrow(temperature, Priority.ALWAYS);
        HBox.setHgrow(weather, Priority.ALWAYS);
        HBox.setHgrow(wind, Priority.ALWAYS);
        HBox scene2ButtonWrapper = new HBox(scene2Button);
        scene2Button.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(scene2Button, Priority.ALWAYS);
        vbox1 = new VBox(15, heading, myButtons, hbox1, hourlyLabelBox, hourlyHBox, scene2ButtonWrapper);


        heading.setStyle("-fx-text-fill: black; -fx-border-color: black; -fx-border-width: 1px; -fx-padding: 12;");
        temperature.setStyle(" -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 1px; -fx-padding: 12;");
        weather.setStyle("-fx-text-fill: black; -fx-border-color: black; -fx-border-width: 1px; -fx-padding: 12;");
        wind.setStyle("-fx-text-fill: black; -fx-border-color: black; -fx-border-width: 1px; -fx-padding: 12;");
        buttonF.setStyle("-fx-background-color: #89cee6; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 1px; -fx-padding: 10;");
        buttonC.setStyle("-fx-background-color: #89cee6; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 1px; -fx-padding: 10;");
        scene2Button.setStyle("-fx-background-color: #89cee6; -fx-text-fill: black; -fx-border-width: 1px; -fx-padding: 10;");

        scene2Button.setOnAction(this::onThreeDayForecastButtonClick);

        BorderPane root = new BorderPane(vbox1);


        var buttonMap = new Button("Map");
        buttonMap.setOnAction(this::onButtonMapClick);
        buttonMap.setStyle("-fx-background-color: #89cee6; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 1px; -fx-padding: 10;");
        buttonMap.setFont(Font.font("Lucida Sans Unicode", 14));


        var aaa = new HBox(buttonMap);
        root.setTop(aaa);
        

        root.setStyle("-fx-background-color: white;");
        vbox1.setPadding(new Insets(20));
        vbox1.setMaxWidth(800);
        vbox1.setMaxHeight(600);
        vbox1.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        vbox1.requestFocus();
    }

    private void onThreeDayForecastButtonClick(ActionEvent event)  {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ThreeDayForecast.fxml"));
            loader.setControllerFactory(c -> {
                try {
                    Object controller = c.getDeclaredConstructor().newInstance();
                    if (controller instanceof WeatherUse) {
                        ((WeatherUse)controller).setMyWeatherAPI(myWeatherAPI);
                    }
                    return controller;
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });

            Scene newScene = new Scene(loader.load());

            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
            // Add additional error handling as needed
        }
    }

    private void onButtonMapClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Map.fxml"));
            loader.setControllerFactory(c -> {
                try {
                    Object controller = c.getDeclaredConstructor().newInstance();
                    if (controller instanceof WeatherUse) {
                        ((WeatherUse)controller).setMyWeatherAPI(myWeatherAPI);
                    }
                    return controller;
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });

            Scene newScene = new Scene(loader.load());

            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setMyWeatherAPI(MyWeatherAPI myWeatherAPI) {
        this.myWeatherAPI = myWeatherAPI;
    }
}