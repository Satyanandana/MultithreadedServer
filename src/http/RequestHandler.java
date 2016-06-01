
package http;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantLock;

import authentication.Authenticator;
import filecache.FileCache;
import filecache.FileCacheLFU;
import filecache.FileCacheLRU;
import servlets.HttpServlet;

public class RequestHandler {
	private static HttpServlet servlet = null;
	private static RequestHandler requestHandler = null;
	private static ResponseHandler responseHandler = null;
	private static ReentrantLock lock = new ReentrantLock();
	@SuppressWarnings("unused")
	private ServerSocket serverSocket;
	private static String currentDirectory = new File("").getAbsolutePath();
	private static FileCache fileCache;

	public static RequestHandler getInstance(String cacheMode, ServerSocket serverSocket, HttpServlet servlet) {
		lock.lock();
		try {
			if (requestHandler == null) {
				requestHandler = new RequestHandler(cacheMode, serverSocket, servlet);
			}
		} finally {
			lock.unlock();
		}
		return requestHandler;

	}

	private RequestHandler(String cacheMode, ServerSocket serverSocket, HttpServlet servlet) {
		if (cacheMode.equals("LFU")) {
			this.fileCache = FileCacheLFU.getInstance();
		} else if (cacheMode.equals("RFU")) {
			this.fileCache = FileCacheLRU.getInstance();
		} else {
			this.fileCache = FileCacheLFU.getInstance();
		}
		this.serverSocket = serverSocket;
		this.servlet = servlet;
		this.servlet.setFileCache(this.fileCache);
		RequestHandler.responseHandler = ResponseHandler.getInstance(serverSocket);
	}

	public void setFileCache(String cacheMode) {
		if (cacheMode.equals("LFU")) {
			this.fileCache = FileCacheLFU.getInstance();
		} else if (cacheMode.equals("RFU")) {
			this.fileCache = FileCacheLRU.getInstance();
		} else {
			this.fileCache = FileCacheLFU.getInstance();
		}
	}

	public void processPersistantRequest(Socket client, HttpRequest request) {
		HttpResponse response = new HttpResponse(client, serverSocket);
		response.setHeader("Server: ", "http://localhost:" + serverSocket.getLocalPort());
		response.setHeader("Connection: ", "keep-alive");
		try {
			while (!client.isClosed()) {
				client.setSoTimeout(30000);
				try {

					dispatch(request, response);

				} finally {
					request.process();
				}

			}

		} catch (IOException e) {

		} finally {

			try {
				request.getInputReader().close();
				response.getOutwriter().close();
				client.close();
			} catch (IOException e) {

			}
			System.out.println("A connection is closed.");
		}

	}

	public void processRequest(Socket client, HttpRequest request) {
		try {
			client.setSoTimeout(30000);
			HttpResponse response = new HttpResponse(client, serverSocket);
			response.setHeader("Server: ", "http://localhost:" + serverSocket.getLocalPort());
			try {

				dispatch(request, response);

			} finally {

				request.getInputReader().close();
				response.getOutwriter().close();
				client.close();
				System.out.println("A connection is closed.");

			}
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void dispatch(HttpRequest request, HttpResponse response) {
		Authenticator.checkAuthentication(request);
		if (request.getRequestMethod().equals("GET")) {

			if (request.getRequestURI().matches("^\\/$") || request.getRequestURI().matches("^\\/index\\.html$")
					|| request.getRequestURI().matches("^\\/login\\.html$") || request.getRequestURI().matches(".+\\.html$")
					|| request.getRequestURI().matches("^\\/resources.+")) {
				String requestPath = request.getRequestURI().equals("/") ? "index.html" : request.getRequestURI();
				Path filePath = Paths.get(currentDirectory + "/WebContent/" + requestPath);
				try {

					// byte[] content = Files.readAllBytes(filePath);
					byte[] content = fileCache.fetch(filePath);
					System.out.println("file cache  fetch method returned byte[]");
					response.getResponseHandler().sendFile(request, response, content);
				} catch (IOException e) {
					System.out.println("Resource not found");
					response.getResponseHandler().resourceNotFound(request, response);

				}
			} else {
				this.servlet.doGet(request, response);
			}

		} else if (request.getRequestMethod().equals("POST")) {
			this.servlet.doPost(request, response);
		} else if (request.getRequestMethod().equals("HEAD")) {
			if (request.getRequestURI().matches("^\\/$") || request.getRequestURI().matches("^\\/index\\.html$")
					|| request.getRequestURI().matches("^\\/login\\.html$") || request.getRequestURI().matches("^\\/resources.+")) {
				String requestPath = request.getRequestURI().equals("/") ? "index.html" : request.getRequestURI();
				Path filePath = Paths.get(currentDirectory + "/WebContent/" + requestPath);
				try {

					byte[] content = Files.readAllBytes(filePath);
					System.out.println("file cache  fetch method returned byte[]");
					response.getResponseHandler().resourceOk(request, response);
				} catch (IOException e) {
					System.out.println("Resource not found");
					response.getResponseHandler().resourceNotFound(request, response);

				}
			} else {
				this.servlet.doHead(request, response);
			}
		}
	}

}
