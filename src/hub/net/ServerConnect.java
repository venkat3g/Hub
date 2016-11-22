package hub.net;

import java.awt.MenuItem;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import hub.file.manager.HubManager;
import hub.runnable.IRunnableButton;
import hub.window.VisualPane;

/**
 * Server that will connect to proxy where client will connect as well.
 * 
 * @author Venkat Garapati
 *
 */
public class ServerConnect extends Thread {

  // private static String host = "localhost";
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
   * @param manager
   *          manager for lists sent to remote server.
   * @param com
   *          MenuItem which is displayed for user.
   */
  public static void connect(HubManager manager, MenuItem com) {
    new Thread(new Runnable() {

      @Override
      public void run() {
        try {
          keepAlive = true;
          proxy = new Socket(host, port);

          ObjectOutputStream out = new ObjectOutputStream(proxy.getOutputStream());

          ObjectInputStream in = new ObjectInputStream(proxy.getInputStream());

          out.writeObject("Server Connected");
          out.writeObject(manager.getStringButtonList());

          while (proxy.isConnected() && keepAlive) {
            try {
              String input = (String) in.readObject();
              System.out.println(input);
              if (input.equals("reload")) {
                out.writeObject(manager.getStringButtonList());
              } else {
                ArrayList<IRunnableButton> buttonList = manager.getButtonList();
                for (int i = 0; i < buttonList.size(); i++) {
                  if (buttonList.get(i).getName().equals(input)) {
                    buttonList.get(i).open();
                  }
                }
              }

            } catch (ClassNotFoundException ex) {
              // TODO Auto-generated catch block
              ex.printStackTrace();
            }
          }
          proxy.close();

        } catch (IOException ex) {
          // Shows message box with error message.
          JOptionPane.showMessageDialog(null, "Unable to connect to Remote Server ... ");
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

}
