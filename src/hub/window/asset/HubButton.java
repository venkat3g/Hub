package hub.window.asset;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
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

import hub.runnable.IRunnableButton;
import hub.runnable.Program;
import hub.window.VisualPane;

public class HubButton extends JButton implements ActionListener, MouseListener {

  private final byte scaledValue = 35;
  private final short width = 200;
  private final short height = 100;
  private static final long serialVersionUID = 1L;

  private IRunnableButton profile;

  /**
   * Constructor of a HubButton.
   * 
   * @param buttonProfile
   *          Refers to the info of the button, a oldProfile in essence.
   */
  public HubButton(IRunnableButton buttonProfile) {
    super(buttonProfile.getName());
    this.profile = buttonProfile;
    this.setPreferredSize(new Dimension(width, height));
    initializeButton();

  }

  public IRunnableButton getProfile() {
    return profile;

  }

  /**
   * Opens the button's location.
   * 
   * @param hubButton
   *          Finds the button location of the hubButton.
   */
  public static void openButtonLocation(HubButton hubButton) {
    String location = ((Program) hubButton.getProfile()).getLocation();

    try {
      @SuppressWarnings("unused")
      Process process = new ProcessBuilder("cmd", "/c", "explorer.exe " + location)
          .start();
    } catch (IOException ex) {
      System.out.println("openButtonLocation function/method has failed");
    }
  }

  private void changeName() {

    JPopupMenu jpm = new JPopupMenu("Change Name of " + getProfile().getName());
    jpm.setPopupSize(200, 100);
    jpm.requestFocus();
    JLabel aboveTextLabel = new JLabel("Enter Name: ");
    JTextArea shortcutField = new JTextArea(getProfile().getName());
    JButton okayButton = new JButton("Okay");
    JButton cancelButton = new JButton("Cancel");

    jpm.add(aboveTextLabel);
    jpm.add(shortcutField);
    jpm.add(okayButton);
    jpm.add(cancelButton);

    shortcutField.requestFocus();

    okayButton.addActionListener(e -> {
      String tmp = shortcutField.getText();
      getProfile().setName(tmp);
      jpm.setVisible(false);
      jpm.setEnabled(false);
      this.setText(tmp);
      setComponentPopupMenu(null);
      getProfile().update();
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

  /**
   * Changes the image associated with a button.
   * 
   * 
   */
  private void changeImage() {

    /*
     * Defines the types that will be allowed to be chosen by user.
     */
    String[] types = { "png", "jpg" };
    /*
     * Instantiates a File Chooser using the method.
     */
    JFileChooser jfc = DockFileChooser.makeFileChooser("Images", types);
    /*
     * Displays the file chooser to the user.
     */
    jfc.showOpenDialog(this);

    /*
     * File chosen by user becomes the imageLocation for the button.
     */
    File imageLoc = jfc.getSelectedFile();
    /*
     * Instantiates an image icon from the path.
     */
    ImageIcon icon = new ImageIcon(imageLoc.getAbsolutePath());
    /*
     * Creates a temp variable source which refers to the source of the
     * imageLocation.
     */
    String imageString = imageLoc.getAbsolutePath();
    Path source = Paths.get(imageLoc.getAbsolutePath());

    try {
      /*
       * Creates the images path for the Images to be saved to.
       */
      File imagesPath = new File("Images\\");
      imagesPath.mkdirs();

      /*
       * Copies the file from the source directory to the Images directory.
       */
      Files.copy(source,
          Paths.get("Images/" + imageString.substring(imageString.lastIndexOf("\\") + 1)),
          StandardCopyOption.REPLACE_EXISTING);
      /*
       * Gets the FileManager Profile to update the image location.
       */
      getProfile().setImageLoc(
          imagesPath.getPath() + imageString.substring(imageString.lastIndexOf("\\")));
      /*
       * Makes the icon an image.
       */
      Image image = icon.getImage();
      /*
       * Scales the image 'smoothly'
       */
      Image newimg = image.getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH);

      ImageIcon newIcon = new ImageIcon(newimg);
      /*
       * Sets the image icon.
       */
      setIcon(newIcon);
      // Updates the profile's node.
      getProfile().update();
    } catch (IOException e1) {
      e1.printStackTrace();
    }

  }

  private void changeShortcut(HubButton hubButton) {

    JPopupMenu jpm = new JPopupMenu(
        "Add Shortcut to " + hubButton.getProfile().getShortcut());
    jpm.setPopupSize(200, 100);
    jpm.requestFocus();
    JLabel aboveTextLabel = new JLabel("Enter Shortcut (only modifier Shift)");
    JTextArea shortcutField = new JTextArea(hubButton.getProfile().getShortcut().trim());
    JButton okayButton = new JButton("Okay");
    JButton cancelButton = new JButton("Cancel");

    jpm.add(aboveTextLabel);
    jpm.add(shortcutField);
    jpm.add(okayButton);
    jpm.add(cancelButton);

    shortcutField.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent ev) {
      }

      @Override
      public void keyPressed(KeyEvent ev) {

      }

      /**
       * Implementation of the Keyboard listener interface that allows the
       * button to have a keyListener.
       */
      public void keyReleased(KeyEvent ev) {
        if (ev.getKeyCode() == KeyEvent.VK_ENTER) {
          okayButton.doClick();
        }
        if (checkForValue(ev)) {
          if (ev.getModifiers() == 0) {
            shortcutField.setText("" + ev.getKeyChar());
          }

        } else {
          shortcutField.setText(shortcutField.getText());
        }
      }

      /**
       * Checks that the key released is a char a-z upper and lower 0-9.
       * 
       * @param ev
       *          Refers to the key released event.
       * @return Returns whether the key released is a proper character.
       */
      private boolean checkForValue(KeyEvent ev) {
        for (char c = 'a'; c <= 'z'; c++) {
          if (("" + ev.getKeyChar()).equalsIgnoreCase("" + c)) {
            return true;
          }
        }
        for (char c = '0'; c <= '9'; c++) {
          if (ev.getKeyChar() == c) {
            return true;
          }
        }
        return false;
      }
    });

    okayButton.addActionListener(new ActionListener() {

      /**
       * Action performed by okay button allowing the shortcut to change.
       * 
       * @param ev
       *          Action Event that refers to button press.
       */
      public void actionPerformed(ActionEvent ev) {
        System.out.println(ev.getActionCommand());

        String tmp = shortcutField.getText().trim();
        if (testCase(tmp)) {
          getProfile().setShortcut(tmp);
          jpm.setVisible(false);
          hubButton.setComponentPopupMenu(null);
          hubButton.setToolTipText("Shortcut: " + hubButton.getProfile().getShortcut());
          hubButton.getProfile().update();
          System.gc();

        }
      }

      /**
       * Checks if the string is a valid shortcut.
       * 
       * @param tmp
       *          string representation of the shortcut.
       * @return Returns whether the shortcut contains at most the shift
       *         modifier.
       */
      private boolean testCase(String tmp) {
        if (tmp.length() == 1) {
          return true;
        }
        System.out.println("" + tmp.substring(tmp.lastIndexOf(" + ") + 3).length());
        if (tmp.substring(tmp.lastIndexOf(" + ") + 3).length() == 1) {
          return true;
        }
        return false;
      }

    });

    cancelButton.addActionListener(e -> {

      jpm.setVisible(false);
      jpm.setEnabled(false);
      hubButton.setComponentPopupMenu(null);
      System.gc();

    });

    jpm.updateUI();

    hubButton.setComponentPopupMenu(jpm);
    jpm.setBorderPainted(true);

    jpm.show(hubButton, 0, 0);

    shortcutField.requestFocus();
    shortcutField.setSize(100, 30);

  }

  @Override
  public void mouseClicked(MouseEvent ev) {
  }

  @Override
  public void mousePressed(MouseEvent ev) {
  }

  @Override
  public void mouseReleased(MouseEvent ev) {
    if (ev.getButton() == MouseEvent.BUTTON3) {
      initializeJOptionMenu(ev);
    }
  }

  private void initializeJOptionMenu(MouseEvent ev) {
    HubButton tmpButton = (HubButton) ev.getComponent();

    JMenuItem addIcons = new JMenuItem("Change Image");

    addIcons.addActionListener(e1 -> changeImage());

    /*
     * Popup menuitem for changing the shortcut.
     */
    JMenuItem addShortcut = new JMenuItem("Change Shortcut");
    addShortcut.addActionListener(e1 -> changeShortcut(tmpButton));

    /*
     * Popup menuitem for opening file location of button.
     */
    JMenuItem openFileLocation = new JMenuItem("Open Location");
    openFileLocation.addActionListener((ActionEvent e1) -> openButtonLocation(tmpButton));

    /*
     * Popup menuitem for changing the buttons name.
     */
    JMenuItem changeButtonName = new JMenuItem("Change Name");
    changeButtonName.addActionListener(e1 -> changeName());

    /*
     * Popup menuitem for removing button.
     */
    JMenuItem removeButton = new JMenuItem("Remove");
    removeButton.addActionListener(e1 -> VisualPane.removeButton(this));

    /*
     * Instantiates the Popup Menu that will display the menuitems above.
     */
    JPopupMenu tmpMenu = new JPopupMenu();
    tmpMenu.add(addIcons);
    tmpMenu.add(addShortcut);
    if (tmpButton.getProfile().getType().equals("Program")) {
      tmpMenu.add(openFileLocation);
    }
    tmpMenu.add(changeButtonName);
    tmpMenu.addSeparator();
    tmpMenu.add(removeButton);
    tmpMenu.setVisible(true);
    tmpMenu.show(ev.getComponent(), ev.getX(), ev.getY());

    /*
     * Allows java gc to collect garbage.
     */
    tmpMenu = null;
    openFileLocation = null;
    changeButtonName = null;
    addShortcut = null;
    addIcons = null;

    System.gc();
  }

  @Override
  public void mouseEntered(MouseEvent ev) {
  }

  @Override
  public void mouseExited(MouseEvent ev) {
  }

  @Override
  public void actionPerformed(ActionEvent ev) {
    getProfile().open();
  }

  /**
   * Initializes the HubButton's necessary listeners and values.
   * 
   */
  public void initializeButton() {

    addActionListener(this);
    addMouseListener(this);

    String imageFileName = findImageForButton(this);
    if (imageFileName != null) {

      Image image = (new ImageIcon(imageFileName)).getImage();

      if (!imageFileName.contains("doNotScale")) {
        /*
         * Scales image
         */
        Image newimg = image.getScaledInstance(scaledValue, scaledValue,
            java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);
        setIcon(newIcon);
      } else {
        Image newimg = image;
        ImageIcon newIcon = new ImageIcon(newimg);
        setIcon(newIcon);
      }

      /*
       * Displays the shortcut of the button tool tip
       */
      if (!this.getProfile().getShortcut().isEmpty()) {
        setToolTipText("Shortcut: " + this.getProfile().getShortcut());
      }
    }
  }

  private static String findImageForButton(JButton jb) {

    return ((HubButton) jb).getProfile().getImageLoc();

  }
}
