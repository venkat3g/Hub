package Test;

import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class FileManager implements Serializable {

	// Instance fields
	private String fileName;
	private String filePath;
	private String shortcut;
	private String programType;
	private Node node;
	private String imageLoc;

	// Class fields
	private static final long serialVersionUID = -2292930000016794120L;
	public static final String PROGRAM_TYPE = "Program";
	public static final String WEBSITE_TYPE = "Website";
	private static String PROGRAM_FILE = "Resources/hub_info.xml";
	private static File file = new File(PROGRAM_FILE);
	private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	private static DocumentBuilder documentBuilder;
	private static Document document;

	private static ArrayList<FileManager> fileManagerList = new ArrayList<>(0);

	static {
		if (file.exists()) {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			try {
				documentBuilder = documentBuilderFactory.newDocumentBuilder();
				document = documentBuilder.parse(file);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				file.createNewFile();
				documentBuilder = documentBuilderFactory.newDocumentBuilder();
				document = documentBuilder.newDocument();
				createProgramFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private FileManager(NodeList programNode) {

		node = programNode.item(0).getParentNode();

		Element eNode = (Element) node;

		fileName = eNode.getElementsByTagName("Name").item(0).getTextContent();

		filePath = eNode.getElementsByTagName("Location").item(0).getTextContent();

		shortcut = eNode.getElementsByTagName("Shortcut").item(0).getTextContent();

		imageLoc = eNode.getElementsByTagName("ImageLocation").item(0).getTextContent();

		programType = eNode.getElementsByTagName("Type").item(0).getTextContent();

	}

	private FileManager(String programName, String path, String shortcut2, String image, String type, Node node) {

		this.node = node;

		fileName = programName;

		filePath = path;

		shortcut = shortcut2;

		imageLoc = image;

		programType = type;
	}

	// Used to instantiate/read from xml file
	public static void instantiateFile() throws Exception {

		// Used to get rid of white space created from removing 'Programs'
		XPath xp = XPathFactory.newInstance().newXPath();
		NodeList nl = (NodeList) xp.evaluate("//text()[normalize-space(.)='']", document, XPathConstants.NODESET);
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

	public static ArrayList<FileManager> addProgram(String programName, String path, String programShortcut,
			String imageLoc, String programType) throws TransformerException {

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

		fileManagerList.add(new FileManager(programName, path, programShortcut, imageLoc, programType, list));
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

	public void removeProgram() throws Exception {
		fileManagerList.remove(this);
		document.getElementsByTagName("Program").item(0).getParentNode().removeChild(node);
		updateFile();
	}

	private void updateFile() throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);

		StreamResult streamResult = new StreamResult(file);

		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(source, streamResult);
	}

	public String setFileName(String name) {
		((Element) node).getElementsByTagName("Name").item(0).setTextContent(name);
		try {
			updateFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName = name;
	}

	public String setFilePath(String path) {
		((Element) node).getElementsByTagName("Location").item(0).setTextContent(path);
		try {
			updateFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath = path;
	}

	public String setShortcut(String shortcut) {
		((Element) node).getElementsByTagName("Shortcut").item(0).setTextContent(shortcut);
		try {
			updateFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.shortcut = shortcut;
	}

	public String setImageLoc(String imageLoc) {
		((Element) node).getElementsByTagName("ImageLocation").item(0).setTextContent(imageLoc);
		try {
			updateFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.imageLoc = imageLoc;
	}

	public String setProgramType(String type) {
		((Element) node).getElementsByTagName("Type").item(0).setTextContent(type);
		try {
			updateFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.programType = type;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public String getShortcut() {
		return shortcut;
	}

	public String getImageLoc() {
		return imageLoc;
	}

	public String getProgramType() {
		return programType;
	}

	public Node getNode() {
		return node;
	}

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

	@Override
	public String toString() {
		return "Name: " + fileName + " Path: " + filePath;
	}

}
