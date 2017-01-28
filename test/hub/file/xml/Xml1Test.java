package hub.file.xml;

import static org.junit.Assert.assertEquals;
import java.io.File;

import org.junit.Test;

public class Xml1Test {

  @Test
  public void testParser() throws Exception {
    XmlParser1 parser = new XmlParser1(new File("Resources/buttonList.xml"));

    assertEquals("ButtonList", parser.getRootNode().getNodeName());
    assertEquals("Button", parser.getRootNode().getChildNodes().item(1).getNodeName());
  }

  @Test
  public void testNode() {

    XmlNode1 root = new XmlNode1(new File("Resources/buttonList.xml"));

    // Checks that the root is the root.
    assertEquals("ButtonList", root.getName());

    assertEquals("v", root.getChildByIndex(0).getChildByName("Name").getTextContent());
    assertEquals("Button", root.getChildByName("Button", 1).getName());

  }

  @Test
  public void testNodeGetChildByName() {

    XmlNode1 root = new XmlNode1(new File("Resources/buttonList.xml"));

    // use get childbyindex with getchildbyname.
    assertEquals("Name", root.getChildByIndex(0).getChildByName("Name").getName());
    // Tests a node found in the childlist with name
    assertEquals("Button", root.getChildByName("Button", 1).getName());
    // Tests a node not found in the child list
    assertEquals("Null", root.getChildByName("Name").getName());

  }

  @Test
  public void testChangesWithSetText() {
    XmlNode1 root = new XmlNode1(new File("Resources/buttonList.xml"));

    XmlNode1 node1 = root.getChildByName("Button");

    node1 = node1.getChildByName("Name");

    node1.setTextContent("v");

    assertEquals("v", node1.getTextContent());

  }

}
