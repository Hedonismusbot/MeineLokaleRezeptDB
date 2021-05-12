package util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileHandler {

	// ----------- Variablen -----------

	private static FileHandler instance;
	private static Logger log = LogManager.getLogger();

	private FileChooser fileChooser = new FileChooser();
	private Desktop desktop = Desktop.getDesktop();
	private File selectedDirectory;

	// ----------- Methoden -----------

	/**
	 * Öffnet File Chooser Fenster um Datei zu öffnen
	 * 
	 * @param stage
	 */
	public void openFileChooserToOpen(Stage stage) {
		log.info("File Chooser Öffnen geöffnet");
		configureFileChooser(fileChooser);
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			openFile(file);
		}

	}

	/**
	 * Öffnet File Chooser Fenster um Dateipfad zu bekommen
	 * 
	 * @param stage
	 */
	public String openFileChooserGetPath(Stage stage) {
		log.info("File Chooser Pfad geöffnet");
		configureFileChooser(fileChooser);
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {

			log.debug("Pfad = " + file.getAbsolutePath());
			return file.getAbsolutePath();

		} else {
			log.info("Pfad = --");
			return "";
		}

	}

	/**
	 * Öffnet File Chooser Fenster um Ordnerpfad zu bekommen
	 * 
	 * @param stage
	 */
	public String openFileChooserSetHomePath(Stage stage) {
		log.info("Directory Chooser  geöffnet");
		DirectoryChooser directoryChooser = new DirectoryChooser();
		selectedDirectory = directoryChooser.showDialog(stage);
		if (selectedDirectory != null) {
			selectedDirectory.getAbsolutePath();
			log.debug("Pfad = " + selectedDirectory.getAbsolutePath());
		}

		return null;

	}

	private void openFile(File file) {
		try {
			desktop.open(file);
		} catch (IOException ex) {
			log.error(ex);
		}
	}

	/**
	 * Setup FileChooser (Title /Filter/Home Verzeichnis)
	 * 
	 * @param fileChooser
	 */
	private void configureFileChooser(final FileChooser fileChooser) {
		log.info("Setup FileChooser");
		// Titel setzen
		fileChooser.setTitle("Bild aussuchen");

		String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
		log.debug(currentPath + "\\pictures");
		// Verzeichnis auswählen wo geöffnet wird
		fileChooser.setInitialDirectory(new File(currentPath + "\\pictures"));

		// fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		// fileChooser.setInitialDirectory(this.selectedDirectory);

		// Filter Datei auswahl
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"),
				new FileChooser.ExtensionFilter("BMP", "*.bmp"));

	}

	public synchronized static FileHandler getInstance() {
		if (instance == null) {
			instance = new FileHandler();
		}
		return instance;
	}

}
