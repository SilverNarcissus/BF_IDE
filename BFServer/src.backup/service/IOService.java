//需要客户端的Stub
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

import serviceToolKit.ReadFileListMethod;
import serviceToolKit.ReadFileMethod;
import serviceToolKit.WriteFileMethod;

public interface IOService extends Remote {
	public boolean writeFile(String file, String userId, String fileName) throws RemoteException;

	public String readFile(String userId, String fileName) throws RemoteException;

	public String readFileList(String userId,String fileName) throws RemoteException;

	public void setReadFileListMethod(ReadFileListMethod readFileListMethod) throws RemoteException;

	public void setReadFileMethod(ReadFileMethod readFileMethod) throws RemoteException;

	public void setWriteFileMethod(WriteFileMethod writeFileMethod) throws RemoteException;

}
