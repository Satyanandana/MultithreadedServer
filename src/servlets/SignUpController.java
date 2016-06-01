package servlets;

import java.sql.SQLException;

import dao.UserDAO;
import domain.User;
import domain.UserAuthentication;
import http.HttpRequest;
import http.HttpResponse;

public class SignUpController implements Controller {

	private UserDAO userDAO;

	public SignUpController(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public void handleRequest(HttpRequest request, HttpResponse response) {
		String msg = "";
		String email = request.getParameter("username");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstname");
		String lastName = request.getParameter("lastname");
		String phoneNumber = request.getParameter("phonenumber");
		if (email != null && password != null && firstName != null && lastName != null && phoneNumber != null) {

			try {
				boolean exist = false;
				exist = userDAO.checkUser(email);
				if (!exist) {
					UserAuthentication userAuthentication = new UserAuthentication();
					userAuthentication.setEmail(email);
					userAuthentication.setPassword(password);
					long id = userDAO.setUserAuthentication(userAuthentication);
					User user = new User();
					user.setId(id);
					user.setFirstName(firstName);
					user.setLastName(lastName);
					user.setPhoneNumber(phoneNumber);
					userDAO.setUser(user);
					msg = "Successfully signed up";
				} else {
					msg = "An account already exists with the given email id.Please try login.";
				}

			} catch (SQLException e) {
				msg = "Sign up failed.Please try again";
				e.printStackTrace();
			}
		} else {
			msg = "Sign up failed .Please enter all the fields correctly";
		}
		response.getResponseHandler().writeTemplate(request, response, "general.vm", msg);
	}

}
