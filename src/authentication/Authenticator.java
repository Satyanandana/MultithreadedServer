package authentication;

import http.HttpRequest;
import session.Session;
import session.SessionRegistry;

public class Authenticator {

	private static SessionRegistry sessionRegistry = SessionRegistry.getInstance();

	private Authenticator() {

	}

	public static void checkAuthentication(HttpRequest request) {

		if (request.getRequestURI().matches("^\\/$") || request.getRequestURI().matches("^\\/index\\.html$")
				|| request.getRequestURI().matches("^\\/login\\.html$") || request.getRequestURI().matches("^\\/login$")
				|| request.getRequestURI().matches("^\\/signup\\.html$") || request.getRequestURI().matches("^\\/signup$")
				|| request.getRequestURI().matches("^\\/logout$") || request.getRequestURI().matches("^\\/resources.+")) {
			return;
		} else {
			String JSESSIONID = request.getJSESSIONID();
			if (JSESSIONID == null) {
				request.setRequestURI("/login.html");
				request.setRequestMethod("GET");
			} else {
				Session session = sessionRegistry.getSession(JSESSIONID);
				if (session == null) {
					request.setRequestURI("/login.html");
					request.setRequestMethod("GET");
				} else {
					request.setSession(session);
				}
			}
		}

	}

	public static boolean authenticate(HttpRequest request) {
		boolean authenticated = false;

		Session session = new Session();
		sessionRegistry.addSession(request.getJSESSIONID(), session);
		request.setSession(session);
		authenticated = true;

		return authenticated;
	}

	public static boolean logout(HttpRequest request) {

		boolean logout = false;
		String JSESSIONID = request.getJSESSIONID();
		sessionRegistry.removeSession(JSESSIONID);
		logout = true;
		return logout;
	}

}
