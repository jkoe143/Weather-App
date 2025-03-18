import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.stage.Stage;


public class JavaFX extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("I'm a professional Weather App!");
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ThreeDayForecast.fxml"));
		Scene scene = new Scene(fxmlLoader.load());

		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) { launch(args); }
}
