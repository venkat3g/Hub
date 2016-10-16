package hub.window;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import hub.net.ServerConnect;
import hub.net.ServerLocal;
import hub.runnable.IRunnableButton;
import hub.window.asset.DockFileChooser;

/**
 * Window which will manage Window operations and instantiate JFrame.
 * 
 * @author Venkat Garapati
 *
 */
public class MainWindow extends JFrame implements KeyListener {

  private static final long serialVersionUID = 1L;

  private static boolean localServerEnabled = false;
  private static boolean connectOnlineEnabled = false;

  // static File sourceFile;
  private static File nameFile;

  private static File imageFileType = new File("Resources/imgType.dat");
  private static File resourceFolder = new File("Resources");
  private static File imageFolder = new File("Images");

  private static String hubName = "";

  private static int port = 1024;

  /**
   * Public constructor of the MainWindow for the Hub.
   */
  public MainWindow() {

    try {
      instantiateFiles();
      useFileName();
      System.gc();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    try {
      instantiateFrame();
      System.gc();
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }

  }

  /**
   * Public constructor of the MainWindow for the Hub.
   * 
   * @param name
   *          Specific name for the Hub.
   */
  public MainWindow(String name) {
    super(name);

    try {
      instantiateFiles();
      changeName(name);
      useFileName();
    } catch (IOException ex) {
      System.out.println("One or more files failed to instatiate.");
    }

    try {
      instantiateFrame();
    } catch (FileNotFoundException ex) {

      ex.printStackTrace();
    }

  }

  /**
   * Instantiates the files needed for Hub.
   */
  private void instantiateFiles() throws IOException {

    nameFile = new File(resourceFolder.getPath() + "/Hub_Name.txt");

    makeDirs();

    /*
     * Checks if the nameFile exists otherwise makes one.
     */
    if (!nameFile.exists()) {
      nameFile.createNewFile();
    }

  }

  /**
   * Creates the necessary directories.
   */
  private void makeDirs() throws IOException {
    /*
     * Checks if the directory exists otherwise creates the directory.
     */
    if (!resourceFolder.exists()) {
      resourceFolder.mkdirs();
    }
    if (!imageFolder.exists()) {
      imageFolder.mkdirs();
    }

  }

  /**
   * Changes the name of the Hub.
   * 
   * @param name
   *          new name of the hub.
   * @throws IOException
   *           Throws and File writing exception.
   */
  private void changeName(String name) throws IOException {
    hubName = name;

    FileWriter nameWriter = new FileWriter(nameFile);
    nameWriter.write(hubName);
    nameWriter.close();

  }

  /**
   * Finds the file name from the name file and sets the name of the Hub to the
   * files specification.
   * 
   * @throws FileNotFoundException
   *           throws error if file is not found.
   */
  private void useFileName() throws FileNotFoundException {
    @SuppressWarnings("resource")
    Scanner scanName = new Scanner(nameFile);
    String line = "";
    if (scanName.hasNextLine()) {
      line = scanName.nextLine();
      this.setTitle(line.toString());
      this.setName(line.toString());
      line = null;

      /*
       * Temporary port changer
       */
      if (scanName.hasNextInt()) {
        port = scanName.nextInt();
      }

      scanName = null;

      System.gc();
    } else {
      this.setName("");
      this.setTitle("");
      scanName = null;
      line = null;
      System.gc();
    }
  }

  /**
   * Instantiates the frame of the Window.
   * 
   * @throws FileNotFoundException
   *           Throws a file not found exception if any files are corrupted.
   */
  private void instantiateFrame() throws FileNotFoundException {
    /*
     * Determines which UI to give the Hub.
     */
    uiLookFeel();

    /*
     * Adds a key listener to the window.
     */
    addKeyListener(this);

    /*
     * Checks if the file for icon image exists.
     */
    initializedIcon();

    /*
     * Creates a new VisualPane and adds the pane to the window.
     */
    VisualPane pane = new VisualPane(this);
    getContentPane().add(pane);

    JMenuBar menubar = instantiateMenuBar(this);

    setJMenuBar(menubar);
    setVisible(true);
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    createSystemTrayIcon();

    setFocusable(true);
    this.requestFocus();
    pack();

  }

  private void initializedIcon() {

    /*
     * Checks if the image file exists w/ which extension.
     */
    if (MainWindow.imageFileType.exists()) {
      Scanner read;
      try {
        /*
         * Reads from the file that specifies the imageIcon extension.
         */
        read = new Scanner(MainWindow.imageFileType);
        /*
         * If the reader finds content in the file it appendes it to the
         * imageIcon
         */
        if (read.hasNextLine()) {
          String type = read.nextLine();
          ImageIcon icon1 = new ImageIcon("Images/imageIcon" + type);
          this.setIconImage(icon1.getImage());
        }

      } catch (FileNotFoundException ex) {
        System.out.println("Could not find files needed for Hub Icon.");
      }
    }

  }

  /**
   * TODO: Fix this implementation.
   * 
   * @return returns non-default Icon location.
   */
  private String getIconPath() {
    if (MainWindow.imageFileType.exists()) {
      Scanner read;
      try {
        /*
         * Reads from the file that specifies the imageIcon extension.
         */
        read = new Scanner(MainWindow.imageFileType);
        /*
         * If the reader finds content in the file it appendes it to the
         * imageIcon
         */
        if (read.hasNextLine()) {
          String type = read.nextLine();
          if (new File("Images/imageIcon" + type).exists()) {
            return "Images/imageIcon" + type;
          } else {
            return "none";
          }
        }

      } catch (FileNotFoundException ex) {
        System.out.println("Could not find files needed for Hub Icon.");
      }
    }
    return "none";

  }

  /**
   * Sets the UI of the Hub.
   */
  private void uiLookFeel() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      SwingUtilities.updateComponentTreeUI(this);

    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | UnsupportedLookAndFeelException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Creates a System Tray for the Hub for background processing.
   */
  private void createSystemTrayIcon() {
    try {

      TrayIcon trayIcon = null;

      if (!getIconPath().equals("none")) {
        /*
         * Creates tray icon
         */
        trayIcon = new TrayIcon(
            this.getIconImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
      } else {
        System.out.println("Using default icon.");
        this.setIconImage(
            ImageIO.read(MainWindow.class.getResource("Icon/hub_icon/hub/web/hub.png")));
        trayIcon = new TrayIcon(
            this.getIconImage().getScaledInstance(16, 16, Image.SCALE_FAST));
      }

      SystemTray.getSystemTray().add(trayIcon);

      /*
       * Creates menuitems
       */

      MenuItem exit = new MenuItem("Exit");
      MenuItem open = new MenuItem("Open");
      MenuItem localServer = new MenuItem("Start Local Server");

      localServer.addActionListener(e -> {
        try {
          if (!localServerEnabled) {
            new ServerLocal(port).start();
            localServerEnabled = true;
            localServer.setLabel("Stop Local Server");
          } else {
            ServerLocal.killLocal();
            localServer.setLabel("Start Local Server");
            localServerEnabled = false;
          }
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      });

      /*
       * ActionListener for open menuitem to display application and set focus
       */
      open.addActionListener(e -> {
        MainWindow.this.setVisible(true);
        MainWindow.this.requestFocus();

      });

      /*
       * ActionListener for exit menuitem to exit the application
       */
      exit.addActionListener(e -> System.exit(1));

      // Connects to online server.
      MenuItem connectOnline = new MenuItem("Connect Online");

      connectOnline.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ex) {
          if (connectOnline.getLabel().equals("Connect Online")) {
            ServerConnect
                .connect(VisualPane.visualPane.getManager().getStringButtonList(),connectOnline);
            connectOnlineEnabled = true;
            connectOnline.setLabel("Disconnect Online");
          } else {
            ServerConnect.stopServer();
            connectOnline.setLabel("Connect Online");
            connectOnlineEnabled = false;
          }

        }
      });

      /*
       * Creating and Inflating popupmenu
       */
      PopupMenu popupmenu = new PopupMenu();
      popupmenu.add(open);
      popupmenu.addSeparator();
      popupmenu.add(localServer);
      popupmenu.add(connectOnline);// TODO: for later implementation
      popupmenu.addSeparator();
      popupmenu.add(exit);

      /*
       * Sets tray popupmenu and adds the trayicon to tray
       */
      trayIcon.setPopupMenu(popupmenu);

    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  private JMenuBar instantiateMenuBar(final MainWindow mainWindow) {

    JMenu file = new JMenu("File");
    JMenu edit = new JMenu("Edit");
    JMenuItem image = new JMenuItem("Change Icon");

    JMenuItem name = new JMenuItem("Name");

    file.add(name);
    edit.add(image);

    image.addActionListener(evt -> addIconImage(mainWindow));

    name.addActionListener(evt -> {

      String nameWindow = JOptionPane.showInputDialog(mainWindow, "Enter Title", null);
      if (!name.equals("")) {
        hubName = nameWindow;
        setTitle(nameWindow);
        setName(nameWindow);
        try {
          FileWriter write = new FileWriter(nameFile);
          write.write(nameWindow);
          write.close();

        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    });

    JMenuBar menubar = new JMenuBar();
    menubar.add(file);
    menubar.add(edit);
    System.gc();
    return menubar;

  }

  /**
   * Adds Icon image to the window. TODO: comment
   * 
   * @param mainWindow
   *          Reference to the window.
   */
  private void addIconImage(final MainWindow mainWindow) {
    /*
     * Creates a File Chooser for choosing the icon.
     */
    String[] types = { "png", "jpg" };
    JFileChooser jfc = DockFileChooser.makeFileChooser("Images", types);
    jfc.showOpenDialog(mainWindow);
    File imageLoc = jfc.getSelectedFile();
    ImageIcon icon = new ImageIcon(imageLoc.getAbsolutePath());

    /*
     * Sets the icon image.
     */
    mainWindow.setIconImage(icon.getImage());
    Path source = Paths.get(imageLoc.getAbsolutePath());
    try {
      int num = imageLoc.getAbsolutePath().lastIndexOf(".");
      Files.copy(source,
          Paths.get("Images/imageIcon" + imageLoc.getPath().substring(num)),
          StandardCopyOption.REPLACE_EXISTING);
      FileWriter imgTypeWriter = new FileWriter(new File("Resources/imgType.dat"));
      imgTypeWriter.write(imageLoc.getAbsolutePath().substring(num));
      imgTypeWriter.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }

  }

  /**
   * The main method which starts the Hub application.
   * 
   * @param args
   *          command-line arguments
   */
  public static void main(String[] args) {

    if (args.length == 1) {
      new MainWindow(args[0]);
    } else {
      new MainWindow();
    }

  }

  @Override
  public void keyTyped(KeyEvent ev) {
  }

  @Override
  public void keyPressed(KeyEvent ev) {
  }

  @Override
  public void keyReleased(KeyEvent ev) {
    String key = KeyEvent.getKeyText(ev.getKeyCode());
    System.out.println(key);
    if (key != null) {
      for (IRunnableButton b : VisualPane.visualPane.getButtonList()) {
        if (b.getShortcut().equalsIgnoreCase(key)) {
          b.open();
        }
      }
    }

  }

}
