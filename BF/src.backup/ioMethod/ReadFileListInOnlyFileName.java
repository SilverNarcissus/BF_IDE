package ioMethod;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * 仅用文件名读取文件的方法
 * 
 * @author SilverNarcissus
 */
public class ReadFileListInOnlyFileName implements ReadFileListMethod,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public String readFileList(String userID, String fileName) {
		// TODO Auto-generated method stub
		ArrayList<String> sArrayList = new ArrayList<String>();
		String result = "";
		File dir = new File("Files/");
		for (File file : dir.listFiles()) {
			String[] name = file.getName().split("_");
			if (name[0].equals(userID) && !sArrayList.contains(name[1])) {
				result = result + name[1] + "/";
				sArrayList.add(name[1]);
			}
		}
		return result;
	}

}
