package serviceImpl;

import ioMethod.ReadFileListMethod;
import ioMethod.ReadFileMethod;
import ioMethod.WriteFileMethod;
import service.IOService;

/**
 * 用于IO功能的接口的实例
 * 
 * @author SilverNarcissus
 */
public class IOServiceImpl implements IOService {
	private WriteFileMethod writeFileMethod;
	private ReadFileMethod readFileMethod;
	private ReadFileListMethod readFileListMethod;

	// 策略模式
	/**
	 * 写文件
	 * 
	 * @author SilverNarcissus
	 */
	@Override
	public boolean writeFile(String file, String userId, String fileName) {
		return writeFileMethod.writeFile(file, userId, fileName);
	}

	@Override
	/**
	 * 读文件
	 * 
	 * @author SilverNarcissus
	 */
	public String readFile(String userId, String fileName) {
		return readFileMethod.readFile(userId, fileName);
	}

	/**
	 * 读文件列表
	 * 
	 * @author SilverNarcissus
	 */
	@Override
	public String readFileList(String userId, String fileName) {
		return readFileListMethod.readFileList(userId, fileName);
	}

	/**
	 * 设定写文件方法
	 * 
	 * @author SilverNarcissus
	 */
	@Override
	public void setWriteFileMethod(WriteFileMethod writeFileMethod) {
		this.writeFileMethod = writeFileMethod;
	}

	/**
	 * 设定读文件方法
	 * 
	 * @author SilverNarcissus
	 */
	@Override
	public void setReadFileMethod(ReadFileMethod readFileMethod) {
		this.readFileMethod = readFileMethod;
	}

	/**
	 * 设定读文件列表方法
	 * 
	 * @author SilverNarcissus
	 */
	@Override
	public void setReadFileListMethod(ReadFileListMethod readFileListMethod) {
		this.readFileListMethod = readFileListMethod;
	}
}
