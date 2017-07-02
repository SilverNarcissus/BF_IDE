package ioMethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
/**
 * 利用用户名和文件名读文件的方法
 * 
 * @author SilverNarcissus
 */
public class ReadFileWithUserIDAndFileName implements ReadFileMethod,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public String readFile(String userID, String fileName) {
		// TODO Auto-generated method stub
		File f = new File("Files/" + userID + "_" + fileName);
		String result = new String();
		String cache;
		try {
			FileReader fileReader = new FileReader(f);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((cache = bufferedReader.readLine()) != null) {
				result = result.concat(cache);
			}
			bufferedReader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println(result);
		return result;
	}

}
