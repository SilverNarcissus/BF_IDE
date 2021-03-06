package serviceToolKit;

import java.io.File;
import java.io.Serializable;

public class ReadFileListInFullName implements ReadFileListMethod,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public String readFileList(String userID,String fileName) {
		// TODO Auto-generated method stub
		String result = "";
		File dir = new File("Files/");
		for (File file : dir.listFiles()) {
			String[] name = file.getName().split("_");
			if (name[0].equals(userID)) {
				result = result + file.getName() + "/";
			}
		}
		return result;
	}
}
