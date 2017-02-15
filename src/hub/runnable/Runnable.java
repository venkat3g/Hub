package hub.runnable;

import hub.file.manager.HubManager;
import hub.file.xml.XmlNode;

/**
 * Abstract class for Runnable Buttons provides properties which will need to be
 * set by specific buttons. Specific buttons will need to provide type and open
 * functionality.
 * 
 * @author Venkat Garapati
 *
 */
public abstract class Runnable implements IRunnableButton {

    protected String name;
    protected String path;
    protected String shortcut;
    protected String imageLocation;
    protected XmlNode node;

    public Runnable() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setImageLoc(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    @Override
    public void setName(String name) {
        this.name = name;

    }

    @Override
    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    @Override
    public void setNode(XmlNode node) {
        this.node = node;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getShortcut() {
        return shortcut;
    }

    @Override
    public String getImageLoc() {
        return imageLocation;
    }

    @Override
    public XmlNode getNode() {
        return node;
    }

    @Override
    public void remove() {
        getNode().removeNode();
        HubManager.saveXml();
    }

    @Override
    public void update() {
        getNode().getChildByName("Name").setTextContent(getName());
        getNode().getChildByName("Location").setTextContent(getPath());
        getNode().getChildByName("Shortcut").setTextContent(getShortcut());
        getNode().getChildByName("ImageLocation").setTextContent(getImageLoc());
        getNode().getChildByName("Type").setTextContent(getType());
        HubManager.saveXml();
    }

}
