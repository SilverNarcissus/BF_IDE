package ioMethod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
/**
 * 利用全文件名写入文件的方法类
 * 
 * @author SilverNarcissus
 */
public class WriteFileWithIDAndFileName implements WriteFileMethod ,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public boolean writeFile(String content,String userID,String fileName) {
		// TODO Auto-generated method stub
		File f = new File("Files/" + userID + "_" + fileName);
		try {
			FileWriter fw = new FileWriter(f, false);
			//
			fw.write(content);
			fw.flush();
			fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
