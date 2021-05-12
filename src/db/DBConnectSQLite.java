package db;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBConnectSQLite {

	private static Logger log = LogManager.getLogger();
	private static DBConnectSQLite instance = null;

	private String path = "jdbc:sqlite:src/rezeptDB.db";

	private Connection con = null;

	
	// ------------ Methoden ------------
	
	/**
	 * Connect to a Rezept database
	 */
	public void connect() {
		try {

			Class.forName("org.sqlite.JDBC");
			// db parameters
			log.debug(path);
			// create a connection to the database
			con = DriverManager.getConnection(path);

			log.info("Connection to SQLite has been established.");

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}


	public synchronized static DBConnectSQLite getInstance() {
		if (instance == null) {
			instance = new DBConnectSQLite();
		}
		return instance;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

}
