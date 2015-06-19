import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

public class cPane extends JPanel {

	/**
	 * 
	 */
	int scaled_value = 45;
	private static final long serialVersionUID = 1L;
	static cPane cPane;
	static MainWindow pInstance;

	@SuppressWarnings("static-access")
	public cPane(MainWindow frame, GridLayout layout) {
		pInstance = frame;
		this.cPane = this;

		setLayout(layout);
		try {
			addButtons(this);
			instantiateDefaultButtons(this, frame);

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}

	private void addButtons(cPane cPane) {
		for (HubButton h : MainWindow.sourceList) {
			h.addActionListener(h);
			h.addMouseListener(h);
			if (findImageForButton(h) != null) {
				String imageFileName = findImageForButton(h); // transform it
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

			cPane.add(h);
		}

	}

	@SuppressWarnings("resource")
	private static String findImageForButton(JButton j) {
		String line = "";
		Scanner imagesIO;
		String tmp = null;
		try {
			imagesIO = new Scanner(MainWindow.imageFile);
			while (imagesIO.hasNextLine()) {
				line = imagesIO.nextLine();
				if (line.contains(((HubButton) j).getProfile().getName()))
					tmp = line.substring(0, line.lastIndexOf(" , Name:"));

			}
		} catch (FileNotFoundException e) {

		}

		return tmp;

	}

	private void instantiateDefaultButtons(final JPanel pane,
			final MainWindow frame) throws FileNotFoundException {

		JButton addPrograms = new JButton();
		JButton webs = new JButton("add Websites");

		addPrograms
				.addActionListener(evt -> {

					String[] types = { "exe", "lnk" };
					JFileChooser jfc = MainWindow.makeFileChooser(
							"Executables & Shorcuts", types);

					int returnValue = jfc.showOpenDialog(frame);

					if (returnValue == JFileChooser.APPROVE_OPTION) {

						String programPath = jfc.getSelectedFile().toString();

						int i = 0;
						while (programPath.indexOf("\\", i) != -1) {
							i++;
							programPath.indexOf("\\", i);

						}
						String fileName = programPath.substring(i);
						HubFiles tempFile = new HubFiles("File:" + programPath
								+ " , Name: " + fileName, HubFiles.PROGRAM);
						tempFile.addToFile(MainWindow.sourceFile);
						HubButton tempButton = new HubButton(tempFile,
								MainWindow.sourceList.size());

						MainWindow.sourceList.add(tempButton);
						tempButton.addActionListener(tempButton);
						tempButton.addMouseListener(tempButton);

						ImageIcon tempIcon = (ImageIcon) FileSystemView
								.getFileSystemView().getSystemIcon(
										new File(programPath));

						BufferedImage bi = new BufferedImage(tempIcon
								.getIconWidth(), tempIcon.getIconHeight(),
								BufferedImage.TYPE_4BYTE_ABGR);

						Graphics2D g2 = bi.createGraphics();
						g2.drawImage(tempIcon.getImage(), 0, 0, null);
						g2.dispose();

						try {
							ImageIO.write(bi, "png", new File("Images/"
									+ fileName + ".doNotScale.png"));
						} catch (IOException e) {
							System.out.println("File Not Found");
						}

						PrintWriter pw;
						try {
							pw = new PrintWriter(new FileWriter(
									MainWindow.imageFile, true));
							pw.println("Images/" + fileName + ".doNotScale.png"
									+ " , Name: " + fileName);
							pw.close();
						} catch (IOException e) {
							System.out.println("File Not Found");
						}

						tempButton.setIcon(tempIcon); // To small

				pane.add(tempButton, MainWindow.sourceList.size() - 1);
				pane.revalidate();
				frame.pack();

			}
			frame.requestFocus();
		}

		);

		webs.addActionListener(e -> {

			String URL = JOptionPane.showInputDialog(frame, "Website URL: ",
					null);

			String name = JOptionPane.showInputDialog(frame, "Name Site: ",
					null);
			String tempPathLine = URL + " , Name: " + name;
			HubFiles tempHubWeb = new HubFiles(tempPathLine, HubFiles.WEBSITE);
			HubButton tempHubButton = new HubButton(tempHubWeb,
					MainWindow.sourceList.size());

			tempHubWeb.addToFile(MainWindow.sourceFile);
			tempHubButton.addActionListener(tempHubButton);
			tempHubButton.addMouseListener(tempHubButton);

			MainWindow.sourceList.add(tempHubButton);
			pane.add(tempHubButton, MainWindow.sourceList.size() - 1);
			frame.revalidate();
			frame.pack();
			frame.requestFocus();

		});

		ImageIcon icon = new ImageIcon(
				MainWindow.class.getResource("Coding Dock/image.png"));

		addPrograms.setIcon(icon);

		pane.add(addPrograms);
		pane.add(webs);

	}

	public static void removeButton(HubButton hubButton) {
		try {
			System.out.println(hubButton.getProfile().toString());
			hubButton.getProfile().removeFromFile(MainWindow.sourceFile,
					hubButton);
			MainWindow.loadFiles();
			cPane.remove(hubButton);
			cPane.revalidate();
			pInstance.pack();
		} catch (IOException e) {
			System.out.println("File not Found");
		}
	}
	// TODO change layout once number of items changes
	// TODO fix removing more than on item... so far removes last multiple

}
