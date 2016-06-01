package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test {

	public static void main(String[] args) throws SQLException {
		DbDAO dao = new DbDAO();
		PreparedStatement statement = null;
		ResultSet resultSet;
		try {
			statement = dao.getConnection().prepareStatement("select * from user_auth");
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				System.out.println(resultSet.getInt(0) + " " + resultSet.getString(1) + " " + resultSet.getString(2));
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			statement.close();
		}

	}

}
