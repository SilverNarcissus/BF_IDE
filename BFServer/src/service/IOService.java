//需要客户端的Stub
package service;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import ioMethod.ReadFileListMethod;
import ioMethod.ReadFileMethod;
import ioMethod.WriteFileMethod;
/**
 * 执行IO任务的接口
 * 
 * @author SilverNarcissus
 */
public interface IOService extends Remote {
	public boolean writeFile(String file, String userId, String fileName) throws RemoteException;

	public String readFile(String userId, String fileName) throws RemoteException;

	public String readFileList(String userId,String fileName) throws RemoteException;

	public void setReadFileListMethod(ReadFileListMethod readFileListMethod) throws RemoteException;

	public void setReadFileMethod(ReadFileMethod readFileMethod) throws RemoteException;

	public void setWriteFileMethod(WriteFileMethod writeFileMethod) throws RemoteException;

}
