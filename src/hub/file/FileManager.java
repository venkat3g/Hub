package hub.file;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FileManager implements Serializable {

  /*
   * TODO: Revise class structure, currently is a mess.
   */
  
  /*
   * Instance fields
   */
  private String buttonName;
  private String buttonPath;
  private String shortcut;
  private String buttonType;
  private Node buttonNode;
  private String buttonImageLoc;

  /*
   * Class fields
   */
  private static final long serialVersionUID = -2292930000016794120L;
  public static final String PROGRAM_TYPE = "Program";
  public static final String WEBSITE_TYPE = "Website";
  private static String PROGRAM_FILE = "Resources/hub_info.xml";
  private static File file = new File(PROGRAM_FILE);
  private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
      .newInstance();
  private static DocumentBuilder documentBuilder;
  private static Document document;

  private static ArrayList<FileManager> fileManagerList = new ArrayList<>(0);

  static {
    if (file.exists()) {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
          .newInstance();
      try {
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        document = documentBuilder.parse(file);
      } catch (ParserConfigurationException ex) {
        ex.printStackTrace();
      } catch (SAXException ex) {
        ex.printStackTrace();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    } else {
      try {
        file.createNewFile();
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        document = documentBuilder.newDocument();
        createProgramFile();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

  }

  private FileManager(NodeList programNode) {

    buttonNode = programNode.item(0).getParentNode();

    Element eNode = (Element) buttonNode;

    buttonName = eNode.getElementsByTagName("Name").item(0).getTextContent();

    buttonPath = eNode.getElementsByTagName("Location").item(0).getTextContent();

    shortcut = eNode.getElementsByTagName("Shortcut").item(0).getTextContent();

    buttonImageLoc = eNode.getElementsByTagName("ImageLocation").item(0).getTextContent();

    buttonType = eNode.getElementsByTagName("Type").item(0).getTextContent();

  }

  private FileManager(String programName, String path, String shortcut2, String image,
      String type, Node node) {

    this.buttonNode = node;

    buttonName = programName;

    buttonPath = path;

    shortcut = shortcut2;

    buttonImageLoc = image;

    buttonType = type;
  }

  /**
   * Method instantiates files for Hub use.
   * 
   * @throws XPathExpressionException
   *           caused by not finding document
   */
  public static void instantiateFile() throws XPathExpressionException {

    // Used to get rid of white space created from removing 'Programs'
    XPath xp = XPathFactory.newInstance().newXPath();
    NodeList nl = (NodeList) xp.evaluate("//text()[normalize-space(.)='']", document,
        XPathConstants.NODESET);
    for (int i = 0; i < nl.getLength(); ++i) {
      Node node = nl.item(i);
      node.getParentNode().removeChild(node);
    }

    // Gathers a list of nodes from xml file and creates a
    // ArrayList<FileManager> to be used
    NodeList list = document.getElementsByTagName("Program");
    fileManagerList = new ArrayList<>(0);
    for (int i = 0; i < list.getLength(); i++) {
      fileManagerList.add(new FileManager(list.item(i).getChildNodes()));
    }
    System.gc();
  }

  /**
   * Creates the files that Hub uses if damaged or not found.
   * 
   * @throws Exception
   *           caused by not finding the XML file used for Hub's ProgramList
   */
  public static void createProgramFile() throws Exception {

    Element element = document.createElement("ProgramList");
    document.appendChild(element);

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(document);

    StreamResult streamResult = new StreamResult(file);

    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.transform(source, streamResult);

  }

  /**
   * Adds a program to the XML file.
   * 
   * @param programName
   *          The name of the program to be added.
   * @param path
   *          the location of the program being added.
   * @param programShortcut
   *          Keyboard shortcut that will respond when Hub is open.
   * @param imageLoc
   *          The image location for the image associated with this program.
   * @param programType
   *          Identifies the programType
   * @return Returns an ArrayList of the updated FileManagerList.
   * @throws TransformerException
   *           Exception thrown if the method is unable to save the elements to
   *           the XML file.
   */
  public static ArrayList<FileManager> addProgram(String programName, String path,
      String programShortcut, String imageLoc, String programType)
      throws TransformerException {

    Element element = document.createElement("Program");

    Element name = document.createElement("Name");
    name.appendChild(document.createTextNode(programName));
    element.appendChild(name);

    Element location = document.createElement("Location");
    location.appendChild(document.createTextNode(path));
    element.appendChild(location);

    Element shortcut = document.createElement("Shortcut");
    shortcut.appendChild(document.createTextNode(programShortcut));
    element.appendChild(shortcut);

    Element image = document.createElement("ImageLocation");
    image.appendChild(document.createTextNode(imageLoc));
    element.appendChild(image);

    Element type = document.createElement("Type");
    type.appendChild(document.createTextNode(programType));
    element.appendChild(type);

    document.getDocumentElement().appendChild(element);

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(document);

    StreamResult streamResult = new StreamResult(file);

    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.transform(source, streamResult);

    Node list = element.getParentNode();

    fileManagerList.add(
        new FileManager(programName, path, programShortcut, imageLoc, programType, list));
    return fileManagerList;
  }

  public static int getProgramNum() {
    return fileManagerList.size();
  }

  public static ArrayList<FileManager> getFileManagerList() {

    return fileManagerList;
  }

  public static File getFile() {
    return file;
  }

  /**
   * This method removes this, FileManager, from FileManagerList
   * 
   * @throws Exception
   *           Throws exception with issue removing elements from document.
   */
  public void removeProgram() throws Exception {
    fileManagerList.remove(this);
    document.getElementsByTagName("Program").item(0).getParentNode()
        .removeChild(buttonNode);
    updateFile();
  }

  /**
   * Updates the XML file with changes this call is internal to FileManager and
   * will be called when there are changes made to the ProgramList.
   * 
   * @throws Exception
   *           Throws an exception if unable to write to document.
   */
  private void updateFile() throws Exception {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(document);

    StreamResult streamResult = new StreamResult(file);

    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.transform(source, streamResult);
  }

  /**
   * Sets the Program name of the
   * 
   * @param name
   *          The name that will replace buttonName.
   * @return Returns the new Button name.
   */
  public String setProgramName(String name) {
    ((Element) buttonNode).getElementsByTagName("Name").item(0).setTextContent(name);
    try {
      updateFile();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return buttonName = name;
  }

  /**
   * Sets the Program name of the
   * 
   * @param path
   *          The path that will replace buttonPath.
   * @return Returns the new Button name.
   */
  public String setFilePath(String path) {
    ((Element) buttonNode).getElementsByTagName("Location").item(0).setTextContent(path);
    try {
      updateFile();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return buttonPath = path;
  }

  /**
   * Sets the Program name of the
   * 
   * @param shortcut
   *          The new shortcut that will replace shortcut.
   * @return Returns shortcut.
   */
  public String setShortcut(String shortcut) {
    ((Element) buttonNode).getElementsByTagName("Shortcut").item(0)
        .setTextContent(shortcut);
    try {
      updateFile();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return this.shortcut = shortcut;
  }

  /**
   * Sets the Program name of the
   * 
   * @param imageLoc
   *          The image location that will replace buttonImageLocation.
   * @return Returns imageLoc.
   */
  public String setImageLoc(String imageLoc) {
    ((Element) buttonNode).getElementsByTagName("ImageLocation").item(0)
        .setTextContent(imageLoc);
    try {
      updateFile();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return this.buttonImageLoc = imageLoc;
  }

  /**
   * Sets the button's type.
   * 
   * @param type
   *          The type that will replace buttonType.
   * @return Returns the new Button name.
   */
  public String setProgramType(String type) {
    ((Element) buttonNode).getElementsByTagName("Type").item(0).setTextContent(type);
    try {
      updateFile();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return this.buttonType = type;
  }

  public String getFilePath() {
    return buttonPath;
  }

  public String getFileName() {
    return buttonName;
  }

  public String getShortcut() {
    return shortcut;
  }

  public String getImageLoc() {
    return buttonImageLoc;
  }

  public String getProgramType() {
    return buttonType;
  }

  public Node getNode() {
    return buttonNode;
  }

  /**
   * Allows the button to open the button's path.
   */
  public void open() {

    if (getProgramType().equals(FileManager.PROGRAM_TYPE)) {
      String temp = getFilePath();
      try {
        @SuppressWarnings("unused")
        Process process = new ProcessBuilder("cmd", "/c", temp).start();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    if (getProgramType().equals(FileManager.WEBSITE_TYPE)) {
      String url = getFilePath();
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

  }

  /**
   * This method display's the basic information of a button.
   */
  public String toString() {
    return "Name: " + buttonName + " Path: " + buttonPath;
  }

}
