package Part2;

import java.net.ServerSocket;
import java.net.Socket;

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

