package serviceImpl;

import service.IOService;
import serviceToolKit.ReadFileListMethod;
import serviceToolKit.ReadFileMethod;
import serviceToolKit.WriteFileMethod;

public class IOServiceImpl implements IOService {
	private WriteFileMethod writeFileMethod;
	private ReadFileMethod readFileMethod;
	private ReadFileListMethod readFileListMethod;

	// 策略模式
	@Override
	public boolean writeFile(String file, String userId, String fileName) {
		return writeFileMethod.writeFile(file, userId, fileName);
	}

	@Override
	public String readFile(String userId, String fileName) {
		return readFileMethod.readFile(userId, fileName);
	}

	@Override
	public String readFileList(String userId, String fileName) {
		return readFileListMethod.readFileList(userId, fileName);
	}

	@Override
	public void setWriteFileMethod(WriteFileMethod writeFileMethod) {
		this.writeFileMethod = writeFileMethod;
	}

	@Override
	public void setReadFileMethod(ReadFileMethod readFileMethod) {
		this.readFileMethod = readFileMethod;
	}

	@Override
	public void setReadFileListMethod(ReadFileListMethod readFileListMethod) {
		this.readFileListMethod = readFileListMethod;
	}
}
