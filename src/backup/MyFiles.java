package backup;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFiles {
	private String path;
	private String name;
	private String pathLine;
	private String fileType;
	final static String PROGRAM = "Program";
	final static String WEBSITE = "Website";
	final static String SHORTCUTS = "Shortcuts";

	public MyFiles(String pathName, String fileType) {
		this.fileType = fileType;
		if (fileType.equals(PROGRAM))
			instantiateToProgram(pathName);
		else if (fileType.equals(WEBSITE))
			instantiateToWeb(pathName);
		else if(fileType.equals(SHORTCUTS))
			instantiateToShortcut(pathName);
	}

	private void instantiateToShortcut(String pathName) {
		path = pathName.substring(0, pathName.indexOf(" , "));
		name = pathName.substring(pathName.indexOf("Name:") + 6);
		
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
		if(fileType == PROGRAM)
			return path.substring(path.indexOf(":") + 1);
		else if(fileType == WEBSITE){
			return path;
		}
		return "Not a valid File Type";
	}

	public String getName() {
		return name;
	}

	public String getPathLine() {
		return pathLine;
	}
	public String getFileType(){
		return fileType;
	}

	public void addToFile(File sourcesFile) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(sourcesFile, true));
			pw.println(pathLine);
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void removeFromFile(File sourcesFile) throws IOException {
		BufferedReader io = new BufferedReader(new FileReader(sourcesFile));
		String line;
		String newFile = "";
		while ((line = io.readLine()) != null) {
			if (!(pathLine.equals(line)))
				newFile += line + "\n";
		}
		FileWriter pw = new FileWriter(sourcesFile);
		pw.write(newFile);
		pw.close();
		io.close();

	}

	public static void main(String[] args) throws IOException {
		MyFiles web = new MyFiles("speedtest.net , Name: SpeedTest", MyFiles.WEBSITE);
	
		System.out.println(web.getName() + "\n" + web.getPath() + "\n" + web.getPathLine());
	}

}
