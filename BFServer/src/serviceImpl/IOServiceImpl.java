package serviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import org.omg.CORBA.PRIVATE_MEMBER;

import javafx.print.Collation;
import service.IOService;
import toolKit.Time;

public class IOServiceImpl implements IOService {

	@Override
	public boolean writeFile(String file, String userId, String fileName) {
		File f = new File("Files/" + userId + "_" + fileName);
		try {
			FileWriter fw = new FileWriter(f, false);
			fw.write(file);
			fw.flush();
			fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveFile(String file, String userId, String fileName) throws RemoteException {
		int max = 0;
		String maxFullName = "";
		int min = Integer.MAX_VALUE;
		String minFullName = "";
		String row = findAllSameFile(userId, fileName);
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
		if (file.equals(readFile(maxFullName))) {
			return false;
		}
		//
		Calendar calendar = Calendar.getInstance();
		String name = userId + "_" + fileName + "_" + String.valueOf(max + 1) + "_"
				+ String.valueOf(calendar.get(Calendar.YEAR)) + "-" + String.valueOf(calendar.get(Calendar.MONTH) + 1)
				+ "-" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "~"
				+ String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + "-"
				+ String.valueOf(calendar.get(Calendar.MINUTE)) + "-" + String.valueOf(calendar.get(Calendar.SECOND));
		// 删除最前面的文件
		if (max >= 10) {
			File deleteFile = new File("Files/" + minFullName);
			deleteFile.delete();
		}
		writeFile(file, name);
		return true;
	}

	@Override
	public String readFile(String userId, String fileName) {
		// TODO Auto-generated method stub
		File f = new File("Files/" + userId + "_" + fileName);
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

	@Override
	public String readFileList(String userId) {
		String result = "";
		File dir = new File("Files/");
		// TODO Auto-generated method stub
		for (File file : dir.listFiles()) {
			String[] name = file.getName().split("_");
			if (name[0].equals(userId)) {
				result = result + file.getName() + "/";
			}
		}
		return result;
	}

	private String readFile(String fileName) {
		// TODO Auto-generated method stub
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

	private boolean writeFile(String file, String fileName) {
		File f = new File("Files/" + fileName);
		try {
			FileWriter fw = new FileWriter(f, false);
			fw.write(file);
			fw.flush();
			fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
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

	@Override
	public String showVersion(String userId, String fileName) throws RemoteException {
		// TODO Auto-generated method stub
		String result="";
		ArrayList<Time> times=new ArrayList<Time>();
		String row=findAllSameFile(userId, fileName);
		if(row.length()<2){
			return "";
		}
		for(String cache:row.split("/")){
			times.add(new Time(cache.split("_")[3]));
		}
		Collections.sort(times);
		for(Time time:times){
			System.out.println(time);
			result=result+time.toString()+"/";
		}
		return result;
	}
}
