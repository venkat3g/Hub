package hub.file.xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlParser1 {

  private final File xmlFile;
  private Node rootNode = null;
  private final Document doc;

  /**
   * XML Parsing class. Takes a file and returns the root node of the xml file.
   * 
   * @param file
   *          file to check for root node.
   * @throws Exception
   *           exception if not able to find file.
   */
  public XmlParser1(File file) throws Exception {
    xmlFile = file;

    doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);

    rootNode = doc.getChildNodes().item(0);

  }

  public XmlParser1(File file, boolean bo) throws Exception {
    xmlFile = file;

    doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

    doc.appendChild(doc.createElement("Properties"));
    
    saveFile();

    rootNode = doc.getChildNodes().item(0);
  }

  public Node getRootNode() {
    return rootNode;
  }

  /**
   * Saves the xml file.
   * 
   * @throws TransformerException
   *           throws exception for being unable to save.
   */
  public void saveFile() throws TransformerException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(rootNode);

    StreamResult streamResult = new StreamResult(xmlFile);

    /*
     * Adds whitespace to the output file. Does not properly add whitespace to
     * first element.
     */
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
    transformer.transform(source, streamResult);
  }

  public void removeNode(XmlNode1 xmlNode1) {
    getRootNode().removeChild(xmlNode1.getNode());
    
  }

  public Element createElement(String tag) {
    
    return doc.createElement(tag);
  }
}
