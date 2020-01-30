package Part2;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

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

	private void processRequest() throws Exception {
		//get a reference to the socket's input and output streams
		InputStream is = socket.getInputStream();
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());

		//set up input stream filters
		InputStreamReader stream = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(stream);

		//get the request line of hte HTTP request message
		String requestLine = br.readLine();

		//display the request line
		System.out.println();
		System.out.println(requestLine);

		//get and display the header lines
		String headerLine;

		while((headerLine = br.readLine()).length() != 0) {
			System.out.println(headerLine);
		}

		//close streams and socket
		os.close();
		br.close();
		socket.close();

		//extract the filename from the request line
		StringTokenizer tokens = new StringTokenizer(requestLine);
		tokens.nextToken(); //skip over the method which should be "GET"
		String fileName = tokens.nextToken();

		//prepend a . so that the file request is within teh current directory
		fileName = "." + fileName;

		//open the requested file
		FileInputStream fis = null;
		boolean fileExists = true;
		try {
			fis = new FileInputStream(fileName);
		}
		catch (FileNotFoundException e) {
			fileExists = false;
		}

		//construct the response message
		String statusLine;
		String contentTypeLine;
		String entityBody = null;

		if(fileExists){
			statusLine = "200 OK" + CRLF; //???????????????
			contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
		}
		else {
			statusLine = "404 Not Found" + CRLF;
			contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
			entityBody = "<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>" + "<BODY>Not Found</BODY></HTML>";
		}

		//send the status line
		os.writeBytes(statusLine);
		//send the content type line
		os.writeBytes(contentTypeLine);
		//send a blank line to indicate the end of the header lines
		os.writeBytes(CRLF);

		//send the entity body
		if(fileExists){
			sendBytes(fis, os);
			fis.close();
		}
		else {
			os.writeBytes(entityBody);
		}
	}

	private static void sendBytes(FileInputStream fis, OutputStream os) throws  Exception {
		//construct a 1K buffer to hold bytes on their way to the socket
		byte[] buffer = new byte[1024];
		int bytes = 0;
		//copy requested file into the socket's output stream
		while((bytes = fis.read(buffer)) != -1){
			os.write(buffer, 0, bytes);
		}
	}

	private static String contentType(String fileName) {
		if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
			return "text/html";
		}
		else if(fileName.endsWith(".gif")){ //??????????????????
			return "image/gif";
		}
		else if (fileName.endsWith(".jpeg")) { //?????????????????
			return "image/jpeg";
		}
		return "application/octet-stream";
	}
}

