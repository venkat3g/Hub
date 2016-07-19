package Test;

import java.io.*;
import java.util.ArrayList;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

@SuppressWarnings("unused")
public class FileManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 986963106583920904L;
	private String fileName;
	private String filePath;
	private String shortcut;
	private String programType;
	private NodeList node;
	public static final String PROGRAM_TYPE = "Program";
	public static final String WEBSITE_TYPE = "Website";
	private String imageLoc;

	private static String PROGRAM_FILE = "Resources/hub_info.xml";
	private static File file = new File(PROGRAM_FILE);

	private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	private static DocumentBuilder documentBuilder;
	private static Document document;

	private static ArrayList<FileManager> fileManagerList = new ArrayList<>(0);

	public static void main(String[] args) throws Exception {
		// createProgramFile();
		instantiateFile();
		System.out.println(fileManagerList);
		// for(int i = 0; i < 10; i++)
		// addProgram("" + i, " ", " ", " ", " ");
		/*
		 * for (int i = 0; i < fileManagerList.size(); i += 3) { FileManager fm
		 * = fileManagerList.get(i); fm.removeProgram(); }
		 */
		System.out.println(fileManagerList.size());

	}

	private FileManager(NodeList programNode) {
		node = programNode;

		fileName = programNode.item(1).getTextContent();

		filePath = programNode.item(3).getTextContent();

		shortcut = programNode.item(5).getTextContent();

		imageLoc = programNode.item(7).getTextContent();

		programType = programNode.item(9).getTextContent();

	}

	private FileManager(String programName, String path, String shortcut2, String image, String type, NodeList node) {
		this.node = node;

		fileName = programName;

		filePath = path;

		shortcut = shortcut2;

		imageLoc = image;

		programType = type;
	}

	public static void instantiateFile() throws Exception {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		document = documentBuilder.parse(file);

		NodeList list = document.getElementsByTagName("Program");
		fileManagerList = new ArrayList<>(0);
		System.gc();
		for (int i = 0; i < list.getLength(); i++) {
			fileManagerList.add(new FileManager(list.item(i).getChildNodes()));
		}

	}

	public static void createProgramFile() throws Exception {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		document = documentBuilder.newDocument();

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

		NodeList list = element.getChildNodes();

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

	// TODO work on removing after adding button(s)
	public void removeProgram() throws Exception {
		fileManagerList.remove(this);
		// document.getDocumentElement().removeChild(node.item(0).getParentNode());
		document.getDocumentElement().removeChild(node.item(0).getParentNode());
		updateFile();
	}

	// TODO remove extra spaces at the beginning
	private void updateFile() throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);

		StreamResult streamResult = new StreamResult(file);

		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(source, streamResult);
	}

	public String setFileName(String name) {
		node.item(1).setTextContent(name);
		try {
			updateFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName = name;
	}

	public String setFilePath(String path) {
		node.item(3).setTextContent(path);
		try {
			updateFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath = path;
	}

	public String setShortcut(String shortcut) {
		node.item(5).setTextContent(shortcut);
		try {
			updateFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.shortcut = shortcut;
	}

	public String setImageLoc(String imageLoc) {
		node.item(7).setTextContent(imageLoc);
		try {
			updateFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.imageLoc = imageLoc;
	}

	public String setProgramType(String type) {
		node.item(9).setTextContent(type);
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

	public NodeList getNode() {
		return node;
	}

	@Override
	public String toString() {
		return "Name: " + fileName + " Path: " + filePath;
	}

}
