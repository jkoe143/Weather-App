package controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import scenes.Today;

import java.io.IOException;

public class MapController {

    @FXML private ImageView map;

    @FXML
    public void handleMapClick(MouseEvent mouseEvent) {
        var x = mouseEvent.getSceneX();
        var y = mouseEvent.getSceneY();

        System.out.println("x: " + x + " y: " + y);

        try {
            Stage stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
            Today today = new Today();

            today.setScene(stage);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
