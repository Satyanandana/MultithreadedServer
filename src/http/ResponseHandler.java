package http;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class ResponseHandler {
	private static String currentDirectory = new File("").getAbsolutePath();
	private static ResponseHandler responseHandler = null;
	private static ReentrantLock lock = new ReentrantLock();
	private ServerSocket serverSocket;

	private ResponseHandler(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public static ResponseHandler getInstance(ServerSocket serverSocket) {
		lock.lock();
		try {
			if (responseHandler == null) {
				responseHandler = new ResponseHandler(serverSocket);
			}
		} finally {
			lock.unlock();
		}
		return responseHandler;

	}

	@SuppressWarnings("unused")
	public void sendFile(HttpRequest request, HttpResponse response, byte[] file) {
		try {
			PrintStream out = response.getOutStream();
			DataInputStream fin = new DataInputStream(new ByteArrayInputStream(file));
			try {
				int len = (int) file.length;

				ResponeHeaderHandler headerHandler = new ResponeHeaderHandler(request, response);
				headerHandler.setContentType();
				headerHandler.setContentLength(len);
				headerHandler.setDate();
				headerHandler.setLastModified();
				headerHandler.setHttpStatus(HttpStatus.OK);
				headerHandler.writeHeaders();

				byte buf[] = new byte[len];
				fin.readFully(buf);
				out.write(buf, 0, len);
				out.flush();
			} finally {
				fin.close();
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public void writeTemplate(HttpRequest request, HttpResponse response, String template, Object object) {
		PrintStream out = response.getOutStream();

		try {
			ResponeHeaderHandler headerHandler = new ResponeHeaderHandler(request, response);
			headerHandler.setContentType();
			// headerHandler.setContentLength(len);
			headerHandler.setDate();
			headerHandler.setLastModified();
			headerHandler.setHttpStatus(HttpStatus.OK);
			headerHandler.writeHeaders();

			/*  first, get and initialize an engine  */
			VelocityEngine ve = new VelocityEngine();
			Properties props = new Properties();

			String path = currentDirectory + File.separator + "WebContent" + File.separator + "velocity" + File.separator;
			props.put("file.resource.loader.path", path);
			props.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
			ve.init(props);
			/*  next, get the Template  */
			Template t = ve.getTemplate(template);

			/*  create a context and add data */
			VelocityContext context = new VelocityContext();
			context.put("map", object);
			/* now render the template into a StringWriter */
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			DataInputStream fin = null;
			try {
				byte[] bytes = writer.toString().getBytes();
				fin = new DataInputStream(new ByteArrayInputStream(bytes));
				int len = bytes.length;
				byte buf[] = new byte[len];
				fin.readFully(buf);
				out.write(buf, 0, len);
				out.flush();
			} finally {
				fin.close();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void resourceNotFound(HttpRequest request, HttpResponse response) {
		PrintStream out = response.getOutStream();
		ResponeHeaderHandler headerHandler = new ResponeHeaderHandler(request, response);
		headerHandler.setContentType();
		headerHandler.setContentLength(0);
		headerHandler.setDate();
		headerHandler.setLastModified();
		headerHandler.setHttpStatus(HttpStatus.NOT_FOUND);
		headerHandler.writeHeaders();
		out.flush();
	}

	public void resourceNotImplemented(HttpRequest request, HttpResponse response) {
		PrintStream out = response.getOutStream();
		ResponeHeaderHandler headerHandler = new ResponeHeaderHandler(request, response);
		headerHandler.setContentType();
		headerHandler.setContentLength(0);
		headerHandler.setDate();
		headerHandler.setLastModified();
		headerHandler.setHttpStatus(HttpStatus.NOT_IMPLEMENTED);
		headerHandler.writeHeaders();
		out.flush();
	}

	public void resourceOk(HttpRequest request, HttpResponse response) {
		PrintStream out = response.getOutStream();
		ResponeHeaderHandler headerHandler = new ResponeHeaderHandler(request, response);
		headerHandler.setContentType();
		headerHandler.setContentLength(0);
		headerHandler.setDate();
		headerHandler.setLastModified();
		headerHandler.setHttpStatus(HttpStatus.NOT_IMPLEMENTED);
		headerHandler.writeHeaders();
		out.flush();
	}

}
