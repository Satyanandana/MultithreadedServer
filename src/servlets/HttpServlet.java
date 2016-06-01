package servlets;

import filecache.FileCache;
import http.HttpRequest;
import http.HttpResponse;

public interface HttpServlet {
	public void doGet(HttpRequest request, HttpResponse response);
	public void doPost(HttpRequest request, HttpResponse response);
	public void doHead(HttpRequest request, HttpResponse response);
	public void setFileCache(FileCache fileCache);
}
