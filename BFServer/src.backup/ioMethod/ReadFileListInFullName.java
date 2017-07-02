package ioMethod;

import java.io.File;
import java.io.Serializable;
/**
 * 用全名读取文件的方法
 * 
 * @author SilverNarcissus
 */
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
