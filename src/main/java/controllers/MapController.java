package controllers;


import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import api.MyWeatherAPI;
import scenes.Today;
import utils.WeatherUse;

import java.io.IOException;

public class MapController implements WeatherUse {

    private MyWeatherAPI myWeatherAPI;

    @FXML private ImageView map;

    @FXML
    public void handleMapClick(MouseEvent mouseEvent) {

        double initialLat = 75;
        double initialLon = -180;

        double totalLon = 9 * 15;
        double totalLat = 4 * 15;

        var mouseX = mouseEvent.getX();
        var mouseY = mouseEvent.getY();

        var mapWidth = map.getBoundsInLocal().getWidth();
        var mapHeight = map.getBoundsInLocal().getHeight();

        double xRatio = mouseX / mapWidth;
        double yRatio = mouseY / mapHeight;

        double lon = initialLon + xRatio * totalLon;
        double lat = initialLat - yRatio * totalLat;

        System.out.println("lat: " + lat + " lon: " + lon);

        myWeatherAPI.changeLocation(lat, lon);

        try {
            Stage stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
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
