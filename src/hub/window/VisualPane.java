package hub.window;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import hub.file.manager.HubManager;
import hub.runnable.IRunnableButton;
import hub.window.asset.DefaultButtons;
import hub.window.asset.GridManager;
import hub.window.asset.HubButton;

/**
 * Visual Panel which will contain the buttons that the user will interact with.
 * 
 * @author Venkat Garapati
 *
 */
public class VisualPane extends JPanel {

  private static final long serialVersionUID = 1L;

  private HubManager manager;

  private ArrayList<IRunnableButton> buttonList;

  private File hubButtonFile = new File("Resources/buttonList.xml");
  /*
   * Current workaround for removing buttons.
   */
  private static MainWindow pInstance;
  public static VisualPane visualPane;

  /**
   * Creates a Pane to display Hub to user.
   * 
   * @param frame
   *          A reference to the Window (JFrame)
   */
  @SuppressWarnings("static-access")
  public VisualPane(MainWindow frame) {
    pInstance = frame;
    visualPane.visualPane = this;
    try {

      getIRunnables();

      setLayout(GridManager.startGridManager(buttonList));

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
      visualPane.manager.removeButtonFromList(hubButton.getProfile());
      hubButton.getProfile().remove();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    visualPane.remove(hubButton);
    visualPane.revalidate();
    pInstance.pack();

  }

  public static void update() {
    visualPane.revalidate();
    pInstance.pack();
  }

  public ArrayList<IRunnableButton> getButtonList() {
    return buttonList;
  }
}
