package hub.runnable;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import hub.file.xml.XmlNode;

/**
 * Website implementation of IRunnableButton which will implement a website
 * 'Button's' information.
 * 
 * @author Venkat Garapati
 *
 */
public class Website extends Runnable {

    /**
     * Instantiates a new Website with the information of the button.
     * 
     * @param name
     *            The name of the button.
     * @param website
     *            The website of the Website.
     * @param shortcut
     *            the shortcut to access the button.
     * @param imageLocation
     *            the image Location for the button.
     * @param node
     *            the node that the button is in the XML doc.
     */
    public Website(String name, String website, String shortcut, String imageLocation,
            XmlNode node) {
        setName(name);
        setPath(website);
        setShortcut(shortcut);
        setNode(node);
        setImageLoc(imageLocation);

    }

    /**
     * Instantiates a new Website with the information of the button.
     * 
     * @param name
     *            The name of the button.
     * @param website
     *            The website of the Website.
     * @param shortcut
     *            the shortcut to access the button.
     * @param imageLocation
     *            the image Location for the button.
     */
    public Website(String name, String website, String shortcut, String imageLocation) {
        setName(name);
        setPath(website);
        setShortcut(shortcut);
        setNode(null);
        setImageLoc(imageLocation);

    }

    @Override
    public void open() {
        String url = getPath();
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

    @Override
    public String getType() {
        return "Website";
    }

}
