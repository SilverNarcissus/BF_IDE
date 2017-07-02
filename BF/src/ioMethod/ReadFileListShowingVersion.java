package ioMethod;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import ioMethodHelper.Time;
/**
 * 读取文件版本列表的方法
 * 
 * @author SilverNarcissus
 */
public class ReadFileListShowingVersion implements ReadFileListMethod,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public String readFileList(String userID, String fileName) {
		// TODO Auto-generated method stub
		String result = "";
		ArrayList<Time> times = new ArrayList<Time>();
		String row = findAllSameFile(userID, fileName);
		if (row.length() < 2) {
			return "";
		}
		for (String cache : row.split("/")) {
			times.add(new Time(cache.split("_")[2] + "~" + cache.split("_")[3]));
		}
		Collections.sort(times);
		for (Time time : times) {
			System.out.println(time);
			result = result + time.toString() + "/";
		}
		return result;
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
}
