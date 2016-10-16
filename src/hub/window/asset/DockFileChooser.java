package hub.window.asset;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Creates a File Chooser used by hub classes.
 * 
 * @author Venkat Garapati
 *
 */
public class DockFileChooser {

  /**
   * Creates a File Chooser.
   * 
   * @param desc
   *          Textual description of the filter
   * @param ext
   *          Various filters
   * @return Returns a JFileChooser
   */
  public static JFileChooser makeFileChooser(String desc, String[] ext) {
    JFileChooser jfc = new JFileChooser();

    jfc.setCurrentDirectory(new File("C:/"));
    FileNameExtensionFilter filter = new FileNameExtensionFilter(desc, ext);

    jfc.setFileFilter(filter);
    System.gc();
    return jfc;
  }

}
