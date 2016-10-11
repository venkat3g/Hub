package hub.file.manager;

import java.io.File;
import java.util.ArrayList;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Element;

import hub.file.xml.XmlNode;
import hub.file.xml.XmlParser;
import hub.runnable.IRunnableButton;
import hub.runnable.Program;
import hub.runnable.Website;

public class HubManager {

  private ArrayList<IRunnableButton> buttonList = new ArrayList<>();

  private XmlNode rootNode;

  public HubManager(File hubButtonFile) {
    rootNode = XmlNode.getXmlRootNode(hubButtonFile);
    generateButtonList();
  }

  private void generateButtonList() {
    for (int i = 0; i < rootNode.getNumberChildren(); i++) {

      // Skips the closing tags.
      XmlNode child = rootNode.getChildByName("Button", ++i);
      if (child != null) {
        /*
         * Gets information from the node to pass into the IRunnableButton.
         */
        String name = child.getChildByName("Name").getTextContent();
        String filepath = child.getChildByName("Location").getTextContent();
        String shortcut = child.getChildByName("Shortcut").getTextContent();
        String imageLocation = child.getChildByName("ImageLocation").getTextContent();
        String type = child.getChildByName("Type").getTextContent();

        if (type.equals("Program")) {
          buttonList.add(new Program(name, filepath, shortcut, imageLocation, child));
        } else if (type.equals("Website")) {
          buttonList.add(new Website(name, filepath, shortcut, imageLocation, child));
        }
      }
    }
  }

  public ArrayList<IRunnableButton> getButtonList() {
    return buttonList;

  }

  /**
   * 
   * @return returns a string array representation of the buttons.
   */
  public ArrayList<String> getStringButtonList() {
    ArrayList<String> list = new ArrayList<>();
    for (IRunnableButton b : getButtonList()) {
      list.add(b.getName());
    }
    return list;
  }

  /**
   * Adds the button to the xml file.
   * 
   * @param button
   *          reference to the Runnable button.
   */
  public void addButtonToXml(IRunnableButton button) {
    createXmlNode(button);
    getButtonList().add(button);
    try {
      XmlParser.saveFile(XmlParser.getCurrentFile());
    } catch (TransformerException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
  }

  /**
   * Adds the button to the xml.
   * 
   * @param button
   *          button to be added.
   */
  private void createXmlNode(IRunnableButton button) {
    /*
     * Creates the tags for the node for buton.
     */

    Element name = rootNode.createElement("Name");
    name.setTextContent(button.getName());
    Element path = rootNode.createElement("Location");
    path.setTextContent(button.getPath());
    Element shortcut = rootNode.createElement("Shortcut");
    shortcut.setTextContent(button.getShortcut());
    Element imageLocation = rootNode.createElement("ImageLocation");
    imageLocation.setTextContent(button.getImageLoc());
    Element type = rootNode.createElement("Type");
    type.setTextContent(button.getType());

    /*
     * Appends the tags to the node.
     */
    Element node = rootNode.createElement("Button");
    node.appendChild(name);
    node.appendChild(path);
    node.appendChild(shortcut);
    node.appendChild(imageLocation);
    node.appendChild(type);

    XmlNode xmlnode = new XmlNode(node);
    button.setNode(xmlnode);

    rootNode.appendChild(xmlnode);

  }

  /**
   * Removes the button from the xml file. TODO: force HubButton to be only way
   * to removeButton.
   * 
   * @param button
   *          Reference to button that will be removed.
   */
  @SuppressWarnings("unused")
  private void removeButton(IRunnableButton button) {
    button.getNode().removeNode();
  }

  /**
   * Call method after calling remove method from button.
   * 
   * @param button
   *          button to be removed from list.
   */
  public void removeButtonFromList(IRunnableButton button) {
    getButtonList().remove(button);
  }

  /**
   * Saves current xml file.
   */
  public static void saveXml() {
    try {
      XmlParser.saveFile(XmlParser.getCurrentFile());
    } catch (TransformerException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
  }

}
