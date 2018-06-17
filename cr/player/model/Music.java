package cr.player.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Music {
	private final StringProperty music;
    private final StringProperty path;
    
    public Music() {
        this(null, null);
    }

    /**
     * Constructor with some initial data.
     * 
     * @param firstName
     * @param lastName
     */
    public Music(String music, String path) {
        this.music = new SimpleStringProperty(music);
        this.path = new SimpleStringProperty(path);
    }
    
    public String getMusic() {
        return music.get();
    }

    public void setMusic(String music) {
        this.music.set(music);
    }

    public StringProperty musicProperty() {
        return music;
    }
    
    public String getPath() {
        return path.get();
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public StringProperty pathProperty() {
        return path;
    }
}
