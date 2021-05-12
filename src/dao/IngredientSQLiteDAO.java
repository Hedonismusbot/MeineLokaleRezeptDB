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

public class IngredientSQLiteDAO implements IngredientDAO {

	private static Logger log = LogManager.getLogger();
	private Connection con;
	private static Ingredient lastEntry;

	public IngredientSQLiteDAO() {
		con = DBConnectSQLite.getInstance().getCon();
	}

	@Override
	public List<Ingredient> findAllIngredients() {
		ArrayList<Ingredient> list = new ArrayList<>();

		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM zutaten");

			fillResultList(list, rs);
			log.info("SELECT alle Zutaten Erfolgreich ");

		} catch (SQLException e) {
			log.error(e);
		}
		return list;

	}

	@Override
	public boolean addNewIngredient(Ingredient ing) {
		updateLastEntry();
		try {
			// insert
			PreparedStatement ps = con.prepareStatement("INSERT INTO zutaten (idjava, name ,einheit) VALUES (?,?,?)");
			ps.setInt(1, getLastEntry().getIdJava() + 1);
			ps.setString(2, ing.getName());
			ps.setString(3, ing.getUnit());

			if (ps.executeUpdate() == 1) {
				updateLastEntry();
				log.info("Neues Zutat hinzugefügt idjava {} name {} unit {} ", getLastEntry().getIdJava() + 1,
						ing.getName(), ing.getUnit());

				return true;
			}

		} catch (SQLException e) {
			log.error(e);
		}

		return false;
	}

	@Override
	public boolean deleteIngredient(int idjava) {

		try {
			// delete
			log.info("Zutat löschen, IdJava: {} ", idjava);
			PreparedStatement deleteStatement = con.prepareStatement("DELETE FROM zutaten WHERE idjava =?");
			deleteStatement.setInt(1, idjava);
			if (deleteStatement.executeUpdate() == 1) {
				setLastEntry(null);
				log.info("Zutat gelöscht, IdJava: {} ", idjava);
				updateLastEntry();
				return true;
			}

		} catch (SQLException e) {
			log.error(e);
		}

		return false;
	}

	@Override
	public boolean deleteAllIngredients() {
		// TODO Alle Zutaten löschen
		return false;
	}

	@Override
	public boolean updateIngredientString(String columnTitle, String newValue, int idjava) {
		String fieldname = getSelectedColSQLiteName(columnTitle);
		log.trace("idjava: " + idjava + " Feld: " + fieldname + "Neuer Wert: " + newValue);
		try {

			// update
			PreparedStatement updateStatement = con
					.prepareStatement("UPDATE zutaten SET " + fieldname + "=? WHERE idjava= ?");
			updateStatement.setString(1, newValue);
			updateStatement.setInt(2, idjava);
			log.debug(updateStatement.toString());
			return (updateStatement.executeUpdate() == 1);

		} catch (SQLException e) {
			log.error(e);
		}

		return false;
	}

	@Override
	public Ingredient findSingleIngredient(int idjava) {

		try {
			log.debug("Single Ingredient id : " + idjava);
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM zutaten WHERE idjava=" + idjava);
			String name = "";
			String unit = "";
			name = rs.getString("name");
			unit = rs.getString("einheit");

			return new Ingredient(name, unit);

		} catch (SQLException e) {
			setLastEntry(new Ingredient(0, "---", "---"));
			log.error(e);
		}
		return new Ingredient("", "");

	}

	public void updateLastEntry() {
		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM zutaten ORDER BY idjava DESC LIMIT 1");

			int id = rs.getInt("id");
			int idjava = rs.getInt("idjava");
			String name = rs.getString("name");
			String unit = rs.getString("einheit");

			setLastEntry(new Ingredient(id, idjava, name, unit));

			log.info("Last Entry Refreshed ");

		} catch (SQLException e) {
			setLastEntry(new Ingredient(0, "---", "---"));
			log.error(e);
		}

	}

	private void fillResultList(ArrayList<Ingredient> list, ResultSet rs) throws SQLException {
		while (rs.next()) {
			int id = rs.getInt("id");
			int idjava = rs.getInt("idjava");
			String name = rs.getString("name");
			String unit = rs.getString("einheit");
			// int time = rs.getInt("dauergesamt");
			setLastEntry(new Ingredient(id, idjava, name, unit));
			list.add(new Ingredient(id, idjava, name, unit));
		}
	}

	public String getSelectedColSQLiteName(String columnTitle) {
		log.trace("get Selected SQLite Col Name with GUI ColName: " + columnTitle);
		switch (columnTitle) {
		case "Name":
			return "name";

		case "Unit":
			return "einheit";

		default:
			return "";
		}
	}

	public static Ingredient getLastEntry() {
		return lastEntry;
	}

	public static void setLastEntry(Ingredient lastEntry) {
		IngredientSQLiteDAO.lastEntry = lastEntry;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

}
