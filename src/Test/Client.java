
package Test;

import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import com.test.FileManager;
//TODO work on then implement for phone
public class Client {
	public static void main(String[] args) {
		// String serverName = args[0];
		String serverName = "192.168.2.2";
		// int port = Integer.parseInt(args[1]);
		int port = 11111;

		// Try statement to catch Class not found exception from server, input
		// (instance), also to catch input out put exceptions from server
		// connection
		try {
			// Creates socket instance (also a client) to the server
			Socket client = new Socket(serverName, port);
			
			
						
			// Prints that client successfully connected
			System.out.println("Connected " + client.getRemoteSocketAddress());
			// Creates instances that will allow server and client to communicate
			OutputStream outToServer = client.getOutputStream();
			InputStream inFromServer = client.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inFromServer);	// Gets objects from Server
			
			// An instance of object that the server sends the client
			Object input = in.readObject();
			if (input instanceof ArrayList) {
				// Prints out the ArrayList that the server should send the client
				for (Object fm : (ArrayList<?>) input) {
					System.out.println(((FileManager) fm).getFileName());
				}
			}
			
			// Requests that client chooses a program from the list.
			System.out.println("Choose a program: ");
			Scanner keyboard = new Scanner(System.in);
			String choice = keyboard.nextLine();
			
			FileManager c = null;
			// If user correctly chooses a program from the list a FileManager is attached
			for (Object fm : (ArrayList<?>) input)
				if (choice.equals(((FileManager) fm).getFileName()))
					c = (FileManager) fm;
			keyboard.close();
			
			
			ObjectOutputStream out = new ObjectOutputStream(outToServer); // Sends objects to Server
			out.writeObject(c); // Sends the FileManager that corresponds to the Program picked by client
			
			// Closes socket (client) connection to server
			client.close();
		} catch (IOException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
}
