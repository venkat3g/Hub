package hub.runnable;

import java.io.IOException;

import hub.file.xml.XmlNode;

/**
 * Program implementation of IRunnableButton which will implement a program
 * 'Button's' information.
 * 
 * @author Venkat Garapati
 *
 */
public class Program extends Runnable {

    /**
     * Instantiates a new Program with the information of the button.
     * 
     * @param name
     *            The name of the button.
     * @param filepath
     *            The filepath of the program.
     * @param shortcut
     *            the shortcut to access the button.
     * @param imageLocation
     *            the image Location for the button.
     * @param node
     *            the node that the button is in the XML doc.
     */
    public Program(String name, String filepath, String shortcut, String imageLocation,
            XmlNode node) {
        setName(name);
        setPath(filepath);
        setShortcut(shortcut);
        setNode(node);
        setImageLoc(imageLocation);

    }

    /**
     * Instantiates a new Program with the information of the button.
     * 
     * @param name
     *            The name of the button.
     * @param filepath
     *            The filepath of the program.
     * @param shortcut
     *            the shortcut to access the button.
     * @param imageLocation
     *            the image Location for the button.
     */
    public Program(String name, String filepath, String shortcut, String imageLocation) {
        setName(name);
        setPath(filepath);
        setShortcut(shortcut);
        setNode(null);
        setImageLoc(imageLocation);

    }

    public String getLocation() {
        return getPath().substring(0, getPath().lastIndexOf("\\"));
    }

    @Override
    public void open() {
        String temp = getPath();
        try {
            @SuppressWarnings("unused")
            Process process = new ProcessBuilder("cmd", "/c", temp).start();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public String getType() {
        return "Program";
    }

}
