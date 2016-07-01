package serviceToolKit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;

public class ReadFileInNewestVersion implements ReadFileMethod ,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String readFile(String userID, String fileName) {
		// TODO Auto-generated method stub
		String cache = findAllSameFile(userID, fileName);
		int max = 0;
		String fullFileName = "";
		for (String fullName : cache.split("/")) {
			int temp = Integer.parseInt(fullName.split("_")[2]);
			if (temp > max) {
				max = temp;
				fullFileName = fullName;
			}
		}
		return fullFileName.split("_")[2] + "_" + readFile(fullFileName);
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
}
