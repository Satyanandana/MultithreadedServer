package server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import dao.DbDAO;
import http.HttpRequest;
import http.RequestHandler;
import servlets.DispatcherServlet;
import threadpool.StaticThreadPool;

public class TinyHttpd3 {
	private static final int PORT = 8888;
	private ServerSocket serverSocket;
	private StaticThreadPool threadPool;
	private RequestHandler requestHandler;
	private static String currentDirectory = new File("").getAbsolutePath();

	public void init() {
		try {
			try {
				serverSocket = new ServerSocket(PORT);
				System.out.println("ServerSocket created..");
				threadPool = StaticThreadPool.getInstance(5, true);
				System.out.println("Thread pool created..");
				DbDAO dbDAO = new DbDAO();
				DispatcherServlet servlet = new DispatcherServlet(dbDAO);
				System.out.println("DispatcherServlet instance created..");
				requestHandler = RequestHandler.getInstance("LFU", serverSocket, servlet);
				System.out.println("Initialized RequestHandler with LFU filecaching ..");
				System.out.println(" ");
				
				Runtime.getRuntime().addShutdownHook(new Thread(){public void run(){
				    try {
				    	serverSocket.close();
				        System.out.println("The server is shut down!");
				    } catch (IOException e) { /* failed */ }
				}});
				
				while (true) {
					System.out.println("Listening to a connection on the local port " + serverSocket.getLocalPort() + "...");
					Socket client = serverSocket.accept();
					client.setKeepAlive(true);

					System.out.println("\nA connection established with the remote port " + client.getPort() + " at "
							+ client.getInetAddress().toString() + " Keep alive : " + client.getKeepAlive());

					HttpRequest request = new HttpRequest(client);

					System.out.println("Choosing between thread per connection and thread per resource ..");
					if (request.getProtocolVersion().equals("1.1")
							|| (request.getConnection() != null && request.getConnection().equals("keep-alive"))) {

						threadPool.execute(() -> {
							System.out.println("thread per connection policy started ..");
							requestHandler.processPersistantRequest(client, request);
							System.out.println("thread per connection policy ended..");
						});

					} else {

						threadPool.execute(() -> {
							System.out.println("thread per resource policy started ..");
							requestHandler.processRequest(client, request);
							System.out.println("thread per resource policy ended ..");
						});
					}

				}
			}catch (Exception e) {
				System.out.println(e.getStackTrace());
			} finally {
				serverSocket.close();
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TinyHttpd3 server = new TinyHttpd3();
		server.init();
	}

}
