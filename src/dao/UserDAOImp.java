package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import domain.User;
import domain.UserAuthentication;

public class UserDAOImp implements UserDAO {

	private DbDAO dbDAO;

	public UserDAOImp(DbDAO dbDAO) {
		this.dbDAO = dbDAO;
	}

	@Override
	public long setUserAuthentication(UserAuthentication userAuthentication) throws SQLException {
		int userkey = 0;
		PreparedStatement statement = null;
		ResultSet resultSet;
		String sql = "insert into user_auth (email,password) values(?,?)";
		try {
			statement = dbDAO.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, userAuthentication.getEmail());
			statement.setString(2, userAuthentication.getPassword());
			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			if (resultSet != null && resultSet.next()) {
				System.out.println("Generated user Id: " + resultSet.getInt(1));
				int user_id = resultSet.getInt(1);
				userkey = user_id;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			statement.close();
		}
		return userkey;
	}

	@Override
	public void setUser(User user) throws SQLException {

		PreparedStatement statement = null;
		String sql = "insert into user (id,firstname,lastname,phonenumber) values(?,?,?,?)";
		try {
			statement = dbDAO.getConnection().prepareStatement(sql);
			statement.setLong(1, user.getId());
			statement.setString(2, user.getFirstName());
			statement.setString(3, user.getLastName());
			statement.setString(4, user.getPhoneNumber());
			statement.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			statement.close();
		}
	}

	public Boolean checkUser(String email) throws SQLException {

		Boolean valid = false;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "select * from user_auth  where email = ? ";
		try {
			statement = dbDAO.getConnection().prepareStatement(sql);
			statement.setString(1, email);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				valid = true;
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			statement.close();
		}
		return valid;
	}

	@Override
	public UserAuthentication getUserAuthentication(String email) throws SQLException {
		UserAuthentication userAuthentication = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "select * from user_auth  where email = ? ";
		try {
			statement = dbDAO.getConnection().prepareStatement(sql);
			statement.setString(1, email);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				userAuthentication = new UserAuthentication();
				userAuthentication.setId(resultSet.getLong("id"));
				userAuthentication.setEmail(resultSet.getString("email"));
				userAuthentication.setPassword(resultSet.getString("password"));
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			statement.close();
		}
		return userAuthentication;
	}

	@Override
	public User getUser(long id) throws SQLException {
		User user = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "select * from user  where id = ? ";
		try {
			statement = dbDAO.getConnection().prepareStatement(sql);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				user = new User();
				user.setId(resultSet.getLong("id"));
				user.setFirstName(resultSet.getString("firstname"));
				user.setLastName(resultSet.getString("lastname"));
				user.setPhoneNumber(resultSet.getString("phonenumber"));
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			statement.close();
		}
		return user;
	}

}
