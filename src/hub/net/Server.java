package hub.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Temporary file which will be implemented in proxy server.
 * 
 * @author Venkat Garapati
 *
 */
public class Server extends Thread {

  private int connectionPort;
  private ServerSocket serverSocket;
  private boolean keepAlive = false;
  private ArrayList<String> list;
  private ObjectOutputStream serverOutput;

  /**
   * Default constructor for Server Thread.
   */
  public Server() {
    connectionPort = 1024;
    keepAlive = true;
    try {
      serverSocket = new ServerSocket(connectionPort);
    } catch (IOException ex) {
      System.out.println("Unable to attach to port: " + connectionPort);
    }
  }

  /**
   * Constructor for Server Thread.
   * 
   * @param port
   *          port the Server will listen for
   */
  public Server(int port) {
    connectionPort = port;
    keepAlive = true;
    try {
      serverSocket = new ServerSocket(connectionPort);
    } catch (IOException ex) {
      System.out.println("Unable to attach to port: " + connectionPort);
    }
  }

  /**
   * Called when thread is started.
   */
  public void run() {
    while (keepAlive) {
      try {
        System.out.println("" + "");
        // Waiting for connection from client
        System.out.println("Waiting for client on port " + serverSocket.getLocalPort());


        checkIfServerOrClient(serverSocket.accept());
        
      } catch (SocketTimeoutException se) {
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

  private void checkIfServerOrClient(final Socket socket) throws Exception {
    
    System.out.println(socket.getRemoteSocketAddress());
    
    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

    String input = (String) in.readObject();
    if (input.equals("Server Connected")) {
      startServerThread(socket, out, in);
      serverOutput = out;
    } else if (input.equals("Client Connected")) {
      startClientThread(socket, out, in);
    } else {
      System.err.println("Warning unidentified client attempting to connect.");
    }
    /*
     * Ignores Clients that do not tell server 'who' they are.
     */

  }

  private void startClientThread(final Socket socket, final ObjectOutputStream out,
      final ObjectInputStream in) {
    new Thread(new Runnable() {

      @Override
      public void run() {
        try {
          out.writeObject(list);
          while (true) {
            Object strOb = in.readObject();
            System.out.println(strOb);
            writeToServer(strOb);
          }

        } catch (IOException ex) {
          // TODO Auto-generated catch block
          ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
          // TODO Auto-generated catch block
          ex.printStackTrace();
        }

      }

    }, "Client Thread").start();
  }

  private void writeToServer(Object output) {
    try {
      serverOutput.writeObject(output);
    } catch (IOException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
  }

  private void startServerThread(Socket socket, final ObjectOutputStream out,
      final ObjectInputStream in) throws IOException {
    new Thread(new Runnable() {

      @SuppressWarnings("unchecked")
      @Override
      public void run() {
        try {
          while (true) {
            Object ob = in.readObject();
            if (ob instanceof ArrayList) {
              list = (ArrayList<String>) ob;
            }
            System.out.println(ob.toString());

          }

        } catch (IOException ex) {
          // TODO Auto-generated catch block
          ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
          // TODO Auto-generated catch block
          ex.printStackTrace();
        }

      }
    }, "Server Thread").start();
  }

  public static void main(String... args) {
    Server ser = new Server(1024);
    ser.start();
  }

}