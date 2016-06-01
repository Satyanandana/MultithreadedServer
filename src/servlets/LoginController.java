package servlets;

import java.sql.SQLException;

import authentication.Authenticator;
import dao.UserDAO;
import domain.UserAuthentication;
import http.HttpRequest;
import http.HttpResponse;

public class LoginController implements Controller {

	private UserDAO userDAO;

	public LoginController(UserDAO userDAO) {
		super();
		this.userDAO = userDAO;
	}

	@Override
	public void handleRequest(HttpRequest request, HttpResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Boolean valid = false;
		String msg;
		if (password != null && username != null) {
			UserAuthentication userAuthentication = null;
			try {
				userAuthentication = userDAO.getUserAuthentication(username);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (userAuthentication != null) {
				if (userAuthentication.getPassword().equals(password)) {
					Authenticator.authenticate(request);
					msg = "successfully logged in";
				} else {
					msg = "Login failed wrong password";
				}
			} else {
				msg = "no account with provided email";
			}

		} else {
			msg = "Please enter both email id and password";
		}

		response.getResponseHandler().writeTemplate(request, response, "general.vm", msg);

	}

}
