package ioMethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
/**
 * 写入最新版本的文件的方法的类
 * 
 * @author SilverNarcissus
 */
public class WriteFileInNewestVersion implements WriteFileMethod,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public boolean writeFile(String content, String userID,String fileName) {
		// TODO Auto-generated method stub
		int max = 0;
		String maxFullName = "";
		int min = Integer.MAX_VALUE;
		String minFullName = "";
		String row = findAllSameFile(userID, fileName);
		// 找出最前和最后的文件
		for (String fullName : row.split("/")) {
			int tag = Integer.parseInt(fullName.split("_")[2]);
			if (tag > max) {
				max = tag;
				maxFullName = fullName;
			}
			if (tag < min) {
				min = tag;
				minFullName = fullName;
			}
		}
		//
		// 检测是否相同
		if (content.equals(readFile(maxFullName))) {
			return false;
		}
		//
		Calendar calendar = Calendar.getInstance();
		String name = userID + "_" + fileName + "_" + String.valueOf(max + 1) + "_"
				+ String.valueOf(calendar.get(Calendar.YEAR)) + "-" + String.valueOf(calendar.get(Calendar.MONTH) + 1)
				+ "-" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "~"
				+ String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + "-"
				+ String.valueOf(calendar.get(Calendar.MINUTE)) + "-" + String.valueOf(calendar.get(Calendar.SECOND));
		// 删除最前面的文件
		if (max >= 10) {
			File deleteFile = new File("Files/" + minFullName);
			deleteFile.delete();
		}
		System.out.println(name);
		writeContentToFile(content, name.split("_")[0],name.split("_")[1]+"_"+name.split("_")[2]+"_"+name.split("_")[3]);
		return true;
	}
	private String findAllSameFile(String userId, String fileName) {
		String result = "";
		for (String cache : readFileList(userId).split("/")) {
			if (cache.split("_")[1].equals(fileName)) {
				result = result + cache + "/";
			}
		}
		return result;
	}
	private String readFileList(String userId) {
		String result = "";
		File dir = new File("Files/");
		for (File file : dir.listFiles()) {
			String[] name = file.getName().split("_");
			if (name[0].equals(userId)) {
				result = result + file.getName() + "/";
			}
		}
		return result;
	}
	private String readFile(String fileName) {
		File f = new File("Files/" + fileName);
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
		return result;
	}
	public boolean writeContentToFile(String content,String userID,String fileName) {
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





