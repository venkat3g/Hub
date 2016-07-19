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
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				Socket server = serverSocket.accept();
				System.out.println("" + server.getRemoteSocketAddress() + " connected");
				ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());

				FileManager.instantiateFile();
				ArrayList<FileManager> fm = FileManager.getFileManagerList();

				out.writeObject(fm);
				ObjectInputStream in = new ObjectInputStream(server.getInputStream());
				System.out.println(in.readObject());

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
