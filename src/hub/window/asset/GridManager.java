package hub.window.asset;

import java.awt.GridLayout;
import java.util.ArrayList;

import hub.runnable.IRunnableButton;
import hub.window.VisualPane;

/**
 * Manages the grid layout. TODO: issues with
 * 
 * @author Venkat Garapati
 */
public class GridManager extends Thread {

  private ArrayList<IRunnableButton> list;
  private boolean keepAlive = false;
  private GridLayout layout;

  private GridManager(ArrayList<IRunnableButton> list, GridLayout grid) {
    keepAlive = true;
    layout = grid;
    this.list = list;
    setName("GridManager");
  }

  @Override
  public void run() {
    while (keepAlive) {
      if (list.size() + 2 <= 4) {
        layout.setRows(2);
      } else if (list.size() + 2 <= 9) {
        layout.setRows(3);
      } else {
        layout.setRows(4);
      }
      VisualPane.update();

    }
  }

  /**
   * Start Gridlayout thread which will maintain the gridlayout on with active
   * changes to buttonList. TODO: may cause issue when removing from list before
   * removing from visualPane
   * 
   * @param buttonList
   *          the list which will alter the layout
   * @return a reference to the gridlayout which will be actively monitored.
   */
  public static GridLayout startGridManager(ArrayList<IRunnableButton> buttonList) {
    GridLayout layout = new GridLayout(2, 1);
    GridManager manager = new GridManager(buttonList, layout);
    manager.start();

    return layout;
    // return new GridLayout(2,1); // Temp fix, I believe
  }

}
