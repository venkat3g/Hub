package dock.runnable;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.transform.TransformerException;

import dock.file.manager.HubManager;
import dock.file.xml.XmlNode;
import dock.file.xml.XmlParser;

public class Website implements IRunnableButton {

  private String name;
  private String website;
  private String shortcut;
  private String imageLocation;
  private XmlNode node;

  /**
   * Instantiates a new Website with the information of the button.
   * 
   * @param name
   *          The name of the button.
   * @param website
   *          The website of the Website.
   * @param shortcut
   *          the shortcut to access the button.
   * @param imageLocation
   *          the image Location for the button.
   * @param node
   *          the node that the button is in the XML doc.
   */
  public Website(String name, String website, String shortcut, String imageLocation,
      XmlNode node) {
    setName(name);
    setPath(website);
    setShortcut(shortcut);
    setNode(node);
    setImageLoc(imageLocation);

  }

  /**
   * Instantiates a new Website with the information of the button.
   * 
   * @param name
   *          The name of the button.
   * @param website
   *          The website of the Website.
   * @param shortcut
   *          the shortcut to access the button.
   * @param imageLocation
   *          the image Location for the button.
   */
  public Website(String name, String website, String shortcut, String imageLocation) {
    setName(name);
    setPath(website);
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
    website = path;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPath() {
    return website;
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
    return "Website";
  }

  @Override
  public XmlNode getNode() {
    return node;
  }

  @Override
  public void open() {
    String url = getPath();
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

  @Override
  public void remove() {
    getNode().removeNode();
    try {
      XmlParser.saveFile(XmlParser.getCurrentFile());
    } catch (TransformerException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
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
