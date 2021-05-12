package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import db.DBConnectSQLite;
import model.Ingredient;
import model.Recipe;

public class RecipeSQLiteDAO implements RecipeDAO {

	private static Logger log = LogManager.getLogger();
	private Connection con;
	private static Recipe lastEntry;

	public RecipeSQLiteDAO() {
		con = DBConnectSQLite.getInstance().getCon();
	}

	@Override
	public List<Recipe> findAllRecipe() {
		ArrayList<Recipe> list = new ArrayList<>();

		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM rezepte");
			log.debug(statement.toString());
			fillResultList(list, rs);
			log.info("SELECT alle Rezepte Erfolgreich abgefragt ");

		} catch (SQLException e) {
			log.debug(e);
		}
	
			

		return list;

	}

	private String prepareNewRecipeInsertStatement(int countIngredients) {
		String statement = "";

		switch (countIngredients) {
		case 1:
			statement = ("INSERT INTO rezepte "
					+ "(idjava,name ,beschreibung,dauergesamt,portion,bildpfad,zutat_1,menge_1)"
					+ " VALUES (?,?,?,?,?,?,?,?)");
			break;
		case 2:
			statement = ("INSERT INTO rezepte "
					+ "(idjava,name ,beschreibung,dauergesamt,portion,bildpfad,zutat_1,menge_1,zutat_2,menge_2)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?)");
			break;

		case 3:
			statement = ("INSERT INTO rezepte "
					+ "(idjava,name ,beschreibung,dauergesamt,portion,bildpfad,zutat_1,menge_1,zutat_2,menge_2,zutat_3,menge_3)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
			break;
		case 4:
			statement = ("INSERT INTO rezepte "
					+ "(idjava,name ,beschreibung,dauergesamt,portion,bildpfad,zutat_1,menge_1,zutat_2,menge_2,zutat_3,menge_3,zutat_4,menge_4)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			break;
		case 5:
			statement = ("INSERT INTO rezepte "
					+ "(idjava,name ,beschreibung,dauergesamt,portion,bildpfad,zutat_1,menge_1,zutat_2,menge_2,zutat_3,menge_3,zutat_4,menge_4,zutat_5,menge_5)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			break;
		default:
			statement = ("INSERT INTO rezepte " + "(idjava,name ,beschreibung,dauergesamt,portion,bildpfad)"
					+ " VALUES (?,?,?,?,?,?)");
			break;
		}
		log.debug("SQL Statement :" + statement);
		return statement;
	}

	@Override
	public boolean addNewRecipe(Recipe rz) {
		updateLastEntry();
		try {
			log.info("Neues Rezept wird angelegt ");
			String statement = prepareNewRecipeInsertStatement(rz.getIngriedients().size());
			PreparedStatement ps = con.prepareStatement(statement);
			// Stammdaten
			ps.setInt(1, getLastEntry().getIdJava() + 1);
			ps.setString(2, rz.getName());
			ps.setString(3, rz.getDescription());
			ps.setInt(4, rz.getDuration());
			ps.setDouble(5, rz.getPortion());
			ps.setString(6, rz.getImagePath());
	
			log.trace("IdJava: " +(getLastEntry().getIdJava() + 1)+ "Name: " +rz.getName()+ "Beschreibung: " 
					+rz.getDescription()+ "Dauer: " +rz.getDuration()+ "Portion: " +rz.getPortion()+ "BildPfad: " +rz.getImagePath() );
			// Zutaten
			int j = 0;
			for (int i = 0; i < rz.getIngriedients().size(); i++) {
				ps.setInt(7 + j, rz.getIngriedients().get(i).getIdJava());
				ps.setDouble(8 + j, rz.getAmountIngriedients().get(i));

				log.trace(" Zutat: i:{} ID:{} Menge:{}  ", i, rz.getIngriedients().get(i).getIdJava(),
						rz.getAmountIngriedients().get(i));
				j = j + 2;
			}

			return ps.executeUpdate() == 1;

		} catch (SQLException e) {
			log.debug(e);
		}

		return false;
	}

	@Override
	public boolean deleteRecipe(int idjava) {

		try {
			// delete
			log.info("Rezept löschen, IdJava: {} ", idjava);
			PreparedStatement deleteStatement = con.prepareStatement("DELETE FROM rezepte WHERE idjava =?");
			deleteStatement.setInt(1, idjava); // 1 = ERSTES FRAGEZEICHEN

			if (deleteStatement.executeUpdate() == 1) {
				setLastEntry(null);
				log.info("Rezept gelöscht, IdJava: {} ", idjava);
				updateLastEntry();
				return true;
			}

		} catch (SQLException e) {
			log.debug(e);
		}

		return false;

	}

	@Override
	public boolean deleteAllRecipe() {
		// TODO Alle Rezepte löschen
		return false;
	}

	@Override
	public boolean updateRecipeString(String columnTitle, String newValue, int idjava) {
		String fieldname = getSelectedColSQLiteName(columnTitle);
		log.trace("idjava: " + idjava + " Feld: " + fieldname + "Neuer Wert: " + newValue);
		try {

			// update
			PreparedStatement updateStatement = con
					.prepareStatement("UPDATE rezepte SET " + fieldname + "=? WHERE idjava= ?");
			updateStatement.setString(1, newValue);
			updateStatement.setInt(2, idjava);
			log.debug("UPDATE rezepte SET " + fieldname + "="+newValue+" WHERE idjava=" + idjava );
			return (updateStatement.executeUpdate() == 1);

		} catch (SQLException e) {

			log.error(e);
		}

		return false;

	}

	@Override
	public boolean updateRecipeInteger(String columnTitle, int newValue, int idjava) {
		String fieldname = getSelectedColSQLiteName(columnTitle);
		log.trace("idjava: " + idjava + " Feld: " + fieldname + "Neuer Wert: " + newValue);
		try {

			// update
			PreparedStatement updateStatement = con
					.prepareStatement("UPDATE rezepte SET " + fieldname + "=? WHERE idjava= ?");
			updateStatement.setInt(1, newValue);
			updateStatement.setInt(2, idjava);
			log.debug("UPDATE rezepte SET " + fieldname + "="+newValue+" WHERE idjava=" + idjava );
			return (updateStatement.executeUpdate() == 1);

		} catch (SQLException e) {
			log.error(e);
		}

		return false;

	}

	@Override
	public boolean updateRecipeDouble(String columnTitle, Double newValue, int idjava) {
		String fieldname = getSelectedColSQLiteName(columnTitle);
		log.trace("idjava: " + idjava + " Feld: " + fieldname + "Neuer Wert: " + newValue);
		try {

			// update
			PreparedStatement updateStatement = con
					.prepareStatement("UPDATE rezepte SET " + fieldname + "=? WHERE idjava= ?");
			updateStatement.setDouble(1, newValue);
			updateStatement.setInt(2, idjava);
			log.debug("UPDATE rezepte SET " + fieldname + " = "+newValue+" WHERE idjava = " + idjava );
			return (updateStatement.executeUpdate() == 1);

		} catch (SQLException e) {
			log.error(e);
		}

		return false;

	}

	@Override
	public Recipe findRecipe(int idjava) {
		// TODO Einzelnes Rezept finden
		return null;
	}

	public void updateLastEntry() {

		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM rezepte ORDER BY idjava DESC LIMIT 1");
			log.trace("Update Last Enty Statemen: SELECT * FROM rezepte ORDER BY idjava DESC LIMIT 1");

			int id = rs.getInt("id");
			int idjava = rs.getInt("idjava");
			String name = rs.getString("name");
			String descritption = rs.getString("beschreibung");
			double portion = rs.getDouble("portion");
			String imagepath = rs.getString("bildpfad");
			
			setLastEntry(new Recipe(id, idjava, name, descritption, portion, imagepath));

			log.info("Last Entry Refreshed ");

		} catch (SQLException e) {
			setLastEntry(new Recipe(0, "--", 0));
			log.error(e);
		}

	}

	private void fillResultList(ArrayList<Recipe> list, ResultSet rs) throws SQLException {

		while (rs.next()) {

			int id = rs.getInt("id");
			int idjava = rs.getInt("idjava");
			String name = rs.getString("name");
			String descritption = rs.getString("beschreibung");
			int duration = rs.getInt("dauergesamt");
			double portion = rs.getDouble("portion");
			String imagepath = rs.getString("bildpfad");

			Recipe recipe = new Recipe(id, idjava, name, descritption, duration, portion, imagepath);

			log.trace("IdJava: " +idjava+ "Name: " +name+ "Beschreibung: " 
					+descritption+ "Dauer: " +duration+ "Portion: " +portion+ "BildPfad: " +imagepath );
			
			for (int i = 1; i <= 5; i++) {
				recipe.getIngriedients().add(new Ingredient(rs.getInt("zutat_" + i)));
				recipe.getAmountIngriedients().add(rs.getDouble("menge_" + i));

				 log.trace("Zutat_"+ i +" "+rs.getInt("zutat_"+i));
				 log.trace("Menge"+ i +" "+rs.getDouble("menge_"+i));
			}

			setLastEntry(recipe);
			list.add(recipe);
		}
	}

	public String getSelectedColSQLiteName(String columnTitle) {
		log.trace("get Selected SQLite Col Name with GUI ColName: " + columnTitle);
		switch (columnTitle) {
		case "Gericht":
			return "name";

		case "Beschreibung":
			return "beschreibung";

		case "Portion":
			return "portion";

		case "Dauer[min]":
			return "dauergesamt";

		case "Bildpfad":
			return "bildpfad";

		default:
			return "";
		}

	}

	public static Recipe getLastEntry() {
		return lastEntry;
	}

	public static void setLastEntry(Recipe lastEntry) {
		RecipeSQLiteDAO.lastEntry = lastEntry;
	}

	public void setCon(Connection con) {
		this.con = con;
	}
}
