package backup;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainWindow implements ActionListener {

	private static JFrame window = new JFrame();
	private static JPanel panel = new JPanel();
	private static JButton jb;
	private static JButton webs;
	private static JMenuItem name;
	private static JMenuItem delete;

	private static File sourcesFile;

	private static ArrayList<JButton> sources = new ArrayList<JButton>(0);

	private static boolean deleteClicked = false;
	private static boolean changeIcons = false;
	private static boolean addShortcuts = false;

	private static String nameOfJar;

	public static void main(String[] args) throws FileNotFoundException {

		instantiateFile();
		fillSources();
		addButtons();
		instatiateFrame();

	}

	// done
	private static void instantiateFile() {
		try {

			sourcesFile = new File("Resources/sourcesFile.dat");
			if (!sourcesFile.exists()) {
				(new File("Resources")).mkdir();
				sourcesFile.createNewFile();
			}

		} catch (IOException ex) {
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
		}

	}

	// done
	private static void instatiateFrame() throws FileNotFoundException {

		window.setVisible(true);
		window.setResizable(true);

		window.setJMenuBar(createMenuBar());

		GridLayout layout = new GridLayout(2, 2);
		panel.setLayout(layout);

		ImageIcon icon = new ImageIcon(
				MainWindow.class.getResource("Coding Dock/image.png"));
		if ((new File("Resources/imgl.dat")).exists()) {
			@SuppressWarnings("resource")
			Scanner read = new Scanner(new File("Resources/imgl.dat"));

			if (read.hasNextLine()) {
				String type = read.nextLine();
				ImageIcon icon1 = new ImageIcon("Images/imageIcon" + type);
				window.setIconImage(icon1.getImage());
			}

		}
		jb = new JButton();
		webs = new JButton("add Websites");
		webs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String URL = JOptionPane.showInputDialog(window,
						"Website URL: ", null);

				String name = JOptionPane.showInputDialog(window,
						"Name Site: ", null);
				String tempPathLine = URL + " , Name: " + name;
				MyFiles tempWeb = new MyFiles(tempPathLine, MyFiles.WEBSITE);
				MyButton tempButton = new MyButton(tempWeb.getPath(), tempWeb
						.getName(), tempWeb);
				tempWeb.addToFile(sourcesFile);
				tempButton.addActionListener(new MainWindow());
				sources.add(tempButton);
				panel.add(tempButton, sources.size() - 1);
				panel.revalidate();
				window.pack();

			}
		});

		jb.setIcon(icon);
		jb.addActionListener(new MainWindow());

		try {
			if ((new Scanner(new File("Resources/name.txt"))).hasNextLine()) {
				nameOfJar = (new Scanner(new File("Resources/name.txt"))
						.nextLine());
				window.setName(nameOfJar);
				window.setTitle(nameOfJar);
			}
		} catch (FileNotFoundException e) {
			try {
				(new File("Resources/name.txt")).createNewFile();
			} catch (IOException e1) {
				System.out.println("Permission Error");
			}
		}

		panel.add(jb);
		panel.add(webs);
		window.add(panel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();

	}

	// Add shortcut menu
	private static JMenuBar createMenuBar() {
		JMenuBar menubar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMenuItem image = new JMenuItem("Change Icon");
		JMenuItem addIcons = new JMenuItem("Add Images");
		JMenuItem addShortcuts = new JMenuItem("Add Shortcuts");

		name = new JMenuItem("Name");
		delete = new JMenuItem("Delete Fields");

		file.add(name);
		edit.add(image);
		edit.add(addIcons);
		edit.add(delete);
		edit.add(addShortcuts);

		addIcons.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteClicked = false;
				window.setTitle("Add Images");
				if (changeIcons) {
					changeIcons = false;
					window.setTitle(nameOfJar);
				} else
					changeIcons = true;
			}

		});

		image.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String[] types = { "png", "jpg" };
				JFileChooser jfc = makeFileChooser("Images", types);
				jfc.showOpenDialog(window);
				File imageLoc = jfc.getSelectedFile();
				ImageIcon icon = new ImageIcon(imageLoc.getAbsolutePath());

				window.setIconImage(icon.getImage());
				Path source = Paths.get(imageLoc.getAbsolutePath());
				try {
					int num = imageLoc.getAbsolutePath().lastIndexOf(".");
					Files.copy(
							source,
							Paths.get("Images/imageIcon"
									+ imageLoc.getPath().substring(num)),
							StandardCopyOption.REPLACE_EXISTING);
					FileWriter t = new FileWriter(
							new File("Resources/imgL.dat"));
					t.write(imageLoc.getAbsolutePath().substring(num));
					t.close();
				} catch (IOException e1) {
				}

			}

		});

		name.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String name = JOptionPane.showInputDialog(window,
						"Enter Title", null);
				System.out.println(name);
				if (!name.equals("")) {
					nameOfJar = name;
					window.setTitle(name);
					window.setName(name);
					try {
						FileWriter write = new FileWriter(new File(
								"Resources/name.txt"));
						write.write(name);
						write.close();

					} catch (IOException e) {
					}
				}
			}
		});

		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				changeIcons = false;
				if (!deleteClicked) {
					deleteClicked = true;
					window.setTitle("Delete Buttons");
				} else {
					deleteClicked = false;
					window.setTitle(nameOfJar);
				}
			}
		});

		addShortcuts.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (MainWindow.addShortcuts) {
					MainWindow.addShortcuts = false;
					window.setTitle(nameOfJar);
				} else {
					MainWindow.addShortcuts = true;
					window.setTitle("Add Shortcuts");
				}

			}

		});

		menubar.add(file);
		menubar.add(edit);
		return menubar;
	}

	// done
	private static void addButtons() {

		for (JButton j : sources) {
			j.addActionListener(new MainWindow());
			if (findImageForButton(j) != null) {
				Image image = findImageForButton(j).getImage(); // transform it

				Image newimg = image.getScaledInstance(120, 120,
						java.awt.Image.SCALE_SMOOTH); // scale it the smooth way

				ImageIcon newIcon = new ImageIcon(newimg);
				j.setIcon(newIcon);
			}

			panel.add(j);
		}
	}

	// done
	@SuppressWarnings("resource")
	private static ImageIcon findImageForButton(JButton j) { // DONE
		String line = "";
		Scanner io;
		ImageIcon tmp = null;
		try {
			io = new Scanner(new File("Resources/images.dat"));
			while (io.hasNextLine()) {
				line = io.nextLine();
				if (line.contains(((MyButton) j).getName()))
					tmp = new ImageIcon(line.substring(0,
							line.lastIndexOf(" , Name:")));
			}
		} catch (FileNotFoundException e) {

		}

		return tmp;

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (deleteClicked) {
			try {
				performDeletedCommands(e);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
		} else if (changeIcons && (e.getSource().getClass()) == MyButton.class)
			performChangeIconsActions(e);
		else if (addShortcuts && (e.getSource().getClass()) == MyButton.class)
			performShortcutsActions(e);
		else {

			if (e.getSource() == jb) {
				createSourceButton();

			}
			if ((e.getSource().getClass()) == MyButton.class
					&& ((MyButton) e.getSource()).getProfile().getFileType()
							.equals(MyFiles.PROGRAM)) {
				System.out.println(((MyButton) e.getSource()).getProfile()
						.getFileType());
				String temp = ((MyButton) e.getSource()).getLoc();
				System.out.println("Here " + temp);

				try {
					@SuppressWarnings("unused")
					Process process = new ProcessBuilder("cmd", "/c", temp)
							.start();
				} catch (IOException e1) {

					e1.printStackTrace();
				}
			}
			if ((e.getSource().getClass()) == MyButton.class
					&& ((MyButton) e.getSource()).getProfile().getFileType()
							.equals(MyFiles.WEBSITE)) {
				System.out.println(((MyButton) e.getSource()).getLoc());
				String url = ((MyButton) e.getSource()).getProfile().getPath();
				if (Desktop.isDesktopSupported()) {
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.browse(new URI(url));
					} catch (IOException | URISyntaxException e1) {

						e1.printStackTrace();
					}
				} else {
					Runtime runtime = Runtime.getRuntime();
					try {
						runtime.exec("xdg-open " + url);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				System.out
						.println("Count of listeners: "
								+ ((MyButton) e.getSource())
										.getActionListeners().length);

			}
		}
		e.getClass();
	}

	private void performShortcutsActions(ActionEvent e) {
		

	}

	// done
	private static void performDeletedCommands(ActionEvent e)
			throws IOException {
		@SuppressWarnings("resource")
		Scanner io = new Scanner(sourcesFile);
		String inputFile = "";
		while (io.hasNextLine()) {
			String temp = io.nextLine();
			if (!temp.equals(""))
				inputFile += temp + "\n";
		}

		if ((e.getSource().getClass()) == MyButton.class) {
			removeLine(((MyButton) e.getSource()), inputFile);
			panel.remove((MyButton) e.getSource());
			panel.revalidate();
			panel.repaint();
			window.pack();
		}

		if (e.getSource() == jb) {
			deleteClicked = false;
			window.setTitle(nameOfJar);
		}

	}

	// done
	private void performChangeIconsActions(ActionEvent e) {

		String[] types = { "png", "jpg" };
		JFileChooser jfc = makeFileChooser("Images", types);
		jfc.showOpenDialog(window);
		File imageLoc = jfc.getSelectedFile();
		ImageIcon icon = new ImageIcon(imageLoc.getAbsolutePath());
		String imageString = imageLoc.getAbsolutePath();
		Path source = Paths.get(imageLoc.getAbsolutePath());

		try {
			File imagesPath = new File("Images\\");
			imagesPath.mkdirs();

			Files.copy(
					source,
					Paths.get("Images/"
							+ imageString.substring(imageString
									.lastIndexOf("\\") + 1)),
					StandardCopyOption.REPLACE_EXISTING);
			PrintWriter pw = new PrintWriter(new FileWriter(new File(
					"Resources/images.dat"), true));
			pw.println("Images/"
					+ imageString.substring(imageString.lastIndexOf("\\") + 1)
					+ " , Name: " + ((MyButton) e.getSource()).getName());
			pw.close();
			Image image = icon.getImage();
			Image newimg = image.getScaledInstance(120, 120,
					java.awt.Image.SCALE_SMOOTH); // scale it the smooth way

			ImageIcon newIcon = new ImageIcon(newimg);
			((MyButton) e.getSource()).setIcon(newIcon);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		window.setTitle("Add Images");

	}

	// done
	private static void fillSources() {
		try {
			@SuppressWarnings("resource")
			Scanner fileIO = new Scanner(sourcesFile);
			while (fileIO.hasNextLine()) {
				String s = fileIO.nextLine();
				if (!s.equals("")) {
					if (s.contains("File:")) {
						MyFiles tempFile = new MyFiles(s, MyFiles.PROGRAM);

						MyButton temp = new MyButton(tempFile.getPath(),
								tempFile.getName(), tempFile);

						sources.add(temp);
					} else if (s.contains("http://") || s.contains("https://")) {
						MyFiles tempFile = new MyFiles(s, MyFiles.WEBSITE);

						MyButton temp = new MyButton(tempFile.getPath(),
								tempFile.getName(), tempFile);

						sources.add(temp);
					}

				}
			}
		} catch (IOException e) {

			System.out.println("file not found");
		}

	}

	// done
	private void createSourceButton() {
		String[] types = { "exe", "lnk" };
		JFileChooser jfc = makeFileChooser("Executables & Shorcuts", types);

		int returnValue = jfc.showOpenDialog(window);

		if (returnValue == JFileChooser.APPROVE_OPTION) {

			String filePath = jfc.getSelectedFile().toString();

			int i = 0;
			while (filePath.indexOf("\\", i) != -1) {
				i++;
				filePath.indexOf("\\", i);

			}
			String fileName = filePath.substring(i);
			MyFiles tempFile = new MyFiles("File:" + filePath + " , Name: "
					+ fileName, MyFiles.PROGRAM);
			tempFile.addToFile(sourcesFile);
			MyButton tempButton = new MyButton(tempFile.getPath(),
					tempFile.getName(), tempFile);
			sources.add(tempButton);
			tempButton.addActionListener(new MainWindow());

			panel.add(tempButton, sources.size() - 1);
			panel.revalidate();
			window.pack();

		}

	}

	// done
	private static void removeLine(MyButton button, String inputFile)
			throws IOException {
		panel.remove(button);
		sources.remove(button);
		button.getProfile().removeFromFile(sourcesFile);

	}

	// done
	private static JFileChooser makeFileChooser(String desc, String[] ext) {
		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File("C:/"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter(desc, ext);

		jfc.setFileFilter(filter);
		return jfc;
	}

}

@SuppressWarnings("serial")
class MyButton extends JButton {
	private String location;
	private String name;
	private String line;
	private MyFiles filesProf;

	MyButton(String path, String name, MyFiles prof) {
		super(name);
		this.name = name;
		location = path;
		line = "File:" + path + " , Name: " + name;
		filesProf = prof;
	}

	public String getLoc() {
		return location;
	}

	public String getName() {
		return name;
	}

	public String getLine() {
		return line;
	}

	public MyFiles getProfile() {
		return filesProf;
	}
}
