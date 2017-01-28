package hub.window.manager;

import java.io.File;

import hub.file.xml.XmlNode1;

public class PropertyManager {

  

  private static String name;
  private static int port;
  private static String closeop;
  private static String imgicon;
  private static XmlNode1 root;

  
  static {
    File file = new File("Resources/hub_properties.xml");
    if (!file.exists()) {
      try {
        name = "";
        port = 1024;
        closeop = "True";
        imgicon = "";
        root = new XmlNode1(file,false);
        root.appendChild(root.createElement("Name"));
        root.appendChild(root.createElement("Port"));
        root.appendChild(root.createElement("CloseOp"));
        root.appendChild(root.createElement("IconImage"));
        root = new XmlNode1(file);
        setPort(1024);
        setCloseOp("True");
        
        
      } catch (Exception ex) {
        System.err.println("Not enough sufficent permissions to write resource.");
      }
    } else {
      load();
    }
  }
  
  /**
   * Loads the properties.
   */
  private static void load() {

    root = new XmlNode1(new File("Resources/hub_properties.xml"));

    port = root.getChildByName("Port") != null
        ? Integer.parseInt(root.getChildByName("Port").getTextContent()) : 1024;

    name = root.getChildByName("Name") != null
        ? root.getChildByName("Name").getTextContent() : "";

    closeop = root.getChildByName("CloseOp") != null
        ? root.getChildByName("CloseOp").getTextContent() : "";

    imgicon = root.getChildByName("IconImage") != null
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
