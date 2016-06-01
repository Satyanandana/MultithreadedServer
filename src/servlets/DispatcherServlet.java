package servlets;

import java.io.File;

import dao.DbDAO;
import dao.UserDAO;
import dao.UserDAOImp;
import filecache.FileCache;
import http.HttpRequest;
import http.HttpResponse;

public class DispatcherServlet implements HttpServlet {

	private static String currentDirectory = new File("").getAbsolutePath();
	private DbDAO dbDAO;
	private UserDAO userDAO;
	private SignUpController signUpController;
	private LoginController loginController;
	private LogoutController logoutController;
	private FileCache fileCache;

	public DispatcherServlet(DbDAO dbDAO) {
		this.dbDAO = dbDAO;
		init();
	}

	public void init() {
		this.userDAO = new UserDAOImp(dbDAO);
		signUpController = new SignUpController(userDAO);
		loginController = new LoginController(userDAO);
		logoutController = new LogoutController(userDAO);
	}

	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		if (request.getRequestURI().equals("/logout")) {
			logoutController.handleRequest(request, response);

		} else {
			response.getResponseHandler().resourceNotImplemented(request, response);
		}

	}

	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		if (request.getRequestURI().equals("/signup")) {
			signUpController.handleRequest(request, response);

		} else if (request.getRequestURI().equals("/login")) {
			loginController.handleRequest(request, response);

		} else {
			response.getResponseHandler().resourceNotImplemented(request, response);
		}
	}

	@Override
	public void doHead(HttpRequest request, HttpResponse response) {

		response.getResponseHandler().resourceNotImplemented(request, response);

	}

	@Override
	public void setFileCache(FileCache fileCache) {
		this.fileCache = fileCache;
	}

}
