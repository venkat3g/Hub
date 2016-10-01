package dock.file.xml;

import java.io.File;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlNode {

  private Node node;

  /**
   * Creates a XmlNode using org.w3c.dom.Nodes.
   * 
   * @param node
   *          reference to node contained in the file.
   */
  public XmlNode(Node node) {
    setNode(node);
    // System.out.println(node);

  }

  /**
   * Creates a root Xml Node.
   * 
   * @param xmlFile
   *          The file that will be parsed for a XmlNode.
   */
  public static XmlNode getXmlRootNode(File xmlFile) {

    XmlParser.loadFile(xmlFile);
    return XmlParser.getRootNode();
  }

  public String getAttributeValue(String attribute) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Returns the actual number of children nodes.
   */
  public int getNumberChildren() {
    return getNode().getChildNodes().getLength() / 2;
  }

  /**
   * Gets the child number sent in.
   * 
   * @param child
   *          child to return with standard numbering scheme 0,1,2...
   * @return returns XmlNode representation of the child node.
   */
  public XmlNode getChild(int child) {
    if (child == 0) {
      return new XmlNode(getNode().getChildNodes().item(1));
    }
    child *= 2;
    return new XmlNode(getNode().getChildNodes().item(++child));
  }

  /**
   * Finds the child node by given tag.
   * 
   * @param tag
   *          The child node to look for.
   * @return returns the XmlNode that represents the child node.
   */
  public XmlNode getChildByName(String tag) {
    for (int i = 0; i < getNumberChildren(); i++) {
      XmlNode currentNode = getChild(i);
      if (currentNode.getNode().getNodeName().equals(tag)) {
        return currentNode;
      }
    }
    return null;
  }

  /**
   * Adds the child to the current node.
   * 
   * @param child
   *          XmlNode - child node
   */
  public void appendChild(XmlNode child) {
    getNode().appendChild(child.getNode());

  }

  public Element createElement(String tag) {
    return XmlParser.createElement(tag);

  }

  public void removeNode() {
    XmlParser.removeNode(this);
  }

  Node getNode() {
    return node;
  }

  void setNode(Node node) {
    this.node = node;
  }

  public String toString() {
    return node.toString();

  }

  /**
   * Main method.
   * 
   * @param args
   *          commandline
   */
  public static void main(String... args) {

  }

  public String getTextContent() {
    return getNode().getTextContent();
  }

  /**
   * Writes new text content.
   * 
   * @param content
   *          the new content to be written.
   */
  public void setTextContent(String content) {
    getNode().setTextContent(content);
  }

}
