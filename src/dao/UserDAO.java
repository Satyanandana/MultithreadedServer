package dao;

import java.sql.SQLException;

import domain.User;
import domain.UserAuthentication;

public interface UserDAO {

	public long setUserAuthentication(UserAuthentication userAuthentication) throws SQLException;
	public void setUser(User user) throws SQLException;
	public UserAuthentication getUserAuthentication(String email) throws SQLException;
	public User getUser(long id) throws SQLException;
	public Boolean checkUser(String email) throws SQLException;
}