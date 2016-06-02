//服务器IOService的Stub，内容相同
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IOService extends Remote {
	public boolean writeFile(String file, String userId, String fileName) throws RemoteException;

	public boolean saveFile(String file, String userId, String fileName) throws RemoteException;

	public String readFile(String userId, String fileName) throws RemoteException;

	public String readFileList(String userId) throws RemoteException;

	public String showVersion(String userId, String fileName) throws RemoteException;

	public String readAllCanReadFileList(String userId) throws RemoteException;

	public String readNewestVersion(String userId, String fileName) throws RemoteException;
}
