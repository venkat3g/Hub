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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
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
import hub.window.manager.PropertyManager;

/**
 * Window which will manage Window operations and instantiate JFrame.
 * 
 * @author Venkat Garapati
 *
 */
public class MainWindow extends JFrame implements KeyListener {

    private static final long serialVersionUID = 1L;

    private static boolean localServerEnabled = false;

    private static File resourceFolder = new File("Resources");
    private static File imageFolder = new File("Images");

    private static String hubName = "";

    private static int port = 1024;

    /**
     * Public constructor of the MainWindow for the Hub.
     */
    public MainWindow() {

        try {
            makeDirs();
            useFileName();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            instantiateFrame();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        System.gc();
    }

    /**
     * Public constructor of the MainWindow for the Hub.
     * 
     * @param name
     *            Specific name for the Hub.
     */
    public MainWindow(String name) {
        super(name);

        try {
            makeDirs();
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
        System.gc();
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
     *            new name of the hub.
     * @throws IOException
     *             Throws and File writing exception.
     */
    private void changeName(String name) throws IOException {
        hubName = name;

        PropertyManager.setName(hubName);

    }

    /**
     * Finds the file name from the name file and sets the name of the Hub to
     * the files specification.
     * 
     * @throws FileNotFoundException
     *             throws error if file is not found.
     */
    private void useFileName() throws FileNotFoundException {
        hubName = PropertyManager.getName();

        this.setTitle(hubName);
        this.setName(hubName);

    }

    /**
     * Instantiates the frame of the Window.
     * 
     * @throws FileNotFoundException
     *             Throws a file not found exception if any files are corrupted.
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

        if ("True".equals(PropertyManager.getCloseOp())) {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }

        createSystemTrayIcon();

        setFocusable(true);
        this.requestFocus();
        pack();

    }

    private void initializedIcon() {

        /*
         * Checks if the image file exists w/ which extension.
         */

        if (!PropertyManager.getIconImage().equals("")) {
            ImageIcon icon1 = new ImageIcon(PropertyManager.getIconImage());
            this.setIconImage(icon1.getImage());
        }

    }

    /**
     * .
     * 
     * @return returns non-default Icon location.
     */
    private String getIconPath() {

        return PropertyManager.getIconImage();

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

            if (!getIconPath().equals("")) {
                /*
                 * Creates tray icon
                 */
                trayIcon = new TrayIcon(this.getIconImage().getScaledInstance(16, 16,
                        Image.SCALE_SMOOTH));
            } else {
                System.out.println("Using default icon.");
                this.setIconImage(ImageIO.read(
                        MainWindow.class.getResource("Icon/hub_icon/hub/web/hub.png")));
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
                    e1.printStackTrace();
                }
            });

            /*
             * ActionListener for open menuitem to display application and set
             * focus
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
                        ServerConnect.connect(VisualPane.visualPane.getManager(),
                                connectOnline);
                        connectOnline.setLabel("Disconnect Online");
                    } else {
                        ServerConnect.stopServer();
                        connectOnline.setLabel("Connect Online");
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

        JCheckBoxMenuItem closeOp = new JCheckBoxMenuItem("Stay in Background");

        closeOp.addActionListener(evt -> {
            if (closeOp.getState()) {
                PropertyManager.setCloseOp("False");
                this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            } else {
                PropertyManager.setCloseOp("True");
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

        file.add(closeOp);

        image.addActionListener(evt -> addIconImage(mainWindow));

        name.addActionListener(evt -> {

            String nameWindow = JOptionPane.showInputDialog(mainWindow, "Enter Title",
                    null);
            if (!name.equals("")) {
                hubName = nameWindow;
                setTitle(nameWindow);
                setName(nameWindow);
                PropertyManager.setName(hubName);

            }
        });

        JMenuBar menubar = new JMenuBar();
        menubar.add(file);
        menubar.add(edit);
        System.gc();
        return menubar;

    }

    /**
     * Adds Icon image to the window.
     * 
     * @param mainWindow
     *            Reference to the window.
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
            Files.copy(source, Paths.get("Images/" + imageLoc.getName()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e1) {
            System.err.println("Error copying image.");
        }
        PropertyManager.setIcon("Images/" + imageLoc.getName());

    }

    /**
     * The main method which starts the Hub application.
     * 
     * @param args
     *            command-line arguments
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
        if (key != null) {
            for (IRunnableButton b : VisualPane.visualPane.getButtonList()) {
                if (b.getShortcut().equalsIgnoreCase(key)) {
                    b.open();
                }
            }
        }

    }

}
