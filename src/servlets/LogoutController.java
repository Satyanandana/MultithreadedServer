package servlets;

import authentication.Authenticator;
import dao.UserDAO;
import http.HttpRequest;
import http.HttpResponse;

public class LogoutController implements Controller {

	private UserDAO userDAO;

	public LogoutController(UserDAO userDAO) {
		super();
		this.userDAO = userDAO;
	}

	@Override
	public void handleRequest(HttpRequest request, HttpResponse response) {
		Authenticator.logout(request);
		String msg = "Successfully logged out";
		response.getResponseHandler().writeTemplate(request, response, "general.vm", msg);

	}

}
