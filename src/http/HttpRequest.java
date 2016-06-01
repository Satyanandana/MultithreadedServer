package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import session.Session;

public class HttpRequest {

	private Socket client;
	private BufferedReader inputReader;
	private InetAddress remoteAddress;
	private String requestMethod = "GET";
	private String requestURI = "/";
	private String protocol = "HTTP";
	private String protocolVersion = "1.0";
	private String host;
	private String JSESSIONID;
	private Session session;
	private String connection;
	private Map<String, String> headers = new HashMap<>();
	private Map<String, String> requestParameters = new HashMap<>();

	public HttpRequest(Socket client) {
		try {
			this.remoteAddress = client.getInetAddress();
			this.inputReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

			process();

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/** code to read the post payload data **/
	@SuppressWarnings("unused")
	private String getPostRequestPayload(BufferedReader inputReader) throws IOException {

		StringBuilder payload = new StringBuilder();
		while (inputReader.ready()) {
			payload.append((char) inputReader.read());
		}
		return payload.toString();
	}

	@SuppressWarnings("unused")
	private void parsePayload(String payload) {

		System.out.println("RequestParams: " + payload);
		if (payload != null) {
			String[] parameters = payload.split("&");
			for (String parameter : parameters) {
				String[] keyValue = parameter.split("=");
				try {

					requestParameters.put(URLDecoder.decode(keyValue[0], "UTF-8"), URLDecoder.decode(keyValue[1], "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	public void process() throws IOException {
		String line = inputReader.readLine();
		// while ( in.ready() && line != null ) {
		while (inputReader.ready() && line != null) {
			System.out.println(line);
			if (line.equals("")) {
				break;
			} else if (line.startsWith("GET") || line.startsWith("POST") || line.startsWith("HEAD")) {
				StringTokenizer tokens = new StringTokenizer(line);
				this.requestMethod = tokens.nextToken();
				this.requestURI = tokens.nextToken();
				if (requestURI.contains("?")) {
					String[] pathPayload = this.requestURI.split("\\?");
					this.requestURI = pathPayload[0];
					parsePayload(pathPayload[1]);
				}
				String[] protocol_version = tokens.nextToken().split("/");
				this.protocol = protocol_version[0];
				this.protocolVersion = protocol_version[1];
				headers.put("RequestMethod", requestMethod);
				headers.put("RequestURI", requestURI);
				headers.put("Protocol", protocol);
				headers.put("ProtocolVersion", protocolVersion);
			} else if (line.startsWith("Host")) {
				StringTokenizer tokens = new StringTokenizer(line);
				tokens.nextToken();
				this.host = tokens.nextToken();
				headers.put("Host", host);
			} else if (line.startsWith("Connection")) {
				StringTokenizer tokens = new StringTokenizer(line);
				tokens.nextToken();
				this.connection = tokens.nextToken();
				headers.put("Connection", connection);
			} else if (line.startsWith("Cookie")) {
				String[] parts = line.split(":");
				String[] cookies = parts[1].split(";");
				for (String cookie : cookies) {
					if (cookie.contains("JSESSIONID")) {
						String[] cookieParts = cookie.split("=");
						this.JSESSIONID = cookieParts[1];
					}
				}

			} else {
				if (line.contains(":")) {
					String[] header = line.split(":");
					headers.put(header[0], header[1]);
				}
			}

			line = inputReader.readLine();
		}
		System.out.println(line);

		if (requestMethod.equals("POST")) {
			String payload = getPostRequestPayload(this.inputReader);
			parsePayload(payload);
		}
	}

	public Socket getClient() {
		return client;
	}

	public BufferedReader getInputReader() {
		return inputReader;
	}

	public InetAddress getRemoteAddress() {
		return remoteAddress;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public String getHost() {
		return host;
	}

	public String getJSESSIONID() {
		return JSESSIONID;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getConnection() {
		return connection;
	}

	public String getParameter(String name) {
		return requestParameters.get(name);
	}

	public String getRequestURI() {
		return requestURI;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getHeader(String name) {
		return headers.get(name);
	}

	public Map<String, String> getRequestParameters() {
		return requestParameters;
	}

	public void setRequestURI(String URI) {
		this.requestURI = URI;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

}
