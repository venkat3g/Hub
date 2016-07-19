import java.awt.EventQueue;
import java.awt.GridLayout;
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
import java.util.ArrayList;
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

import Test.Server;

/*
	Additions:
 		-Access content/shortcuts from phone
 */
public class MainWindow extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;

	private static File xmlFile;
	static ArrayList<FileManager> list;
	static File sourceFile, imageFile, shortcutFile, nameFile;
	static File imageFileType = new File("Resources/imgType.dat"), resourceFolder = new File("Resources"),
			imageFolder = new File("Images");

	static ArrayList<HubButton> sourceList;

	static String hubName = "";

	static cPane cPane;

	public static boolean deleteButtonsOff = true;

	static MainWindow pInstance;

	private static GridLayout layout;

	/*
	 * Call to Initialize Files and loads resource files/Image folder
	 * 
	 * Initialize the window
	 */

	public MainWindow() {

		try {
			instantiateFiles();
			useFileName();
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			instantiateFrame();
			System.gc();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

	}

	public MainWindow(String name) {
		super(name);

		try {
			instantiateFiles();
			changeName(name);
			useFileName();
		} catch (IOException e) {
			System.out.println("One or more files failed to instatiate.");
		}

		try {
			instantiateFrame();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

	}

	/*
	 * Instantiates files and folders
	 */

	private void instantiateFiles() throws IOException {

		xmlFile = FileManager.getFile();

		nameFile = new File(resourceFolder.getPath() + "/Hub_Name.txt");

		if (!xmlFile.exists()) {
			try {
				FileManager.createProgramFile();
			} catch (Exception e) {
				System.out.println("Error in Instantiating XML File");
			}
		}
		if (xmlFile.length() == 0) {
			try {
				FileManager.createProgramFile();
			} catch (Exception e) {
				System.out.println("Error in Instantiating XML File");
			}
		}

		if (!resourceFolder.exists()) {
			resourceFolder.mkdirs();
		}
		if (!imageFolder.exists()) {
			imageFolder.mkdirs();
		}
		if (!nameFile.exists()) {
			nameFile.createNewFile();
		}

	}

	/*
	 * Changes the name of the Hub, and changes the nameFile
	 */
	private void changeName(String name) throws IOException {
		hubName = name;

		FileWriter nameWriter = new FileWriter(nameFile);
		nameWriter.write(hubName);
		nameWriter.close();

	}

	/*
	 * Takes the nameFile and sets the name of MainWindow to value
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

	/*
	 *
	 * Instantiates the window frame
	 *
	 */

	private void instantiateFrame() throws FileNotFoundException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		layout = new GridLayout(2, 1);

		loadFiles();

		cPane = new cPane(this, layout);

		JMenuBar menubar = instantiateMenuBar(this);

		addKeyListener(this);

		if (MainWindow.imageFileType.exists()) {
			@SuppressWarnings("resource")
			Scanner read = new Scanner(MainWindow.imageFileType);

			if (read.hasNextLine()) {
				String type = read.nextLine();
				ImageIcon icon1 = new ImageIcon("Images/imageIcon" + type);
				this.setIconImage(icon1.getImage());
			}

		}

		add(cPane);

		setJMenuBar(menubar);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setFocusable(true);
		this.requestFocus();
		pack();

	}

	private JMenuBar instantiateMenuBar(final MainWindow MainWindow) {
		JMenuBar menubar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMenuItem image = new JMenuItem("Change Icon");

		JMenuItem name = new JMenuItem("Name");
		JMenuItem delete = new JMenuItem("Delete Fields");

		file.add(name);
		edit.add(image);
		edit.addSeparator();
		edit.add(delete);

		image.addActionListener(e -> {
			String[] types = { "png", "jpg" };
			JFileChooser jfc = makeFileChooser("Images", types);
			jfc.showOpenDialog(MainWindow);
			File imageLoc = jfc.getSelectedFile();
			ImageIcon icon = new ImageIcon(imageLoc.getAbsolutePath());

			MainWindow.setIconImage(icon.getImage());
			Path source = Paths.get(imageLoc.getAbsolutePath());
			try {
				int num = imageLoc.getAbsolutePath().lastIndexOf(".");
				Files.copy(source, Paths.get("Images/imageIcon" + imageLoc.getPath().substring(num)),
						StandardCopyOption.REPLACE_EXISTING);
				FileWriter imgTypeWriter = new FileWriter(new File("Resources/imgType.dat"));
				imgTypeWriter.write(imageLoc.getAbsolutePath().substring(num));
				imgTypeWriter.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		});

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

				} catch (IOException e) {
				}
			}
		});

		delete.addActionListener(e -> {
			if (deleteButtonsOff) {
				deleteButtonsOff = false;
				MainWindow.setTitle("Delete Button Mode");
			} else {
				deleteButtonsOff = true;
				MainWindow.setTitle(hubName);

			}
		});

		menubar.add(file);
		menubar.add(edit);
		System.gc();
		return menubar;

	}

	/*
	 * Creates a FileChooser.
	 */

	static JFileChooser makeFileChooser(String desc, String[] ext) {
		JFileChooser jfc = new JFileChooser();

		jfc.setCurrentDirectory(new File("C:/"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter(desc, ext);

		jfc.setFileFilter(filter);
		System.gc();
		return jfc;
	}

	/*
	 * Loads data from files (move to HubFiles)
	 */
	public static void loadFiles() {
		// Instantiates sourceList
		sourceList = new ArrayList<>();
		list = new ArrayList<>();
		try {
			FileManager.instantiateFile();
			list = FileManager.getFileManagerList();
			for (FileManager fm : list) {
				HubButton temp = new HubButton(fm);
				sourceList.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.gc();

	}

	public static void main(String[] args) {

		if (args.length == 1)
			pInstance = new MainWindow(args[0]);
		else
			pInstance = new MainWindow();

		Thread server;

		try {
			server = new Server(1111);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		String tmp;

		tmp = "" + e.getKeyChar();

		for (HubButton b : sourceList) {

			String testCase = b.getFProfile().getShortcut();

			if (testCase == null) {
				System.out.println("One or more files do not have shortcuts.");
			} else if (tmp.equals(testCase)) {
				HubButton.openButton(b);
			} else if (testCase.endsWith(tmp)) {
				HubButton.openButton(b);
			}
		}
	}

}
