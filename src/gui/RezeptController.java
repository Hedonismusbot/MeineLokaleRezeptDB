package gui;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.IngredientSQLiteDAO;
import dao.RecipeSQLiteDAO;
import db.DBConnectSQLite;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.AddedIngredient;
import model.Ingredient;
import model.Recipe;
import util.FileHandler;
import util.FreshRecipeCreator;
import util.Validator;

public class RezeptController {

	// ------------ FXML Variablen ------------
	@FXML
	private BorderPane rootBorderPane;

	@FXML
	private TableView<Recipe> ar_TableView;

	@FXML
	private TableColumn<Recipe, String> ar_nameCol;

	@FXML
	private TableColumn<Recipe, String> ar_descriptionCol;

	@FXML
	private TableColumn<Recipe, Double> ar_portionCol;
	@FXML
	private TableColumn<Recipe, String> ar_picturePathCol;

	@FXML
	private TableColumn<Recipe, Integer> ar_durationCol;

	@FXML
	private TableColumn<Recipe, Integer> ar_idJavaCol;

	@FXML
	private TableColumn<Recipe, ImageView> ar_pictureCol;

	@FXML
	private Tab nr_Tab;

	@FXML
	private TextField nr_TitelTextField;

	@FXML
	private TextField nr_PicPathTextField;

	@FXML
	private TextField nr_DurationTextField;

	@FXML
	private TextField nr_PortionTextField;

	@FXML
	private TextArea nr_descriptionTextArea;

	@FXML
	private TextField nr_IngredientAmountTextField;

	@FXML
	private ImageView nr_ImageView;

	@FXML
	private ComboBox<String> nr_IngredientCmbBox;

	@FXML
	private TableView<AddedIngredient> nr_TableView;

	@FXML
	private TableColumn<AddedIngredient, String> nr_NameCol;

	@FXML
	private TableColumn<AddedIngredient, String> nr_AmountCol;

	@FXML
	private TableColumn<AddedIngredient, String> nr_UnitCol;

	@FXML
	private Label nr_AmountIngredientLabel;

	@FXML
	private TextField ai_NameTextField;

	@FXML
	private TextField ai_UnitTextField;

	@FXML
	private TableView<Ingredient> ai_TableView;

	@FXML
	private TableColumn<Ingredient, Integer> ai_IdCol;

	@FXML
	private TableColumn<Ingredient, String> ai_NameCol;
	@FXML
	private TableColumn<Ingredient, String> ai_UnitCol;

	@FXML
	private Label infoLabel;

	// ------------ Variablen ------------

	private static Logger log = LogManager.getLogger();
	private static RezeptController rezeptController = new RezeptController();
	private RecipeSQLiteDAO recipe_dao;
	private IngredientSQLiteDAO ingredient_dao;
	private FileHandler fileHandler_dao;
	private FreshRecipeCreator freshRecipeCreator;

	// ------------ FXML on Action Methoden ------------

	@FXML
	void ar_onAct_FindAllRecipes(ActionEvent event) {
		log.debug("On Action Find All Rezepte");
		refreshTableViewAllRecipes();

	}

	@FXML
	void nr_onAct_addPicture(ActionEvent event) {
		log.info("On Action Bild hinzufügen");
		fileHandler_dao = FileHandler.getInstance();
		freshRecipeCreator = FreshRecipeCreator.getInstance();

		Stage stage = (Stage) rootBorderPane.getScene().getWindow();
		String filepath = fileHandler_dao.openFileChooserGetPath(stage);
		log.debug("BildPfad :" + filepath);
		
		if (!(filepath == null || filepath.length() == 0)) {
			freshRecipeCreator.getFreshRecipe().setImagePath(filepath);
			Path path = Paths.get(filepath);
			// filepath = "file:pictures/" + path.getFileName();
			filepath = "file:/"+filepath;
			nr_ImageView.setImage(new Image(filepath, 50.0, 50.0, true, true));

		}

	}

	@FXML
	void nr_onAct_CmbBoxPick(ActionEvent event) {
		if (nr_Tab.isSelected()) {
			log.debug("On Action ComboBox Zutat Pick");
			freshRecipeCreator = FreshRecipeCreator.getInstance();
			Ingredient selectedIng = freshRecipeCreator.getAllIngredientList()
					.get(nr_IngredientCmbBox.getSelectionModel().getSelectedItem());
			nr_AmountIngredientLabel.setText(selectedIng.getUnit());
		}
	}

	@FXML
	void nr_onAct_addIngredient(ActionEvent event) {
		log.debug("On Action Add neue Zutat");

		freshRecipeCreator = FreshRecipeCreator.getInstance();

		if (validateAddIngredientToRecipe()) {
			log.info("Validierung Menge neue Zutat OK");
			if (freshRecipeCreator.getFreshRecipe().getIngriedients().size() < freshRecipeCreator.getMAXINGRDIENTS()) {
				Double amount = Double.valueOf(nr_IngredientAmountTextField.getText());
				String ingredientname = nr_IngredientCmbBox.getSelectionModel().getSelectedItem();

				freshRecipeCreator.getFreshRecipe().getIngriedients()
						.add(freshRecipeCreator.getAllIngredientList().get(ingredientname));
				freshRecipeCreator.getFreshRecipe().getAmountIngriedients().add(amount);

				refreshTableViewRecipeIngriedients();
				log.info("Zutat: " + freshRecipeCreator.getAllIngredientList().get(ingredientname) + " hinzugefügt");
			} else {
				log.info("Maximale Zutaten Anzahl hinzugefügt");
				setInfoMsg("Maximal Zutaten Anzahl erreicht");
			}

		}

	}

	@FXML
	void nr_onAct_addRecipe(ActionEvent event) {
		log.debug("On Action Add neues Rezept");

		String name = "";
		String description = "";
		Integer dauer = 0;
		Double portion = 0.0;
		FreshRecipeCreator rc = FreshRecipeCreator.getInstance();

		if (validateNewRecipe()) {
			log.info("Validierung neues Rezept OK");
			name = nr_TitelTextField.getText();
			description = nr_descriptionTextArea.getText();
			portion = Double.valueOf(nr_PortionTextField.getText());
			if (Validator.validateInputEmpty(nr_DurationTextField.getText())) {
				dauer = 0;
			} else {
				dauer = Integer.valueOf(nr_DurationTextField.getText());
			}
			// Daten setzen
			rc.getFreshRecipe().setName(name);
			rc.getFreshRecipe().setDescription(description);
			rc.getFreshRecipe().setDuration(dauer);
			rc.getFreshRecipe().setPortion(portion);

			// Rezept zu DatenBank hinzugefügt
			recipe_dao.addNewRecipe(rc.getFreshRecipe());

			// GUI Resetten
			nr_TitelTextField.clear();
			nr_descriptionTextArea.clear();
			nr_DurationTextField.clear();
			nr_PortionTextField.clear();
			nr_PicPathTextField.clear();
			nr_IngredientAmountTextField.clear();
			nr_TableView.getItems().clear();
			rc.setFreshRecipe(new Recipe());
			nr_ImageView.setImage(null);

			// Rezept Anzeige erneuern
			refreshTableViewAllRecipes();
			log.info("Rezept erfolgreich hinzugefügt und TableView aktualisiert");
		} else {
			name = nr_TitelTextField.getText();
			description = nr_descriptionTextArea.getText();

			if (Validator.validateInputEmpty(nr_PortionTextField.getText())) {
				log.info("User Input neues Rezept nicht OK: name: {} Beschreibung: {} Portion: {} -- ", name,
						description, dauer, portion);

			} else {
				log.info("User Input neues Rezept nicht OK: name: {} Beschreibung: {} Dauer: {} Portion: {} ", name,
						description, dauer, portion);
			}
		}

	}

	@FXML
	void ai_onAct_AddIngrdient(ActionEvent event) {
		log.debug("On Action Add neue Zutat");
		String name = "";
		String unit = "";
		if (validateNewIngredient()) {
			log.info("Validierung neue Zutat OK");

			name = ai_NameTextField.getText();
			unit = ai_UnitTextField.getText();

			// Rezept zu DatenBank hinzugefügt
			ingredient_dao.addNewIngredient(new Ingredient(name, unit));
			// Textfelder leeren
			ai_NameTextField.clear();
			ai_UnitTextField.clear();

			ai_TableView.getItems().setAll(ingredient_dao.findAllIngredients());
			refreshComboBoxIngriedents();

		} else {
			log.trace("Keine Änderung durchgeführt");
		}
	}

	@FXML
	void ai_onAct_FindAllIngrdients(ActionEvent event) {
		log.debug("On Action Find All Zutaten");
		ai_TableView.getItems().setAll(ingredient_dao.findAllIngredients());

	}

	// ------------ FXML TableView on Edit Methoden ------------

	@FXML
	void ar_onEditString(CellEditEvent<Recipe, String> ce) {
		if(!Validator.validateCharactersWithSpace(ce.getNewValue())) {
			setInfoMsg("Keine Sonderzeichen erlaubt");
		}else if (!ce.getNewValue().equals(ce.getOldValue())) {

			Integer idjava = ce.getRowValue().getIdJava();
			String columnTitle = ce.getTableColumn().getText();
			String newValue = ce.getNewValue();
			log.info("OnAction Commit in IdJava " + idjava + " Spalte " + columnTitle + " String: Value New "
					+ newValue);
			recipe_dao.updateRecipeString(columnTitle, newValue, idjava);
			refreshTableViewAllRecipes();

		} else {
			setInfoMsg("Keine Änderung durchgeführt");
			log.trace("Keine Änderung durchgeführt");
		}

	}

	@FXML
	void ar_onEditDouble(CellEditEvent<Recipe, Double> ce) {
		if (!ce.getNewValue().equals(ce.getOldValue())) {
			Integer idjava = ce.getRowValue().getIdJava();
			String columnTitle = ce.getTableColumn().getText();
			Double newValue = ce.getNewValue();
			log.info("OnAction Commit in IdJava " + idjava + " Spalte " + columnTitle + " Double: Value New "
					+ newValue);
			recipe_dao.updateRecipeDouble(columnTitle, newValue, idjava);
			refreshTableViewAllRecipes();

		} else {
			setInfoMsg("Keine Änderung durchgeführt");
			log.trace("Keine Änderung durchgeführt");
		}

	}

	@FXML
	void ar_onEditInt(CellEditEvent<Recipe, Integer> ce) {
		if (!ce.getNewValue().equals(ce.getOldValue())) {
			Integer idjava = ce.getRowValue().getIdJava();
			String columnTitle = ce.getTableColumn().getText();
			Integer newValue = ce.getNewValue();
			log.info(
					"OnAction Commit in IdJava " + idjava + "Spalte " + columnTitle + " Double: Value New " + newValue);
			recipe_dao.updateRecipeInteger(columnTitle, newValue, idjava);
			refreshTableViewAllRecipes();

		} else {
			setInfoMsg("Keine Änderung durchgeführt");
			log.trace("Keine Änderung durchgeführt");
		}

	}

	@FXML
	void ai_onEditString(CellEditEvent<Ingredient, String> ce) {
		if(!Validator.validateCharactersWithSpace(ce.getNewValue())) {
			setInfoMsg("Keine Sonderzeichen erlaubt");
		}else if (!ce.getNewValue().equals(ce.getOldValue())) {
			Integer idjava = ce.getRowValue().getIdJava();
			String columnTitle = ce.getTableColumn().getText();
			String newValue = ce.getNewValue();
			log.info("OnAction Commit in IdJava " + idjava + "Spalte" + columnTitle + "String: Value New" + newValue);
			ingredient_dao.updateIngredientString(columnTitle, newValue, idjava);
			ai_TableView.setItems(FXCollections.observableArrayList(ingredient_dao.findAllIngredients()));
			refreshComboBoxIngriedents();
		} else {
			setInfoMsg("Keine Änderung durchgeführt");
			log.trace("Keine Änderung durchgeführt");
		}

	}

	// ------------ Methoden ------------

	@FXML
	public void initialize() {
		log.info("Init...");
		setInfoMsg("Init...");
		// SQL Verbindung erstellen und DAO erstellen
		recipe_dao = new RecipeSQLiteDAO();
		ingredient_dao = new IngredientSQLiteDAO();
		DBConnectSQLite con = DBConnectSQLite.getInstance();
		con.connect();
		recipe_dao.setCon(con.getCon());
		ingredient_dao.setCon(con.getCon());

		// GUI Einstellen
		setupTableViewAllRecipes();
		setupTableViewAllIngredients();
		refreshComboBoxIngriedents();
		setupTableViewIngredientsNewRecipe();
		setInfoMsg("Init...Beendet");

	}
	
	/**
	 * Aktualisiert TableView alle Zutaten
	 */
	private void refreshTableViewRecipeIngriedients() {

		log.info("Refresh Table View Zutaten neues Rezepte ");

		freshRecipeCreator = FreshRecipeCreator.getInstance();

		ArrayList<AddedIngredient> list = new ArrayList<AddedIngredient>();
		for (int i = 0; i < freshRecipeCreator.getFreshRecipe().getIngriedients().size(); i++) {
			Ingredient ing = freshRecipeCreator.getFreshRecipe().getIngriedients().get(i);
			Double amount = freshRecipeCreator.getFreshRecipe().getAmountIngriedients().get(i);
			list.add(new AddedIngredient(ing.getName(), ing.getUnit(), amount));
		}

		nr_TableView.setItems(FXCollections.observableArrayList(list));

	}

	/**
	 * Aktualisiert Zutatenliste in ComboBox Neues Rezept und in FreshRecipeCreator
	 */
	private void refreshComboBoxIngriedents() {
		log.info("ComboBox Zutaten refreshed");
		freshRecipeCreator = FreshRecipeCreator.getInstance();
		ArrayList<String> guiList = new ArrayList<String>();
		List<Ingredient> allingredientList = ingredient_dao.findAllIngredients();
		HashMap<String, Ingredient> map = new HashMap<String, Ingredient>();
		for (Ingredient ingredient : allingredientList) {

			guiList.add(ingredient.getName());
			map.put(ingredient.getName(), ingredient);
		}

		nr_IngredientCmbBox.getItems().setAll(guiList);
		freshRecipeCreator.setAllIngredientList(map);
	}

	/**
	 * Setup TableView alle Rezepte
	 */
	private void setupTableViewAllRecipes() {
		log.info("SetUp Table View Alle Rezepte Start");
		// Initialisert Spalten
		ar_idJavaCol.setCellValueFactory(new PropertyValueFactory<>("idjava"));
		ar_nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		ar_nameCol.setStyle("-fx-alignment: CENTER;");
		ar_descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
		ar_descriptionCol.setStyle("-fx-alignment: CENTER;");
		ar_portionCol.setCellValueFactory(new PropertyValueFactory<>("portion"));
		ar_portionCol.setStyle("-fx-alignment: CENTER;");
		ar_picturePathCol.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
		ar_durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
		ar_durationCol.setStyle("-fx-alignment: CENTER;");
		ar_pictureCol.setCellValueFactory(new PropertyValueFactory<>("imageView"));
		ar_pictureCol.setStyle("-fx-alignment: CENTER;");

		// Aktualisiert TableView
		refreshTableViewAllRecipes();

		// Context Menu - Delete (Rechts CLICK)
		ContextMenu cm = new ContextMenu();
		MenuItem deleteItem = new MenuItem("Delete");
		deleteItem.setOnAction(e -> {
			log.debug("On Action Rezept löschen");
			Recipe selectedRecipe = ar_TableView.getSelectionModel().getSelectedItem();
			boolean deleted = recipe_dao.deleteRecipe(selectedRecipe.getIdJava());
			if (deleted) {
				ar_TableView.getItems().remove(selectedRecipe);
				log.info("Rezept gelöscht! IdJava:" + selectedRecipe.getIdJava());
			}
		});
		cm.getItems().add(deleteItem);
		ar_TableView.setContextMenu(cm);

		// Zellen editierbar machen ( TableView editable machen )
		ar_nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		ar_descriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());
		ar_portionCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		ar_durationCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		ar_picturePathCol.setCellFactory(TextFieldTableCell.forTableColumn());
		log.info("SetUp Table View Alle Rezepte Start");
	}

	/**
	 * Aktualiesiert TableView alle Rezepte (auch Bilder)
	 */
	private void refreshTableViewAllRecipes() {
		log.info("Refresh Table View Alle Rezepte Start");
		//TODO Dateipfad dynamisch machen 
		String imagepath = "";
		List<Recipe> list = recipe_dao.findAllRecipe();

		for (Recipe recipe : list) {
			for (Ingredient ingredient : recipe.getIngriedients()) {
				if (ingredient.getIdJava() != 0) {
					Ingredient dbingredient = ingredient_dao.findSingleIngredient(ingredient.getIdJava());
					ingredient.setName(dbingredient.getName());
					ingredient.setUnit(dbingredient.getUnit());

				}
			}

			log.debug(recipe.toString());

			imagepath = recipe.getImagePath(); // z.B.="E:\Java02-JRE15\Projekt_J2_RezeptDB\pictures\strassburger-auflauf.png";
			if (!(imagepath == null || imagepath.length() == 0)) {
				Path path = Paths.get(imagepath);
				try {
					imagepath = "file:pictures/" + path.getFileName();
					log.info("imagepath:" + imagepath);
					// Image img = new Image("file:pictures/strassburger-auflauf.png", 160.0 , 160.0, true , true);
					Image img = new Image(imagepath, 160.0, 160.0, true, true);

					recipe.setImageView(new ImageView(img));
				} catch (Exception e) {
					log.error(e);
				}

			}
		}

		// Liste mit Grafischer Oberfläche verbinden
		ar_TableView.setItems(FXCollections.observableArrayList(list));
		log.info("Refresh Table View Alle Rezepte Ende");
	}

	/**
	 * Setup Table View alle Zuaten
	 */
	private void setupTableViewAllIngredients() {
		log.info("SetUp Table View Alle Zutaten Start");
		// Initialisert Spalten
		ai_IdCol.setCellValueFactory(new PropertyValueFactory<>("idJava"));
		ai_NameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		ai_NameCol.setStyle("-fx-alignment: CENTER;");
		ai_UnitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));
		ai_UnitCol.setStyle("-fx-alignment: CENTER;");

		// Verbindet tableView mit Observable list
		ai_TableView.setItems(FXCollections.observableArrayList(ingredient_dao.findAllIngredients()));

		// Context Menu - Delete (Rechts CLICK)
		ContextMenu cm = new ContextMenu();
		MenuItem deleteItem = new MenuItem("Delete");
		deleteItem.setOnAction(e -> {
			log.debug("On Action Zutat löschen");
			Ingredient selectedIngrdient = ai_TableView.getSelectionModel().getSelectedItem();
			boolean deleted = ingredient_dao.deleteIngredient(selectedIngrdient.getIdJava());
			if (deleted) {
				ai_TableView.getItems().remove(selectedIngrdient);
				refreshComboBoxIngriedents();
				log.info("Zutat gelöscht! IdJava:" + selectedIngrdient.getIdJava());
			}
		});
		cm.getItems().add(deleteItem);
		ai_TableView.setContextMenu(cm);

		// Zellen editierbar machen ( TableView editable machen )
		ai_NameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		ai_UnitCol.setCellFactory(TextFieldTableCell.forTableColumn());

		log.info("SetUp Table View Alle Zutaten Ende");
	}

	/**
	 * Setup Table View Zutaten in neuem Rezept
	 */
	private void setupTableViewIngredientsNewRecipe() {
		log.info("SetUp Table View Zutaten neues Rezept Start");
		freshRecipeCreator = FreshRecipeCreator.getInstance();
		// Initialisert Spalten
		nr_NameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		nr_AmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
		nr_UnitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));

		// Context Menu - Delete (Rechts CLICK)
		ContextMenu cm = new ContextMenu();
		MenuItem deleteItem = new MenuItem("Delete");
		deleteItem.setOnAction(e -> {
			log.debug("On Action Zutat löschen");
			System.out.println("TODO:LÖSCHEN");
			/*
			 * Ingredient selectedIngrdient =
			 * ai_TableView.getSelectionModel().getSelectedItem(); boolean deleted =
			 * ingredient_dao.deleteIngredient(selectedIngrdient.getIdJava()); if (deleted )
			 * { ai_TableView.getItems().remove(selectedIngrdient);
			 * log.info("Zutat gelöscht! IdJava:" + selectedIngrdient.getIdJava()); }
			 */
		});
		cm.getItems().add(deleteItem);
		nr_TableView.setContextMenu(cm);

		log.info("SetUp Table View Zutaten neues Rezept Ende");
	}

	/**
	 * Set Label and Fade from Bottom Info Label
	 */
	private void setInfoMsg(String msg) {

		infoLabel.setOpacity(1);
		FadeTransition fade = new FadeTransition(Duration.seconds(1), infoLabel);
		fade.setToValue(0);
		fade.setDelay(Duration.seconds(4));
		infoLabel.setText(msg);
		fade.play();

	}

	/**
	 *
	 * @return if user input ok return = true
	 */
	private boolean validateNewIngredient() {
		boolean result = true;

		// ------------ Unit ------------
		if (!Validator.validateLength(ai_UnitTextField.getText(), 0, 25)) {
			setInfoMsg("Einheit zu lang !");
			result = false;
		}
		if (!Validator.validateCharacters(ai_UnitTextField.getText())) {
			setInfoMsg("Keine Sonderzeichen in Einheit!");
			result = false;
		}
		if (!Validator.validateInputNotEmpty(ai_UnitTextField.getText())) {
			setInfoMsg("Einheit wird benötigt");
			result = false;
		}

		// ------------ Name ------------
		if (!Validator.validateCharacters(ai_NameTextField.getText())) {
			setInfoMsg("Keine Sonderzeichen im Namen!");
			result = false;
		}

		if (!Validator.validateLength(ai_NameTextField.getText(), 1, 50)) {
			setInfoMsg("Name zu lang!");
			result = false;
		}

		if (!Validator.validateInputNotEmpty(ai_NameTextField.getText())) {
			setInfoMsg("Name wird benötigt");
			result = false;
		}
		return result;
	}

	/**
	 * 
	 * @return if user input ok return = true
	 */
	private boolean validateNewRecipe() {
		boolean result = true;

		// ------------ Description ------------
		if (Validator.validateInputNotEmpty(nr_descriptionTextArea.getText())) {
			if (!Validator.validateLength(nr_descriptionTextArea.getText(), 1, 500)) {
				setInfoMsg("Beschreibung zu lang!");
				log.debug("Beschreibung zu lang!");
				result = false;
			}
			if (!Validator.validateCharactersWithSpace(nr_descriptionTextArea.getText())) {
				setInfoMsg("Keine Sonderzeichen in der Beschreibung!");
				log.debug("Keine Sonderzeichen in der Beschreibung!");
				result = false;
			}
		}
		// ------------ Duration ------------
		if (Validator.validateInputNotEmpty(nr_DurationTextField.getText())) {
			log.debug("Dauer vorhanden: " + Validator.validateInteger(nr_DurationTextField.getText()));
			if (!Validator.validateInteger(nr_DurationTextField.getText())) {
				setInfoMsg("Ungültige Dauer");
				log.debug("Ungültige Dauer");
				result = false;
			}
		}
		// ------------ Portion ------------
		if (!Validator.validateInputNotEmpty(nr_PortionTextField.getText())) {
			setInfoMsg("Portionen werden benötigt");
			log.debug("Portionen werden benötigt");
			result = false;
		}
		if (!Validator.validateDouble(nr_PortionTextField.getText())) {
			setInfoMsg("Ungültige Portionen");
			log.debug("Ungültige Portionen");
			result = false;
		}

		// ------------ Titel ------------
		if (!Validator.validateLength(nr_TitelTextField.getText(), 1, 50)) {
			setInfoMsg("Titel zu lang !");
			log.debug("Titel zu lang !");
			result = false;
		}
		if (!Validator.validateCharacters(nr_TitelTextField.getText())) {
			setInfoMsg("Keine Sonderzeichen im Titel!");
			log.debug("Keine Sonderzeichen im Titel!");
			result = false;
		}
		if (!Validator.validateInputNotEmpty(nr_TitelTextField.getText())) {
			setInfoMsg("Titel wird benötigt");
			log.debug("Titel wird benötigt");
			result = false;
		}

		return result;
	}

	/**
	 * 
	 * @return if user input ok return = true
	 */
	private boolean validateAddIngredientToRecipe() {
		boolean result = true;

		if (!Validator.validateDouble(nr_IngredientAmountTextField.getText())) {
			setInfoMsg("Ungültige Menge");
			log.debug("Ungültige Menge");
			result = false;
		}

		if (!Validator.validateInputNotEmpty(nr_IngredientAmountTextField.getText())) {
			setInfoMsg("Menge wird benötigt");
			log.debug("Ungültige Menge");
			result = false;
		}

		return result;

	}

	public static RezeptController getInstance() {
		return rezeptController;
	}

}
