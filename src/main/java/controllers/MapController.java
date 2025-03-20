package controllers;


import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import api.MyWeatherAPI;
import scenes.Today;
import utils.WeatherUse;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class MapController implements WeatherUse, Initializable {



    private MyWeatherAPI myWeatherAPI;

    @FXML private ImageView map;

    @FXML private StackPane mapContainer;

    @FXML private Label zoomLabel;
    @FXML private Button zoomInButton;
    @FXML private Button zoomOutButton;
    @FXML private Slider zoomSlider;
    @FXML private Button resetZoomButton;
    @FXML private VBox controlBox;

    @FXML private HBox toast;
    @FXML private Label toastLabel;
    @FXML private AnchorPane toastAnchor;

    private static final double MIN_ZOOM = 0.5;
    private static final double MAX_ZOOM = 4.0;
    private static final double STEP = 0.1;
    private double zoom = 1.0;

    private double originalFitWidth;
    private double originalFitHeight;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hideToast();

        originalFitWidth = map.getFitWidth();
        originalFitHeight = map.getFitHeight();

        mapContainer.setFocusTraversable(true);
        mapContainer.requestFocus();

        zoomLabel.textProperty().bind(
                Bindings.createStringBinding(() -> {
                    NumberFormat format = NumberFormat.getPercentInstance();
                    return format.format(zoomSlider.getValue());
                }, zoomSlider.valueProperty())
        );

        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            applyZoom(newVal.doubleValue());
        });

        mapContainer.setOnKeyPressed((e) -> {

            if(e.getCode() == KeyCode.W) {
                double newTranslateY = map.getTranslateY() + 10.0;
                map.setTranslateY(newTranslateY);
            }

            if(e.getCode() == KeyCode.A) {
                double newTranslateX = map.getTranslateX() + 10.0;
                map.setTranslateX(newTranslateX);
            }

            if(e.getCode() == KeyCode.S) {
                double newTranslateY = map.getTranslateY() - 10.0;
                map.setTranslateY(newTranslateY);
            }

            if(e.getCode() == KeyCode.D) {
                double newTranslateX = map.getTranslateX() - 10.0;
                map.setTranslateX(newTranslateX);
            }

        });
    }

    @FXML
    public void handleMapClick(MouseEvent mouseEvent) {
        System.out.println("Loading...");
        showToast("Loading...", "red");

        double initialLat = 90;
        double initialLon = -180;

        double endLat = 0.10;
        double endLon = -40.40;

        var mouseX = mouseEvent.getX();
        var mouseY = mouseEvent.getY();

        var mapWidth = map.getBoundsInLocal().getWidth();
        var mapHeight = map.getBoundsInLocal().getHeight();

        double xRatio = mouseX / mapWidth;
        double yRatio = mouseY / mapHeight;

        double lon = initialLon + xRatio * Math.abs(endLon - initialLon);
        double lat = initialLat - yRatio * Math.abs(endLat - initialLat);

        try {
            myWeatherAPI.changeLocation(lat, lon);
        } catch (RuntimeException e) {
            System.out.println("Error changing location...");
            showToast("Please click on an area in the US", "red");
            return;
        }

        System.out.println("Redirecting to Map...");
        showToast("Redirecting...", "red");

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

    @FXML
    public void zoomIn(ActionEvent actionEvent) {
        double currentZoom = zoomSlider.getValue();
        zoomSlider.setValue(Math.min(currentZoom + STEP, MAX_ZOOM));
    }

    @FXML
    public void zoomOut(ActionEvent actionEvent) {
        double currentZoom = zoomSlider.getValue();
        zoomSlider.setValue(Math.max(currentZoom - STEP, MIN_ZOOM));
    }

    @FXML
    public void resetZoom() {
        zoomSlider.setValue(1.0);
        map.setLayoutX(0);
        map.setLayoutY(0);
        map.setTranslateX(0);
        map.setTranslateY(0);
    }

    private void applyZoom(double zoomLevel) {
        map.setFitWidth(originalFitWidth * zoomLevel);
        map.setFitHeight(originalFitHeight * zoomLevel);

        if (zoomLevel <= 1.0) {
            map.setLayoutX(0);
            map.setLayoutY(0);
        }
    }

    private void showToast(String message, String color) {
        toastAnchor.setVisible(true);
        toastAnchor.setMouseTransparent(true);
        toastLabel.setText(message);
        toast.setStyle("-fx-background-color: " + color + ";");
        toastLabel.requestLayout();
        toast.requestLayout();
    }

    private void hideToast() {
        toastAnchor.setVisible(false);
    }
}
