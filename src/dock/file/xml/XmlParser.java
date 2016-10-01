package dock.file.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XmlParser {
  private static Document xmlFile;
  private static XmlNode root;
  private static File xfile;

  /**
   * Loads the File into the static document.
   * 
   * @param file
   *          The file that contains XML.
   * 
   */
  public static void loadFile(File file) {
    xfile = file;
    if (file.length() == 0) {
      try {

        createProgramFile(file, "ButtonList");
        saveFile(file);
      } catch (TransformerException ex) {
        // TODO Auto-generated catch block
        ex.printStackTrace();
      }
    } else {
      try {
        /*
         * Creates a document builder that will parse the xml file.
         */
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder();
        /*
         * Parse the file to hte xmlFile document.
         */
        xmlFile = docBuilder.parse(file);
        /*
         * Creates the root XmlNode.
         */
        root = new XmlNode(xmlFile.getFirstChild());

      } catch (ParserConfigurationException ex) {
        ex.printStackTrace();
      } catch (SAXException ex) {
        // TODO Auto-generated catch block
        ex.printStackTrace();
      } catch (IOException ex) {
        // TODO Auto-generated catch block
        ex.printStackTrace();
      }
    }
  }

  /**
   * Saves the xml document.
   * 
   * @param file
   *          the file to be saved to.
   * @throws TransformerException
   *           any exceptions found saving.
   */
  public static void saveFile(File file) throws TransformerException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(xmlFile);

    StreamResult streamResult = new StreamResult(file);

    /*
     * Adds whitespace to the output file. Does not properly add whitespace to
     * first element.
     */
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
    transformer.transform(source, streamResult);
  }

  static void changeContent(String content, XmlNode programNode, String tag) {

  }

  /**
   * Creates the files that Hub uses if damaged or not found.
   * 
   * @throws TransformerException
   *           caused by not finding the XML file used for Hub's ProgramList
   */
  private static void createProgramFile(File file, String rootNode)
      throws TransformerException {
    /*
     * Creates a new document.
     */
    try {
      xmlFile = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    } catch (ParserConfigurationException ex) {
      System.err.println("Error creating new document.");
      ex.printStackTrace();
    }
    /*
     * Creates first Node ButtonList.
     */
    Element element = xmlFile.createElement(rootNode);
    /*
     * Appends the root element to the document.
     */
    xmlFile.appendChild(element);

    DOMSource source = new DOMSource(xmlFile);

    StreamResult streamResult = new StreamResult(file);

    /*
     * Creates a TransformerFactory and Transformer to set proper output for XML
     * file.
     */
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();

    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.transform(source, streamResult);

  }

  static void addChild(Element button) {

    xmlFile.getDocumentElement().appendChild(button);

  }

  static Element createElement(String tag) {
    return xmlFile.createElement(tag);
  }

  /**
   * Removes the node from the xmlFile.
   * 
   * @param node
   *          node to be removed.
   */
  static void removeNode(XmlNode node) {

    root.getNode().removeChild(node.getNode());
  }

  /**
   * Finds the root node of the xmlFile stored.
   */
  static XmlNode getRootNode() {
    assert xmlFile == null : "xmlFile not initialized";

    return root;

  }

  public static File getCurrentFile() {
    return xfile;
  }

}