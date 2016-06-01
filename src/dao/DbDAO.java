package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbDAO {
	private static final String HSQLDB_DRIVER = "org.hsqldb.jdbcDriver";
	private static final String HSQLDB_URL = "jdbc:hsqldb:hsql://localhost/";
	private static final String usr = "sa";
	private Connection connection;

	public DbDAO() {
		connect();
	}

	public Connection getConnection() {

		try {
			if (connection == null) {
				connect();
			} else

			if (connection.isClosed()) {
				connect();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	private void connect() {

		try {
			System.out.println("Connecting using driver " + HSQLDB_DRIVER + ", DB URL " + HSQLDB_URL);
			try {
				Class.forName(HSQLDB_DRIVER);
				System.out.println("Hsqldb driver found");
			} catch (Exception e) {
				System.out.println("Hsqldb driver not found");
			}
			try {
				connection = DriverManager.getConnection(HSQLDB_URL, usr, "");
				System.out.println("connection established");
			} catch (SQLException e) {
				System.out.println("unable to create a database connection Hsqldb");
			}
		} finally {

		}

	}
}
