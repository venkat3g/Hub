
package hub.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

  /**
   * Main method that implements the client.
   * 
   * @param args
   *          commandline args.
   */
  public static void main(String[] args) {
    //String serverName = "ec2-54-174-234-55.compute-1.amazonaws.com";
    int port = 1024;
    String serverName = "localhost";
    // Try statement to catch Class not found exception from server, input
    // (instance), also to catch input out put exceptions from server
    // connection
    try {
      // Creates socket instance (also a client) to the server
      Socket client = new Socket(serverName, port);

      System.out.println(client.getRemoteSocketAddress());

      // Prints that client successfully connected
      System.out.println("Connected " + client.getRemoteSocketAddress());
      
      // Lets Server know that device connecting is a Client.
      ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
      out.writeObject("Client Connected");

      // Gets objects from Server
      ObjectInputStream in = new ObjectInputStream(client.getInputStream());

      try {
        System.out.println(in.readObject());
      } catch (ClassNotFoundException ex) {
        // TODO Auto-generated catch block
        ex.printStackTrace();
      }

      
      Scanner keyboard = new Scanner(System.in);
      String output = keyboard.next();
      System.out.println(output);
      out.writeObject(output);
      

      keyboard.close();

      out.close();
      in.close();
      // Closes socket (client) connection to server
      client.close();
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
  }
}
