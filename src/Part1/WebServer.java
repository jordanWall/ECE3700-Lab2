package Part1;

import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.*;
import java.util.*;

public final class WebServer {

	public static void main(String args[]) throws Exception {
		int port = 5555; //set the port number
		//establish the listen socket ??????????????????????????
		ServerSocket serverSocket = new ServerSocket(port);
		//process the HTTP service requests in an infinite loop
		while(true){
			//listen for TCP connection request ???????????????????????????
			Socket clientSocket = serverSocket.accept();
			//construct an object to process the HTTP request message
			HttpRequest request = new HttpRequest(clientSocket);
			//create a new thread to process the request
			Thread thread = new Thread(request);
			//start the thread
			thread.start();
		}
	}
}

final class HttpRequest implements Runnable {
	final static String CRLF = "\r\n";
	Socket socket;

	//constructor
	public HttpRequest(Socket socket) throws Exception {
		this.socket = socket;
	}

	//implement the run() method of the runnable interface
	public void run() {
		try {
			processRequest();
		}
		catch (Exception e){
			System.out.println(e);
		}
	}

	/////////////////////////Initialize things
	private void processRequest() throws Exception {
		//get a reference to the socket's input and output streams ????????????????????
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream(); ////////////DataOutputStream??

		//set up input stream filters ??????????????????????
		InputStreamReader stream = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(stream);

		//get the request line of hte HTTP request message ????????????????????????
		String requestLine = br.readLine();

		//display the request line
		System.out.println();
		System.out.println(requestLine);

		//get and display the header lines
		String headerLine = null;

		while((headerLine = br.readLine()).length() != 0) {
			System.out.println(headerLine);
		}

		//close streams and socket
		os.close();
		br.close();
		socket.close();
	}


}
