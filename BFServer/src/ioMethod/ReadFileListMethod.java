package ioMethod;

import java.io.Serializable;

/**
 * 读取文件列表的方法接口
 * 
 * @author SilverNarcissus
 */
public interface ReadFileListMethod extends Serializable{
	public String readFileList(String userID,String fileName);
}
