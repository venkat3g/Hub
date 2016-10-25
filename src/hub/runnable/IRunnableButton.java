package hub.runnable;

import hub.file.xml.XmlNode1;

/**
 * Interface that will serve as the information of the 'Button'.
 * 
 * @author Venkat Garapati
 *
 */
public interface IRunnableButton {

  /**
   * Sets the name of the button.
   * 
   * @param name
   *          new name of the button
   */
  void setName(String name);

  /**
   * Sets the shortcut of the button.
   * 
   * @param shortcut
   *          new shortcut of the button.
   */
  void setShortcut(String shortcut);

  /**
   * Sets the node the refers to this button.
   * 
   * @param node
   *          XmlNode that represents this node.
   */
  void setNode(XmlNode1 node);

  /**
   * Sets the path of the this button.
   * 
   * @param path
   *          new path of the button.
   */
  void setPath(String path);

  /**
   * Sets the path of the image.
   * 
   * @param imageLoc
   *          location of the image.
   */
  void setImageLoc(String imageLoc);

  /**
   * 
   * @return gets the name of the button.
   */
  String getName();

  /**
   * 
   * @return gets the path of the button.
   */
  String getPath();

  /**
   * 
   * @return gets the shortcut of the button.
   */
  String getShortcut();

  /**
   * 
   * @return gets teh image location of the button.
   */
  String getImageLoc();

  /**
   * 
   * @return gets the program type of the button.
   */
  String getType();

  /**
   * 
   * @return returns the node of the document.
   */
  XmlNode1 getNode();

  /**
   * Removes the button.
   */
  void remove();

  /**
   * Opens the button.
   */
  void open();

  /**
   * Updates the button's node.
   */
  void update();

}
