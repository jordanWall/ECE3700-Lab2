package Part1;

import java.net.*;

public final class WebServer {

	public static void main(String args[]) throws Exception {
		int port = 5555; //set the port number
		//establish the listen socket
		ServerSocket serverSocket = new ServerSocket(port);
		//process the HTTP service requests in an infinite loop
		while(true){
			//listen for TCP connection request
			Socket clientSocket = serverSocket.accept();
			//construct an object to process the HTTP request message
			Part1.HttpRequest request = new Part1.HttpRequest(clientSocket);
			//create a new thread to process the request
			Thread thread = new Thread(request);
			//start the thread
			thread.start();
		}
	}
}

