package cr.player;

import java.io.IOException;

import cr.player.model.Database;
import cr.player.model.Music;
import cr.player.view.PlayerController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private static Database db;
    
    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<Music> musicTable = FXCollections.observableArrayList();

	
    /**
     * Constructor
     */
    public MainApp() {
    	db = new Database();
    	db.musicTable.forEach((key, value) -> {
    		musicTable.add(new Music(key, value));
		});
    	
    }
    
    /**
     * Returns the data as an observable list of Persons. 
     * @return
     */
    public ObservableList<Music> getMusicData() {
        return musicTable;
    }
    
    public Database getDB() {
        return db;
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("4Music");

        initRootLayout();

        showPlayerView();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPlayerView() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PlayerView.fxml"));
            AnchorPane playerview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(playerview);
            
         // Give the controller access to the main app.
	        PlayerController controller = loader.getController();
	        controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
        db.SaveDB();
    }
    
    
}