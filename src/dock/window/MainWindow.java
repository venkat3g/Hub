package dock.window;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import dock.file.FileManager;
import dock.net.Server;

public class MainWindow extends JFrame implements KeyListener {

  private static final long serialVersionUID = 1L;

  private static File xmlFile;

  static File sourceFile;
  static File imageFile;
  static File shortcutFile;
  static File nameFile;

  static File imageFileType = new File("Resources/imgType.dat");
  static File resourceFolder = new File("Resources");
  static File imageFolder = new File("Images");

  static String hubName = "";

  static int port = 11111;

  static MainWindow pInstance;

  private static GridLayout layout;

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

    xmlFile = FileManager.getFile();

    nameFile = new File(resourceFolder.getPath() + "/Hub_Name.txt");

    /*
     * Checks if the xmlFile does not exist, creates if necessary.
     */
    if (!xmlFile.exists()) {
      try {
        FileManager.createProgramFile();
      } catch (Exception ex) {
        System.out.println("Error in Instantiating XML File");
      }
    }
    /*
     * Checks if the xmlFile has content, otherwise creates a new xmlFile.
     */
    if (xmlFile.length() == 0) {
      try {
        FileManager.createProgramFile();
      } catch (Exception ex) {
        System.out.println("Error in Instantiating XML File");
      }
    }

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
     * Sets the initial layout of the Hub. TODO: Create a layout manager class
     * or something.
     */
    layout = new GridLayout(2, 1);

    /*
     * Loads the necessary files.
     */
    // loadFilesFromManager();

    /*
     * Adds a key listener to the window. TODO: Check if needed. Currently
     * managed by MainWindow shift to HubButton.
     */
    addKeyListener(this);

    /*
     * Checks if the file for
     */
    initializedIcon();

    /*
     * Creates a new VisualPane and adds the pane to the window.
     */
    add(new VisualPane(this, layout));

    createSystemTrayIcon();

    JMenuBar menubar = instantiateMenuBar(this);

    setJMenuBar(menubar);
    setVisible(true);
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

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

      /*
       * Creates popupmenu with menuitems
       */
      PopupMenu popupmenu = new PopupMenu();
      MenuItem exit = new MenuItem("Exit");
      MenuItem open = new MenuItem("Open");

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

      /*
       * Inflating popupmenu
       */
      popupmenu.add(open);
      popupmenu.add(exit);

      /*
       * Creates trayicon
       */
      TrayIcon trayIcon = new TrayIcon(
          this.getIconImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
      /*
       * Sets tray popupmenu and adds the trayicon to tray
       */
      trayIcon.setPopupMenu(popupmenu);
      SystemTray.getSystemTray().add(trayIcon);

    } catch (

    Exception ex) {
      System.out.println("Could not create TrayIcon: no icon for application");
    }

  }

  private JMenuBar instantiateMenuBar(final MainWindow MainWindow) {

    JMenu file = new JMenu("File");
    JMenu edit = new JMenu("Edit");
    JMenuItem image = new JMenuItem("Change Icon");

    JMenuItem name = new JMenuItem("Name");

    file.add(name);
    edit.add(image);
    edit.addSeparator();

    image.addActionListener(evt -> addIconImage(MainWindow));

    name.addActionListener(evt -> {

      String nameWindow = JOptionPane.showInputDialog(MainWindow, "Enter Title", null);
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
   * @param MainWindow
   *          Reference to the window.
   */
  private void addIconImage(final MainWindow MainWindow) {
    /*
     * Creates a File Chooser for choosing the icon.
     */
    String[] types = { "png", "jpg" };
    JFileChooser jfc = makeFileChooser("Images", types);
    jfc.showOpenDialog(MainWindow);
    File imageLoc = jfc.getSelectedFile();
    ImageIcon icon = new ImageIcon(imageLoc.getAbsolutePath());

    /*
     * Sets the icon image.
     */
    MainWindow.setIconImage(icon.getImage());
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
   * Creates a File Chooser.
   * 
   * @param desc
   *          Textual description of the filter
   * @param ext
   *          Various filters
   * @return Returns a JFileChooser
   */
  public static JFileChooser makeFileChooser(String desc, String[] ext) {
    JFileChooser jfc = new JFileChooser();

    jfc.setCurrentDirectory(new File("C:/"));
    FileNameExtensionFilter filter = new FileNameExtensionFilter(desc, ext);

    jfc.setFileFilter(filter);
    System.gc();
    return jfc;
  }

  /**
   * The main method which starts the Hub application.
   * 
   * @param args
   *          command-line arguments
   */
  public static void main(String[] args) {

    if (args.length == 1) {
      pInstance = new MainWindow(args[0]);
    } else {
      pInstance = new MainWindow();
    }

    /*
     * Creates Server.
     * 
     * TODO: will become 'client' to aws server that acts as a server.
     */

    // initializeServer();

  }

  /**
   * Creates a thread for the Server.
   */
  private static void initializeServer() {
    Thread server;
    try {
      server = new Server(port);
      server.start();
    } catch (IOException ex) {
      ex.printStackTrace();
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

  }

}
