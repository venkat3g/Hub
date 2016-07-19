package Test;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

//TODO work on implementation
public class Server extends Thread {

	private ServerSocket serverSocket;

	public Server(int port) throws IOException {
		super("Server");

		serverSocket = new ServerSocket(port);
		// serverSocket.setSoTimeout(10000);
	}

	@Override
	public void run() {
		while (true) {
			try {
				// Waiting for connection from client
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				Socket server = serverSocket.accept();
				// Prints to console that socket (client) has connected
				System.out.println("" + server.getRemoteSocketAddress() + " connected");
				// Creates an object output stream
				ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
				
				// Gets current Program List from FileManager
				FileManager.instantiateFile();
				ArrayList<FileManager> fm = FileManager.getFileManagerList();

				// Sends ArrayList of programs to client
				out.writeObject(fm);
				
				// Creates an object input stream
				ObjectInputStream in = new ObjectInputStream(server.getInputStream());
				Object input = in.readObject();
				// Outputs clients choice
				System.out.println(input);
				
				if(input instanceof FileManager){
					((FileManager) input).open();
				}

				server.close();
			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public static void main(String[] args) {
		// int port = Integer.parseInt(args[0]);
		try {
			Thread t = new Server(1111);
			//Thread stop = getStopper();
			t.start();
			//stop.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
