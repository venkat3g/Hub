package hub.runnable;

import java.io.IOException;

import hub.file.manager.HubManager;
import hub.file.xml.XmlNode;

public class Program implements IRunnableButton {

  private String name;
  private String filepath;
  private String shortcut;
  private String imageLocation;
  private XmlNode node;

  /**
   * Instantiates a new Program with the information of the button.
   * 
   * @param name
   *          The name of the button.
   * @param filepath
   *          The filepath of the program.
   * @param shortcut
   *          the shortcut to access the button.
   * @param imageLocation
   *          the image Location for the button.
   * @param node
   *          the node that the button is in the XML doc.
   */
  public Program(String name, String filepath, String shortcut, String imageLocation,
      XmlNode node) {
    setName(name);
    setPath(filepath);
    setShortcut(shortcut);
    setNode(node);
    setImageLoc(imageLocation);

  }

  /**
   * Instantiates a new Program with the information of the button.
   * 
   * @param name
   *          The name of the button.
   * @param filepath
   *          The filepath of the program.
   * @param shortcut
   *          the shortcut to access the button.
   * @param imageLocation
   *          the image Location for the button.
   */
  public Program(String name, String filepath, String shortcut, String imageLocation) {
    setName(name);
    setPath(filepath);
    setShortcut(shortcut);
    setNode(null);
    setImageLoc(imageLocation);

  }

  @Override
  public void setImageLoc(String imageLocation) {
    this.imageLocation = imageLocation;
  }

  @Override
  public void setName(String name) {
    this.name = name;

  }

  @Override
  public void setShortcut(String shortcut) {
    this.shortcut = shortcut;
  }

  @Override
  public void setNode(XmlNode node) {
    this.node = node;
  }

  @Override
  public void setPath(String path) {
    filepath = path;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPath() {
    return filepath;
  }

  @Override
  public String getShortcut() {
    return shortcut;
  }

  @Override
  public String getImageLoc() {
    return imageLocation;
  }

  @Override
  public String getType() {
    return "Program";
  }

  @Override
  public XmlNode getNode() {
    return node;
  }

  public String getLocation() {
    return getPath().substring(0, getPath().lastIndexOf("\\"));
  }

  @Override
  public void open() {
    String temp = getPath();
    try {
      @SuppressWarnings("unused")
      Process process = new ProcessBuilder("cmd", "/c", temp).start();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  @Override
  public void remove() {
    getNode().removeNode();
    HubManager.saveXml();
  }

  @Override
  public void update() {
    getNode().getChildByName("Name").setTextContent(getName());
    getNode().getChildByName("Location").setTextContent(getPath());
    getNode().getChildByName("Shortcut").setTextContent(getShortcut());
    getNode().getChildByName("ImageLocation").setTextContent(getImageLoc());
    getNode().getChildByName("Type").setTextContent(getType());
    HubManager.saveXml();
  }

}
