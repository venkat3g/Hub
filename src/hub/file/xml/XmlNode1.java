package hub.file.xml;

import java.io.File;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Node;

public class XmlNode1 {

  private String name;
  private Node node;
  private XmlParser1 parser;

  /**
   * Constructs a XmlNode.
   * 
   */
  public XmlNode1() {
    name = "Null";
    parser = null;
  }

  /**
   * Constructs a XmlNode.
   * 
   * @param file
   *          file to construct xmlnode from.
   */
  public XmlNode1(File file) {
    try {
      parser = new XmlParser1(file);
    } catch (Exception ex) {
      parser = null;
    }
    node = parser.getRootNode();
    name = node.getNodeName();
    node.getChildNodes().getLength();
  }

  /**
   * Constructs a XmlNode.
   * 
   * @param node
   *          node to construct xmlnode from.
   * @param parser
   *          the parser to save files.
   */
  public XmlNode1(XmlParser1 parser, Node node) {
    this.parser = parser;
    this.node = node;
    name = node.getNodeName();
    node.getChildNodes().getLength();
  }

  public String getName() {
    return name;

  }

  /**
   * Gets child XML node.
   * 
   * @param item
   *          item number to return
   * @return returns the XmlNode child
   */
  public XmlNode1 getChildByIndex(int item) {

    return new XmlNode1(this.parser, node.getChildNodes().item(item * 2 + 1));
  }

  /**
   * Finds the child node by given tag.
   * 
   * @param tag
   *          The child node to look for.
   * @return returns the XmlNode that represents the child node.
   */
  public XmlNode1 getChildByName(String tag) {
    for (int i = 0; i < this.getLength() + 1; i++) {
      XmlNode1 currentNode = getChildByIndex(i);
      if (currentNode.getName().equals(tag)) {
        return currentNode;
      }
    }
    return new XmlNode1();
  }

  /**
   * Finds the child node by given tag.
   * 
   * @param tag
   *          The child node to look for.
   * @param index
   *          node to start from
   * @return returns the XmlNode that represents the child node.
   */
  public XmlNode1 getChildByName(String tag, int index) {
    for (int i = index; i < node.getChildNodes().getLength(); i++) {
      XmlNode1 currentNode = getChildByIndex(i);
      if (currentNode.getName().equals(tag)) {
        return currentNode;
      }
    }
    return new XmlNode1();
  }

  public int getLength() {

    return node.getChildNodes().getLength() / 2 - 1;
  }

  public String getTextContent() {

    return node.getTextContent();
  }

  /**
   * Sets the text content of the node.
   * 
   * @param content
   *          the content to be added.
   */
  public void setTextContent(String content) {
    node.setTextContent(content);
    try {
      parser.saveFile();
    } catch (TransformerException ex) {
      System.err.println("unable to save");
    }
  }

  public Node getNode() {
    return node;

  }
}
