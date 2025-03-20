import javafx.application.Application;

import javafx.stage.Stage;
import api.MyWeatherAPI;
import scenes.Today;


public class JavaFX extends Application {

	private final MyWeatherAPI weatherAPI = new MyWeatherAPI();

	@Override
	public void start(Stage stage) throws Exception {
		Today today = new Today();

		today.setMyWeatherAPI(weatherAPI);
		today.setScene(stage);

		stage.setTitle("Weather App");
		stage.show();
	}

	public static void main(String[] args) { launch(args); }
}
