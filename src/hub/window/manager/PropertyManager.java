package hub.window.manager;

import java.io.File;

import hub.file.xml.XmlNode1;

public class PropertyManager {

  private static String name;
  private static int port;
  private static String closeop;
  private static String imgicon;
  private static XmlNode1 root;

  /**
   * Loads the properties.
   */
  public static void load() {

    root = new XmlNode1(new File("Resources/button_properties.xml"));

    port = root.getChildByName("Port") != null
        ? Integer.parseInt(root.getChildByName("Port").getTextContent()) : 1024;

    name = root.getChildByName("Name") != null
        ? root.getChildByName("Name").getTextContent() : "";

    closeop = root.getChildByName("CloseOp") != null
        ? root.getChildByName("CloseOp").getTextContent() : "";

    imgicon = root.getChildByName("IconExt") != null
        ? root.getChildByName("IconImage").getTextContent() : "";

  }

  public static void setName(String name) {
    root.getChildByName("Name").setTextContent(name);
  }

  public static void setPort(int port) {
    root.getChildByName("Port").setTextContent("" + port);
  }

  public static void setCloseOp(String closeOp) {
    root.getChildByName("CloseOp").setTextContent(closeOp);

  }

  public static void setIcon(String icon) {
    root.getChildByName("IconImage").setTextContent(icon);
  }

  public static String getName() {
    return name;

  }

  public static String getCloseOp() {
    return closeop;

  }

  public static String getIconImage() {
    return imgicon;

  }

  public static int getPort() {
    return port;

  }

}
