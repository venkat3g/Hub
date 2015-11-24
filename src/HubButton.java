import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

public class HubButton extends JButton implements ActionListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FileManager profile;

	public HubButton(FileManager profile) {
		super(profile.getFileName());
		this.profile = profile;
	}

	public FileManager getFProfile() {
		return profile;
	}

	public static void openButton(ActionEvent e) {
		System.out.println(((HubButton) e.getSource()).getFProfile().getProgramType());
		if ((e.getSource().getClass()) == HubButton.class
				&& ((HubButton) e.getSource()).getFProfile().getProgramType().equals(FileManager.PROGRAM_TYPE)) {
			System.out.println(((HubButton) e.getSource()).getFProfile().getFilePath());
			String temp = ((HubButton) e.getSource()).getFProfile().getFilePath();
			System.out.println("Here " + temp);

			try {
				@SuppressWarnings("unused")
				Process process = new ProcessBuilder("cmd", "/c", temp).start();
			} catch (IOException e1) {

				e1.printStackTrace();
			}
		}
		if ((e.getSource().getClass()) == HubButton.class
				&& ((HubButton) e.getSource()).getFProfile().getProgramType().equals(FileManager.WEBSITE_TYPE)) {

			String url = ((HubButton) e.getSource()).getFProfile().getFilePath();
			System.out.println(url);
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
		}
	}

	// Opens whatever button does
	public static void openButton(HubButton b) {
		if (b.getFProfile().getProgramType().equals(FileManager.PROGRAM_TYPE)) {
			String temp = b.getFProfile().getFilePath();
			System.out.println("Here " + temp);
			try {
				@SuppressWarnings("unused")
				Process process = new ProcessBuilder("cmd", "/c", temp).start();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (b.getFProfile().getProgramType().equals(FileManager.WEBSITE_TYPE)) {
			String url = b.getFProfile().getFilePath();
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
		}
	}

	public static void openButtonLocation(HubButton HubButton) {
		String location = HubButton.getFProfile().getFilePath().substring(0,
				HubButton.getFProfile().getFilePath().lastIndexOf("\\"));
		System.out.println(location);
		try {
			@SuppressWarnings("unused")
			Process process = new ProcessBuilder("cmd", "/c", "explorer.exe " + location).start();
		} catch (IOException e) {
			System.out.println("openButtonLocation function/method has failed");
		}
	}

	private void changeName() {

		HubButton local = this;

		JPopupMenu jpm = new JPopupMenu("Change Name of " + getFProfile().getFileName());
		jpm.setPopupSize(200, 100);
		jpm.requestFocus();
		JLabel aboveTextLabel = new JLabel("Enter Name: ");
		JTextArea shortcutField = new JTextArea(getFProfile().getFileName());
		JButton okayButton = new JButton("Okay");
		JButton cancelButton = new JButton("Cancel");

		jpm.add(aboveTextLabel);
		jpm.add(shortcutField);
		jpm.add(okayButton);
		jpm.add(cancelButton);

		shortcutField.requestFocus();

		okayButton.addActionListener(e -> {
			String tmp = shortcutField.getText();
			getFProfile().setFileName(tmp);
			jpm.setVisible(false);
			jpm.setEnabled(false);
			MainWindow.loadFiles();
			local.setText(tmp);
			setComponentPopupMenu(null);
			System.gc();

		});

		cancelButton.addActionListener(e -> {

			jpm.setVisible(false);
			jpm.setEnabled(false);
			setComponentPopupMenu(null);
			System.gc();

		});

		jpm.updateUI();

		setComponentPopupMenu(jpm);
		jpm.setBorderPainted(true);

		jpm.show(this, 0, 0);

	}

	private void changeImage(HubButton e) {

		String[] types = { "png", "jpg" };
		JFileChooser jfc = MainWindow.makeFileChooser("Images", types);
		jfc.showOpenDialog(this);
		File imageLoc = jfc.getSelectedFile();
		ImageIcon icon = new ImageIcon(imageLoc.getAbsolutePath());
		String imageString = imageLoc.getAbsolutePath();
		Path source = Paths.get(imageLoc.getAbsolutePath());

		try {
			File imagesPath = new File("Images\\");
			imagesPath.mkdirs();

			Files.copy(source, Paths.get("Images/" + imageString.substring(imageString.lastIndexOf("\\") + 1)),
					StandardCopyOption.REPLACE_EXISTING);
			/*
			 * PrintWriter pw = new PrintWriter(new
			 * FileWriter(MainWindow.imageFile, true)); pw.println("Images/" +
			 * imageString.substring(imageString.lastIndexOf("\\") + 1) + " ,
			 * Name: " + (e).getProfile().getName()); pw.close();
			 */
			// System.out.println(getFProfile());
			getFProfile().setImageLoc(imagesPath.getPath() + imageString.substring(imageString.lastIndexOf("\\")));
			Image image = icon.getImage();
			Image newimg = image.getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH); // scale
																							// it
																							// the
																							// smooth
																							// way

			ImageIcon newIcon = new ImageIcon(newimg);
			(e).setIcon(newIcon);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private void changeShortcut(HubButton HubButton) {

		JPopupMenu jpm = new JPopupMenu("Add Shortcut to " + HubButton.getFProfile().getShortcut());
		jpm.setPopupSize(200, 100);
		jpm.requestFocus();
		JLabel aboveTextLabel = new JLabel("Enter Shortcut (only modifier Shift)");
		JTextArea shortcutField = new JTextArea(HubButton.getFProfile().getShortcut());
		JButton okayButton = new JButton("Okay");
		JButton cancelButton = new JButton("Cancel");

		jpm.add(aboveTextLabel);
		jpm.add(shortcutField);
		jpm.add(okayButton);
		jpm.add(cancelButton);

		shortcutField.requestFocus();

		shortcutField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (checkForValue(e)) {
					if (e.getModifiers() == 0)
						shortcutField.setText("" + e.getKeyChar());
					else if (e.isShiftDown())
						shortcutField.setText("Shift + " + e.getKeyChar());

				} else
					shortcutField.setText(shortcutField.getText());
			}

			private boolean checkForValue(KeyEvent e) {
				for (char c = 'a'; c <= 'z'; c++)
					if (("" + e.getKeyChar()).equalsIgnoreCase("" + c))
						return true;
				for (char c = '0'; c <= '9'; c++)
					if (e.getKeyChar() == c)
						return true;
				return false;
			}
		});

		okayButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String tmp = shortcutField.getText();
				if (testCase(tmp)) {
					getFProfile().setShortcut(tmp);
					jpm.setVisible(false);
					HubButton.setComponentPopupMenu(null);
					System.gc();
				}
			}

			private boolean testCase(String tmp) {
				if (tmp.length() == 1)
					return true;
				System.out.println("" + tmp.substring(tmp.lastIndexOf(" + ") + 3).length());
				if (tmp.substring(tmp.lastIndexOf(" + ") + 3).length() == 1)
					return true;
				return false;
			}

		});

		cancelButton.addActionListener(e -> {

			jpm.setVisible(false);
			jpm.setEnabled(false);
			HubButton.setComponentPopupMenu(null);
			System.gc();

		});

		jpm.updateUI();

		HubButton.setComponentPopupMenu(jpm);
		jpm.setBorderPainted(true);

		jpm.show(HubButton, 0, 0);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			JPopupMenu tmpMenu = new JPopupMenu();
			HubButton tmpButton = (HubButton) e.getComponent();

			JMenuItem addIcons = new JMenuItem("Change Image");
			JMenuItem addShortcut = new JMenuItem("Change Shortcut");
			JMenuItem openFileLocation = new JMenuItem("Open Location");
			JMenuItem changeButtonName = new JMenuItem("Change Name");

			addIcons.addActionListener(e1 -> changeImage(tmpButton));

			addShortcut.addActionListener(e1 -> changeShortcut(tmpButton));

			openFileLocation.addActionListener((ActionEvent e1) -> openButtonLocation(tmpButton));

			changeButtonName.addActionListener(e1 -> changeName());

			tmpMenu.add(addIcons);
			tmpMenu.add(addShortcut);
			if (tmpButton.getFProfile().getProgramType().equals(FileManager.PROGRAM_TYPE))
				tmpMenu.add(openFileLocation);
			tmpMenu.add(changeButtonName);
			tmpMenu.setVisible(true);
			tmpMenu.show(e.getComponent(), e.getX(), e.getY());

		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (MainWindow.deleteButtonsOff)
			openButton(e);
		else
			cPane.removeButton(this);
	}
}
