package http;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

	private Socket client;
	private ServerSocket serverSocket;
	private PrintStream outStream;
	private PrintWriter outwriter;
	private ResponseHandler responseHandler;
	private Map<String, String> headers = new HashMap<>();

	public HttpResponse(Socket client, ServerSocket serverSocket) {
		this.client = client;
		try {
			this.serverSocket = serverSocket;
			this.responseHandler = ResponseHandler.getInstance(serverSocket);
			this.outStream = new PrintStream(client.getOutputStream());
			this.outwriter = new PrintWriter(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getClient() {
		return client;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public ResponseHandler getResponseHandler() {
		return responseHandler;
	}

	public PrintStream getOutStream() {
		return outStream;
	}

	public PrintWriter getOutwriter() {
		return outwriter;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeader(String headerName, String headerValue) {
		this.headers.put(headerName, headerValue);
	}

}
