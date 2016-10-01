package dock.window;

import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import dock.file.manager.HubManager;
import dock.runnable.IRunnableButton;
import dock.window.asset.DefaultButtons;
import dock.window.asset.HubButton;

public class VisualPane extends JPanel {

  private static final long serialVersionUID = 1L;

  private HubManager manager;

  private ArrayList<IRunnableButton> buttonList;

  private File hubButtonFile = new File("Resources/hub_info.xml");
  /*
   * Current workaround for removing buttons.
   */
  static VisualPane visualPane;
  static MainWindow pInstance;

  /**
   * Creates a Pane to display Hub to user.
   * 
   * @param frame
   *          A reference to the Window (JFrame)
   * @param layout
   *          A reference to the GridLayout of the window.
   */
  @SuppressWarnings("static-access")
  public VisualPane(MainWindow frame, GridLayout layout) {
    pInstance = frame;
    visualPane.visualPane = this;

    setLayout(layout);
    try {

      getIRunnables();

      addButtons(this);

      instantiateDefaultButtons(this, frame);

    } catch (Exception ex) {

      ex.printStackTrace();
    }
  }

  public HubManager getManager() {
    return manager;
  }

  private void getIRunnables() {
    manager = new HubManager(hubButtonFile);
    setButtonList(manager.getButtonList());
  }

  private void setButtonList(ArrayList<IRunnableButton> buttonList2) {
    buttonList = buttonList2;
  }

  private void addButtons(VisualPane visualPane) {
    /*
     * for (HubButton h : MainWindow.getButtonList()) { visualPane.add(h); }
     */

    for (IRunnableButton b : manager.getButtonList()) {
      visualPane.add(new HubButton(b));
    }

    System.gc();
  }

  /**
   * Adds default buttons to Pane.
   */
  protected void instantiateDefaultButtons(final VisualPane pane,
      final MainWindow frame) {

    JButton addButtons = new JButton("Add Program");
    JButton webs = new JButton("Add Websites");

    DefaultButtons.initializeDefaultButtonAddProgram(frame, pane, addButtons);

    DefaultButtons.initializeDefaultButtonAddWeb(frame, pane, webs);

    pane.add(addButtons);
    pane.add(webs);

  }

  /**
   * Removes the button from the FileManagerList.
   * 
   * @param hubButton
   *          button to be removed.
   */
  public static void removeButton(HubButton hubButton) {
    try {
      hubButton.getProfile().remove();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    visualPane.remove(hubButton);
    visualPane.revalidate();
    pInstance.pack();
  }

  public ArrayList<IRunnableButton> getButtonList() {
    return buttonList;
  }
}
