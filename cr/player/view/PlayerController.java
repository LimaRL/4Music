package cr.player.view;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cr.player.MainApp;
import cr.player.model.Music;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Alert.AlertType;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class PlayerController {
	@FXML
	private TableView<Music> musicTable;
	@FXML
	private TableColumn<Music, String> musicName;

	// referencia da aplicação main
	private MainApp mainApp;

	private FileInputStream FIS;
	private BufferedInputStream BIS;

	private Player player;
	private Music selectedMusic;

	private long lastPosition;
	private long musicSize;

	private String path;

	// flags
	private boolean isPlaying = false;
	private boolean wasPaused = false;

	/**
	 * constructor
	 */
	public PlayerController() {}

	/**
	 * Stop
	 */
	public void Stop() {
		if(this.player != null) {
			this.player.close();
			this.isPlaying = false;
			this.wasPaused = false;
			this.lastPosition = 0;
			this.musicSize = 0;
		}
	}

	/**
	 * Pause
	 */
	public void Pause() {
		if(this.wasPaused != true) {
			if(this.player != null) {
				try {
					this.lastPosition = FIS.available();
					this.wasPaused = true; 
					this.player.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Play Button
	 */
	public void PlayButton() {
		if(!this.isPlaying || SameMTest() ) {
			this.isPlaying = true;
			this.Play();
		}
		if(this.wasPaused) {
			this.wasPaused = false;
			this.Resume();
		}
	}
	
	public boolean SameMTest() {
		if(this.selectedMusic != null) {
			Music playingMusica = musicTable.getSelectionModel().getSelectedItem();
			String value = playingMusica.getMusic();
			if(this.selectedMusic.getMusic().equals(value)){
				return false;
			}else {
				this.Stop();
			}
		}
		return true;
	}

	/**
	 * Play
	 */
	public void Play() {
		this.selectedMusic = musicTable.getSelectionModel().getSelectedItem();
	    if (selectedMusic != null) {
	    	String music = selectedMusic.getMusic();
			try {
				this.path = new java.io.File(".").getCanonicalPath() + "/musics/" + music;
				this.FIS = new FileInputStream(path);
				this.BIS = new BufferedInputStream(this.FIS);
				this.player = new Player(this.BIS);
				this.musicSize = this.FIS.available();
			} catch (JavaLayerException | IOException e) {
				e.printStackTrace();
			}
			new Thread() {
				@Override
				public void run() {
					try {
						player.play();
						if(player.isComplete()) {
							Stop();
						}
					} catch (JavaLayerException e) {
						e.printStackTrace();
					}
				}
			}.start();
	    }else {
	    	this.isPlaying = false;
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(mainApp.getPrimaryStage());
	        alert.setTitle("No Selection");
	        alert.setHeaderText("No Music Selected");
	        alert.setContentText("Please select a music in the list.");
	        alert.showAndWait();
	    }
	}

	/**
	 * Resume
	 */
	public void Resume() {
		try {
			this.FIS = new FileInputStream(this.path);
			this.BIS = new BufferedInputStream(this.FIS);
			this.player = new Player(this.BIS);
			this.FIS.skip(this.musicSize - this.lastPosition);
		} catch (JavaLayerException | IOException e) {
			e.printStackTrace();
		}
		new Thread() {
			@Override
			public void run() {
				try {
					player.play();
					if(player.isComplete()) {
						Stop();
					}
				} catch (JavaLayerException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	/**
	 * Called when the user clicks on the delete button.
	 */
	@FXML
	private void deleteMusic() {
	    int selectedIndex = musicTable.getSelectionModel().getSelectedIndex();
	    String music = musicTable.getSelectionModel().getSelectedItem().getMusic();
	    if (selectedIndex >= 0) {
	    	musicTable.getItems().remove(selectedIndex);
	    	MainApp.db.delete(music);
	    	this.Stop();
	    } else {
	        // Nothing selected.
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(mainApp.getPrimaryStage());
	        alert.setTitle("Unable to delete music");
	        alert.setHeaderText("Unknown error");
	        alert.showAndWait();
	    }
	}
	
	/**
	 * 
	 */
	@FXML
	private void insertMusic() {
	    FileChooser file = new FileChooser();	    
	    try {
	    	File newFile = new File(new java.io.File(".").getCanonicalPath() + "/musics/");
			file.setInitialDirectory(newFile);
			file.getExtensionFilters().addAll(
					new  ExtensionFilter("mp3 FILES",".mp3","*.mp3","mp3"));
			
			File selectedFile = file.showOpenDialog(null);
			
			if(selectedFile != null) {
				String path = selectedFile.getAbsolutePath();
				String name = selectedFile.getName();
				
				musicTable.getItems().add(new Music(name, path));
				
				MainApp.db.insert(name, path);
			}else {
				// Nothing selected.
		        Alert alert = new Alert(AlertType.WARNING);
		        alert.initOwner(mainApp.getPrimaryStage());
		        alert.setTitle("No Selection");
		        alert.setHeaderText("No music found");
		        alert.showAndWait();
			}
			
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("fffff");
		}
	}
	
	
	
	/**
	 * initialize
	 */
	@FXML
	private void initialize() {}

	/**
	 * setMainApp
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp app) {
		// Inicializa a coluna das musicas
		musicName.setCellValueFactory(
				cellData -> cellData.getValue().musicProperty());
		this.mainApp = app;
		// Adiciona dados da observable list na tabela 
		musicTable.setItems(MainApp.musicTable);
	}
}
