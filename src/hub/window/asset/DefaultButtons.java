package hub.window.asset;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import hub.file.manager.HubManager;
import hub.runnable.IRunnableButton;
import hub.runnable.Program;
import hub.runnable.Website;
import hub.window.MainWindow;
import hub.window.VisualPane;

public class DefaultButtons {

  /**
   * Adds the Add Button Button to the Hub.
   * 
   * @param window
   *          reference to the JFrame that will have the Add Button.
   * @param addButton
   *          reference to the JButton addButton.
   */
  public static void initializeDefaultButtonAddProgram(MainWindow window,
      VisualPane visualPane, JButton addButton) {
    addButton.addActionListener(evt -> {

      new Thread(() -> {

        /*
         * Makes a JFileChooser
         */
        String[] types = { "exe", "lnk" };
        JFileChooser jfc = DockFileChooser.makeFileChooser("Executables & Shorcuts", types);

        int returnValue = jfc.showOpenDialog(window);
        /*
         * Checks if user hits okay
         */
        if (returnValue == JFileChooser.APPROVE_OPTION) {

          String programPath = jfc.getSelectedFile().toString();
          /*
           * Gets File name
           */
          int i = 0;
          while (programPath.indexOf("\\", i) != -1) {
            i++;
            programPath.indexOf("\\", i);

          }
          String fileName = programPath.substring(i);

          String imageLoc = "";
          /*
           * Gets image from program selected
           */
          ImageIcon tempIcon = (ImageIcon) FileSystemView.getFileSystemView()
              .getSystemIcon(new File(programPath));

          /*
           * Creates a buffered image for saving to Images(resource folder)
           */
          BufferedImage bi = new BufferedImage(tempIcon.getIconWidth(),
              tempIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);

          Graphics2D g2 = bi.createGraphics();
          g2.drawImage(tempIcon.getImage(), 0, 0, null);
          g2.dispose();

          try {
            ImageIO.write(bi, "png", new File("Images/" + fileName + ".doNotScale.png"));
            imageLoc = "Images/" + fileName + ".doNotScale.png";

          } catch (IOException ex) {
            ex.printStackTrace();
          }

          /*
           * Sets new button.
           */
          HubManager manager = visualPane.getManager();
          // Creates a new profile IRunnable
          IRunnableButton buttonProfile = new Program(fileName, programPath, "",
              imageLoc);
          // Creates a new HubButton
          HubButton tempHubButton = new HubButton(buttonProfile);

          visualPane.add(tempHubButton, manager.getButtonList().size());

          manager.addButtonToXml(buttonProfile);
          /*
           * Sets Images for Button
           */
          tempHubButton.setIcon(tempIcon); // Too small
          /*
           * Shows additions to the JFrame
           */
          visualPane.revalidate();
          window.pack();

        }
        window.requestFocus();

      }, "Add Program").start();
    }

    );

  }

  /**
   * Adds the website adder button.
   * 
   * @param window
   *          Reference to the window that the button will be added to.
   * @param visualPane
   *          reference to the panel that the button will be added.
   * @param webs
   *          reference to the JButton that will be instantiated.
   */
  public static void initializeDefaultButtonAddWeb(MainWindow window,
      VisualPane visualPane, JButton webs) {
    webs.addActionListener(e -> {
      new Thread(() -> {
        /*
         * Makes JOptionPane for obtaining Website Button
         */
        String url = JOptionPane.showInputDialog(window, "Website URL: ", null);
        /*
         * Adds http: to entries without http: prefix
         */
        if (url != null) {
          if (!url.contains("http://") || !url.contains("https://")) {
            url = "http://" + url;
          }
          String name = JOptionPane.showInputDialog(window, "Name Site: ", null);

          /*
           * Gets new list
           */
          HubManager manager = visualPane.getManager();
          IRunnableButton buttonProfile = new Website(name, url, "", "");
          manager.addButtonToXml(buttonProfile);
          HubButton tempHubButton = new HubButton(buttonProfile);

          /*
           * Loads buttons w/ new button
           */
          /*
           * MainWindow.loadFilesFromManager(); HubButton tempHubButton =
           * MainWindow.getButtonList() .get(MainWindow.getList().size() - 1);
           */

          visualPane.add(tempHubButton, manager.getButtonList().size() - 1);
          window.revalidate();
          window.pack();
          window.requestFocus();

        }
      }, "Add Website").start();
    });
  }

}
