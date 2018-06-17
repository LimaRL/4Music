package cr.player.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Database {
	public Map<String,String> musicTable = new HashMap<String,String>();
	String pathDB;

	public Database() {
		try {
			this.pathDB = new java.io.File(".").getCanonicalPath() + "/database/database.txt";
			this.LoadDB();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void LoadDB() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(this.pathDB));
			String lineKey = reader.readLine();
			String lineValue = reader.readLine();
			while (lineKey != null) {
				this.insert(lineKey, lineValue);
				//this.musics.add(lineKey);
				lineKey = reader.readLine();
				lineValue = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void SaveDB() {
		PrintWriter writer;
		try {

			writer = new PrintWriter(this.pathDB, "UTF-8");
			this.musicTable.forEach((key, value) -> {
				writer.println(key);
				writer.println(value);
			});
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
	}
	
	
	
	public void insert(String key, String value) {
		this.musicTable.put(key, value);
	}

	public void delete(String key) {
		this.musicTable.remove(key);
	}

}
