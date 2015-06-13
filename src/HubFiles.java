import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class HubFiles {
	private String path;
	private String name;
	private String pathLine;
	private String fileType;
	final static String PROGRAM = "Program";
	final static String WEBSITE = "Website";
	final static String SHORTCUTS = "Shortcuts";

	public HubFiles(String pathName, String fileType) {
		this.fileType = fileType;
		if (fileType.equals(PROGRAM))
			instantiateToProgram(pathName);
		else if (fileType.equals(WEBSITE))
			instantiateToWeb(pathName);
		else if (fileType.equals(SHORTCUTS))
			instantiateToShortcut(pathName);
	}

	private void instantiateToShortcut(String pathName) {
		path = pathName.substring(pathName.indexOf("Shortcut:"),
				pathName.indexOf(" , "));
		name = pathName.substring(pathName.indexOf("Name:") + 6);
		pathLine = pathName;

	}

	private void instantiateToWeb(String pathName) {
		path = pathName.substring(0, pathName.indexOf(" , "));
		name = pathName.substring(pathName.indexOf("Name:") + 6);
		if (path.indexOf("http://") != -1 || path.indexOf("https://") != -1)
			pathLine = pathName;
		else {
			path = "http://" + path;
			pathLine = path + " , Name: " + name;
		}

	}

	private void instantiateToProgram(String pathName) {
		path = pathName.substring(0, pathName.indexOf(" , "));
		name = pathName.substring(pathName.indexOf("Name:") + 6);
		pathLine = pathName;
	}

	public String getPath() {
		if (fileType == PROGRAM)
			return path.substring(path.indexOf(":") + 1);
		else if (fileType == WEBSITE) {
			return path;
		} else if (fileType == SHORTCUTS) {
			return path.substring(path.lastIndexOf(':') + 1);
		}
		return "Not a valid File Type";
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return pathLine;
	}

	public String getFileType() {
		return fileType;
	}

	public void addToFile(File sourceFile) {
		try {
			PrintWriter lineAdder = new PrintWriter(new FileWriter(sourceFile,
					true));
			lineAdder.println(pathLine);
			lineAdder.close();
		} catch (FileNotFoundException e) {
			try {
				sourceFile.createNewFile();
			} catch (IOException e1) {

				e1.printStackTrace();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void removeFromFile(File sourceFile, HubButton HubButton)
			throws IOException {
		Scanner fileScan = new Scanner(sourceFile);
		String line;

		StringBuilder newFile = new StringBuilder("");
		while (fileScan.hasNextLine()) {
			line = fileScan.nextLine();
			newFile.append(line + "\n");
		}
		FileWriter pw = new FileWriter(sourceFile);
		pw.write(newFile.substring(0,
				newFile.lastIndexOf(HubButton.getProfile().toString())));
		pw.close();
		fileScan.close();
		line = null;
		fileScan = null;
		System.gc();
	}
	
	public void changeName(String newName) {
		// Changes Name in Image, Shortcut, and Source Files
		
		
		Scanner imageScan;
		Scanner shortcutScan;
		Scanner sourceScan;

		try {
			StringBuilder tmpContents = new StringBuilder();
			sourceScan = new Scanner(MainWindow.sourceFile);
			while(sourceScan.hasNextLine()){
				String line = sourceScan.nextLine();
				if(line.equals(toString())){
					tmpContents.append(line.substring(0, line.lastIndexOf(name)) + newName + "\n");
				}
				else{
					tmpContents.append(line + "\n");
				}
			}
			FileWriter sourceFileWriter = new FileWriter(MainWindow.sourceFile);
			
			sourceFileWriter.write(tmpContents.toString());
			sourceFileWriter.close();
			
			System.out.println(tmpContents.toString());
			
			

		} catch (NullPointerException | IOException e) {
			System.out
					.println("File not found or window has not instantiated  "
							+ e.getCause());
		}
		
		
		try {
			StringBuilder tmpContents = new StringBuilder();
			imageScan = new Scanner(MainWindow.imageFile);
			while(imageScan.hasNextLine()){
				String line = imageScan.nextLine();
				if(line.endsWith(name)){
					tmpContents.append(line.substring(0, line.lastIndexOf(name)) + newName + "\n");
				}
				else{
					tmpContents.append(line + "\n");
				}
			}
			FileWriter sourceFileWriter = new FileWriter(MainWindow.imageFile);
			
			sourceFileWriter.write(tmpContents.toString());
			sourceFileWriter.close();
			
			System.out.println(tmpContents.toString());
			
			
			

		} catch (NullPointerException | IOException e) {
			System.out
					.println("File not found or window has not instantiated  "
							+ e.getCause());
		}
		try {
			StringBuilder tmpContents = new StringBuilder();
			shortcutScan = new Scanner(MainWindow.shortcutFile);
			while(shortcutScan.hasNextLine()){
				String line = shortcutScan.nextLine();
				if(line.endsWith(name)){
					tmpContents.append(line.substring(0, line.lastIndexOf(name)) + newName + "\n");
				}
				else{
					tmpContents.append(line + "\n");
				}
			}
			FileWriter sourceFileWriter = new FileWriter(MainWindow.shortcutFile);
			
			sourceFileWriter.write(tmpContents.toString());
			sourceFileWriter.close();
			
			System.out.println(tmpContents.toString() + " here");
			
			

		} catch (NullPointerException | IOException e) {
			System.out
					.println("File not found or window has not instantiated  "
							+ e.getCause());
		}
		
		name = newName;
	}

}
