package hub.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import hub.runnable.IRunnableButton;
import hub.window.VisualPane;

/**
 * Server used for local clients using the same subnet.
 * 
 * @author Venkat Garapati
 *
 */
public class ServerLocal extends Thread {

  private static ServerSocket serverSocket;
  private static ServerLocal server;
  private boolean keepAlive = true;
  @SuppressWarnings("unused")
  private static int port;

  /**
   * Instantiates a server for hub listening on a port.
   * 
   * @param portPassed
   *          port that the server listens on.
   * @throws IOException
   *           Throws an exception if the socket is unable to listen to a port.
   */
  public ServerLocal(int portPassed) throws IOException {
    super("Local Server");

    port = portPassed;
    serverSocket = new ServerSocket(portPassed);
    server = this;
    // serverSocket.setSoTimeout(10000);
  }

  @Override
  public void run() {
    while (keepAlive) {
      try {
        System.out.println("" + "");
        // Waiting for connection from client
        System.out
            .println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
        Socket server = serverSocket.accept();
        // Prints to console that socket (client) has connected
        System.out.println("" + server.getRemoteSocketAddress() + " connected");
        // Creates an object output stream
        ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());

        // Creates an object input stream
        ObjectInputStream in = new ObjectInputStream(server.getInputStream());
        Object input = in.readObject();

        // Gets current Program List
        ArrayList<String> output = getFileList();

        // Sends ArrayList of programs to client
        out.writeObject(output);

        while (keepAlive && server.isConnected()) {
          try {

            if (input instanceof String) {
              if (input.equals("Client Connected")) {
                input = in.readObject();
                if (input.equals("reload")) {
                  output = getFileList();
                  out.writeObject(output);
                } else {
                  for (IRunnableButton b : VisualPane.visualPane.getManager()
                      .getButtonList()) {
                    if (input.equals(b.getName())) {
                      b.open();
                    }
                  }
                }
              }
            }
          } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
          }
        }

      } catch (SocketTimeoutException so) {
        System.out.println("Socket timed out!");

      } catch (Exception ex) {
        if (ex instanceof SocketException) {
          System.out.println("Client unexpectedly disconnected");
        }
      }
    }
  }

  private ArrayList<String> getFileList() throws Exception {
    return VisualPane.visualPane.getManager().getStringButtonList();
  }

  /**
   * Kills all active server.
   */
  public static void killLocal() {
    server.keepAlive = false;
    try {
      System.out.print("Self Close...");
      serverSocket.close();
    } catch (IOException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
    System.out.println("...done.");
  }

}
