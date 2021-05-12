package application;
	
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	private static Logger log = LogManager.getRootLogger();
	@Override
	public void start(Stage primaryStage) {
	
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("/gui/rezept.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			primaryStage.setTitle("Meine Rezept Datenbank");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		log.info("Start App");
		launch(args);
		log.info("Ende App");
	}
	




	
}
