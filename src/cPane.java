import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

import com.test.FileManager;

public class cPane extends JPanel {

	/**
	 
	 */
	int scaled_value = 35;
	private static final long serialVersionUID = 1L;
	static cPane cPane;
	static MainWindow pInstance;

	@SuppressWarnings("static-access")
	public cPane(MainWindow frame, GridLayout layout) {
		pInstance = frame;
		cPane.cPane = this;

		setLayout(layout);
		try {

			addButtons(this);

			instantiateDefaultButtons(this, frame);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void addButtons(cPane cPane) {

		for (HubButton h : MainWindow.sourceList) {
			h.addActionListener(h);
			h.addMouseListener(h);

			if (findImageForButton(h) != null) {
				String imageFileName = findImageForButton(h); // transform
																// it
				Image image = (new ImageIcon(imageFileName)).getImage();

				if (!imageFileName.contains("doNotScale")) {

					Image newimg = image.getScaledInstance(scaled_value, // scaled
							scaled_value, java.awt.Image.SCALE_SMOOTH); // image
					ImageIcon newIcon = new ImageIcon(newimg);
					h.setIcon(newIcon);
				} else {
					Image newimg = image;
					ImageIcon newIcon = new ImageIcon(newimg);
					h.setIcon(newIcon);
				}

			}
			// Displays the shortcut of the button tool tip
			if (!h.getFProfile().getShortcut().isEmpty())
				h.setToolTipText("Shortcut: " + h.getFProfile().getShortcut());
			cPane.add(h);
		}

		System.gc();
	}

	static String findImageForButton(JButton j) {

		return ((HubButton) j).getFProfile().getImageLoc();

	}

	/*
	 * Adds default buttons to Pane
	 */
	protected void instantiateDefaultButtons(final JPanel pane, final MainWindow frame) {

		JButton addPrograms = new JButton();
		JButton webs = new JButton("add Websites");

		addPrograms.addActionListener(evt -> {

			new Thread(() -> {// Makes a JFileChooser

				String[] types = { "exe", "lnk" };
				JFileChooser jfc = MainWindow.makeFileChooser("Executables & Shorcuts", types);

				int returnValue = jfc.showOpenDialog(frame);
				// Checks if user hits okay
				if (returnValue == JFileChooser.APPROVE_OPTION) {

					String programPath = jfc.getSelectedFile().toString();
					// Gets File name
					int i = 0;
					while (programPath.indexOf("\\", i) != -1) {
						i++;
						programPath.indexOf("\\", i);

					}
					String fileName = programPath.substring(i);

					// Gets new List
					try {
						MainWindow.list = FileManager.addProgram(fileName, programPath, " ", " ",
								FileManager.PROGRAM_TYPE);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					// Loads all buttons w/ new button
					MainWindow.loadFiles();
					HubButton tempButton = MainWindow.sourceList.get(MainWindow.list.size() - 1);
					tempButton.getFProfile().setImageLoc("Images/" + fileName + ".doNotScale.png");
					tempButton.addMouseListener(tempButton);
					tempButton.addActionListener(tempButton);
					pane.add(tempButton, MainWindow.sourceList.size() - 1);

					// Gets image from program selected
					ImageIcon tempIcon = (ImageIcon) FileSystemView.getFileSystemView()
							.getSystemIcon(new File(programPath));

					// Creates a buffered image for saving to Images(resource
					// folder)
					BufferedImage bi = new BufferedImage(tempIcon.getIconWidth(), tempIcon.getIconHeight(),
							BufferedImage.TYPE_4BYTE_ABGR);

					Graphics2D g2 = bi.createGraphics();
					g2.drawImage(tempIcon.getImage(), 0, 0, null);
					g2.dispose();

					try {
						ImageIO.write(bi, "png", new File("Images/" + fileName + ".doNotScale.png"));
					} catch (IOException e) {
						e.printStackTrace();
					}

					// Sets Images for Button
					tempButton.setIcon(tempIcon); // Too small
					// Shows additions to the JFrame
					pane.revalidate();
					frame.pack();

				}
				frame.requestFocus();

			}, "Add Program").start();
		}

		);

		webs.addActionListener(e -> {
			new Thread(() -> {
				// Makes JOptionPane for obtaining Website Button
				String URL = JOptionPane.showInputDialog(frame, "Website URL: ", null);
				// Adds http: to entries without http: prefix
				if (URL == null)
					;
				else {
					if (!URL.contains("http://") || !URL.contains("https://"))
						URL = "http://" + URL;
					String name = JOptionPane.showInputDialog(frame, "Name Site: ", null);

					// Gets new list
					try {
						MainWindow.list = FileManager.addProgram(name, URL, "", "", FileManager.WEBSITE_TYPE);
					} catch (Exception e1) {

						e1.printStackTrace();
					}
					// Loads buttons w/ new button
					MainWindow.loadFiles();
					HubButton tempHubButton = MainWindow.sourceList.get(MainWindow.list.size() - 1);

					tempHubButton.addActionListener(tempHubButton);
					tempHubButton.addMouseListener(tempHubButton);

					pane.add(tempHubButton, MainWindow.sourceList.size() - 1);
					frame.revalidate();
					frame.pack();
					frame.requestFocus();

				}
			}, "Add Website").start();
		});

		ImageIcon icon = new ImageIcon(cPane.class.getResource("Coding Dock/image.png"));

		addPrograms.setIcon(icon);

		pane.add(addPrograms);
		pane.add(webs);

	}

	public static void removeButton(HubButton hubButton) {
		try {
			hubButton.getFProfile().removeProgram();
		} catch (Exception e) {
			e.printStackTrace();
		}

		cPane.remove(hubButton);
		cPane.revalidate();
		pInstance.pack();
	}
}
