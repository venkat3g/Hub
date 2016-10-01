package dock.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import com.test.FileManager;

//TODO work on implementation
public class Server extends Thread {

  private ServerSocket serverSocket;

  /**
   * Instantiates a server for hub listening on a port.
   * 
   * @param port
   *          port that the server listens on.
   * @throws IOException
   *           Throws an exception if the socket is unable to listen to a port.
   */
  public Server(int port) throws IOException {
    super("Server");

    serverSocket = new ServerSocket(port);
    // serverSocket.setSoTimeout(10000);
  }

  @Override
  public void run() {
    while (true) {
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
                if (input.equals(m.getFileName())) {
                  m.open();
                }
              }
            }
          }
        }

      } catch (SocketTimeoutException so) {
        System.out.println("Socket timed out!");

      } catch (Exception ex) {
        if (ex instanceof SocketException) {
          System.out.println("Client unexpectedly disconnected");
        }
        if (ex instanceof EOFException) {
          System.out.println("Client has disconnected");
        } else {
          ex.printStackTrace();
        }
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
