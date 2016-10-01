
package dock.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import com.test.FileManager;

//TODO work on then implement for phone
public class Client {

  /**
   * Main method that implements the client.
   * 
   * @param args
   *          commandline args.
   */
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
      // Creates instances that will allow server and client to
      // communicate

      InputStream inFromServer = client.getInputStream();
      ObjectInputStream in = new ObjectInputStream(inFromServer); // Gets
      // objects
      // from
      // Server

      // An instance of object that the server sends the client
      Object input = in.readObject();
      if (input instanceof ArrayList) {
        // Prints out the ArrayList that the server should send the
        // client
        for (Object fm : (ArrayList<?>) input) {
          System.out.println(((FileManager) fm).getFileName());
        }
      }

      // Requests that client chooses a program from the list.
      System.out.println("Choose a program: ");
      Scanner keyboard = new Scanner(System.in);
      String choice = keyboard.nextLine();

      FileManager ch = null;
      // If user correctly chooses a program from the list a FileManager
      // is attached
      for (Object fm : (ArrayList<?>) input) {
        if (choice.equals(((FileManager) fm).getFileName())) {
          ch = (FileManager) fm;
        }
      }
      keyboard.close();
      OutputStream outToServer = client.getOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(outToServer);
      out.writeObject(ch);
      // Closes socket (client) connection to server
      client.close();
    } catch (IOException | ClassNotFoundException ex) {
      System.out.println(ex.getMessage());
    }
  }
}
