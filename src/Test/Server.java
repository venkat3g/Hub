package Test;

import java.net.*;
import java.util.ArrayList;

import com.test.FileManager;

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

				FileManager.instantiateFile();
				
				// Gets current Program List from FileManager
				ArrayList<String> output = getFileList();
				
				// Sends ArrayList of programs to client
				out.writeObject(output);

				while (server.getRemoteSocketAddress() != null) {
					// Creates an object input stream
					ObjectInputStream in = new ObjectInputStream(server.getInputStream());
					Object input = in.readObject();

					if (input instanceof String) {
						ObjectOutputStream o = new ObjectOutputStream(server.getOutputStream());
						ArrayList<FileManager> fm = FileManager.getFileManagerList();
						if (input.equals("reload")) {
							output = getFileList();
							o.writeObject(output);
						} else {
							for (FileManager m : fm) {
								if (input.equals(m.getFileName()))
									m.open();
							}
						}
					}
				}

			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");

			} catch (Exception e) {
				if(e instanceof SocketException){
					System.out.println("Client unexpectedly disconnected");
				}
				if (e instanceof EOFException) {
					System.out.println("Client has disconnected");
				} else
					e.printStackTrace();
			}
		}
	}

	private ArrayList<String> getFileList() throws Exception {
		
		ArrayList<FileManager> fm = FileManager.getFileManagerList();
		

		ArrayList<String> output = new ArrayList<>();

		for (FileManager m : fm) {
			output.add(m.getFileName());
		}
		System.out.println(output);
		return output;
	}

}
