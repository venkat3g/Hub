package hub.net;

import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.MenuComponent;
import java.awt.MenuItem;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JTextField;

import hub.runnable.IRunnableButton;
import hub.window.VisualPane;

/**
 * Server that will connect to proxy where client will connect as well.
 * 
 * @author Venkat Garapati
 *
 */
public class ServerConnect extends Thread {

  //private static String host = "localhost";
  private static String host = "ec2-54-174-234-55.compute-1.amazonaws.com";
  private static Socket proxy;
  private static int port = 1024;
  private static boolean keepAlive = false;

  public ServerConnect(Runnable runnable, String string) {
    super(runnable, string);
  }

  /**
   * Connects to remote server.
   * 
   * @param listToStream
   *          list sent to remote server.
   */
  public static void connect(ArrayList<String> listToStream, MenuItem com) {
    new Thread(new Runnable() {

      @Override
      public void run() {
        try {
          keepAlive = true;
          proxy = new Socket(host, port);
          ObjectOutputStream out = new ObjectOutputStream(proxy.getOutputStream());

          ObjectInputStream in = new ObjectInputStream(proxy.getInputStream());

          out.writeObject("Server Connected");
          out.writeObject(listToStream);

          while (proxy.isConnected() && keepAlive) {
            try {
              String input = (String) in.readObject();
              System.out.println(input);
              for (IRunnableButton b : VisualPane.visualPane.getManager()
                  .getButtonList()) {
                if (b.getName().equals(input)) {
                  b.open();
                }
              }

            } catch (ClassNotFoundException ex) {
              // TODO Auto-generated catch block
              ex.printStackTrace();
            }
          }
          proxy.close();

        } catch (IOException ex) {
          // Creates a new Dialog box.
          JDialog message = new JDialog();
          // Makes dialog box visible.
          message.setVisible(true);
          // Creates Text for Error message.
          JTextField tf = new JTextField("Unable to connect to remote server: ... ");
          // Sets Font for Error message.
          Font font1 = new Font("SansSerif", Font.PLAIN, 20);
          tf.setFont(font1);
          // Sets margins for TextField.
          tf.setMargin(new Insets(50, 50, 50, 50));
          // sets alignment
          tf.setHorizontalAlignment(JTextField.CENTER);
          // adds content to error message.
          message.getContentPane().add(tf);
          message.pack();

          message.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
          message.setLocation(
              (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
              (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
          System.err.println("Unable to connect to remote server.");
          com.setLabel("Connect Online");
        }
      }
    }, "Remote Server Connect").start();

  }

  /**
   * Stops the connection to the proxy server.
   */
  public static void stopServer() {
    keepAlive = false;
    try {
      proxy.close();
    } catch (IOException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
  }

  /**
   * Main method.
   * 
   * @param args
   *          commandline arguments.
   */
  public static void main(String... args) {

    ArrayList<String> list = new ArrayList<>();
    list.add("BLANKSNDKNF");
   // connect(list);

  }

}