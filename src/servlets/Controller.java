package servlets;

import http.HttpRequest;
import http.HttpResponse;

public interface Controller {

	public void handleRequest(HttpRequest request, HttpResponse response);

}
