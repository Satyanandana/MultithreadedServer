package http;

import java.io.PrintStream;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Random;

public class ResponeHeaderHandler {

	private HttpRequest request;
	private HttpResponse response;

	public ResponeHeaderHandler(HttpRequest request, HttpResponse response) {
		this.request = request;
		this.response = response;
		if (request.getJSESSIONID() != null) {
			setHeader("Set-Cookie: ", "JSESSIONID=" + request.getJSESSIONID() + "; path=/");
		} else {
			setHeader("Set-Cookie: ", "JSESSIONID=" + getRandomHexString(15) + "; path=/");
		}

	}
	private String getRandomHexString(int numchars) {
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		while (sb.length() < numchars) {
			sb.append(Integer.toHexString(r.nextInt()));
		}

		return sb.toString().substring(0, numchars);
	}

	public void setHeader(String headerName, String headerValue) {
		response.setHeader(headerName, headerValue);
	}

	public void setContentType() {
		String contentType = "text/html";
		if (request.getRequestURI().endsWith("html")) {
			contentType = "text/html";
		} else if (request.getRequestURI().endsWith("png")) {
			contentType = "image/png";
		} else if (request.getRequestURI().endsWith("jpg")) {
			contentType = "image/jpg";
		} else if (request.getRequestURI().endsWith("pdf")) {
			contentType = "application/pdf";
		} else if (request.getRequestURI().endsWith("css")) {
			contentType = "text/css";
		} else if (request.getRequestURI().endsWith("js")) {
			contentType = "text/javascript";
		}

		response.setHeader("Content-Type: ", contentType);
	}

	public void setDate() {
		response.setHeader("Date: ", new Date().toString());
	}

	public void setLastModified() {
		response.setHeader("Last-Modified: ", new Date().toString());
	}

	public void setContentLength(long length) {
		response.setHeader("Content-Length: ", length + "");
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		String status = "HTTP/1.0 200 OK";
		if (httpStatus.equals(HttpStatus.OK)) {
			status = "HTTP/1.0 200 OK";
		} else if (httpStatus.equals(HttpStatus.NOT_FOUND)) {
			status = "HTTP/1.0 404 Not Found";
		} else if (httpStatus.equals(HttpStatus.NOT_IMPLEMENTED)) {
			status = "HTTP/1.0 501 Not Implemented";
		}
		response.setHeader("status", status);
	}

	public void writeHeaders() {
		PrintStream out = response.getOutStream();
		System.out.println("writing headers to response ..");
		out.println(response.getHeaders().get("status"));
		System.out.println(response.getHeaders().get("status"));
		for (Entry<String, String> entity : response.getHeaders().entrySet()) {
			if (entity.getKey().contains(":")) {

				out.println(entity.getKey() + entity.getValue());
				System.out.println(entity.getKey() + entity.getValue());
			} else if (!entity.getKey().equals("status")) {
				out.println(entity.getValue());
				System.out.println(entity.getValue());
			}
		}
		out.println("");
		System.out.println("");
		System.out.println("wrote headers to response ..");
		response.getHeaders().clear();
		System.out.println("Clearing the response headers");
		response.setHeader("Connection: ", "keep-alive");
		response.setHeader("Server: ", "http://localhost:" + response.getServerSocket().getLocalPort());

	}

}
