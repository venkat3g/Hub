
package Test;

import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

//TODO work on then implement for phone
public class Client {
	public static void main(String[] args) {
		// String serverName = args[0];
		String serverName = "localhost";
		// int port = Integer.parseInt(args[1]);
		int port = 1111;
		try {
			Socket client = new Socket(serverName, port);
			System.out.println("Connected " + client.getRemoteSocketAddress());
			OutputStream outToServer = client.getOutputStream();
			InputStream inFromServer = client.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inFromServer);
			Object input = in.readObject();
			if (input instanceof ArrayList) {
				for (Object fm : (ArrayList<?>)input) {
					System.out.println(((FileManager) fm).getFileName());
				}
			}
			System.out.println("Choose a program: ");
			Scanner keyboard = new Scanner(System.in);
			String choice = keyboard.next();
			FileManager c = null;
			for(Object fm: (ArrayList<?>)input)
				if(choice.equals(((FileManager)fm).getFileName()))
					c = (FileManager)fm;
			keyboard.close();
			ObjectOutputStream out = new ObjectOutputStream(outToServer);
			out.writeObject(c);
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
