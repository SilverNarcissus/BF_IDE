//需要客户端的Stub
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
/**
 * 登录登出所用的接口
 * 
 * @author SilverNarcissus
 */
public interface UserService extends Remote {
	public boolean creatNewUser(String username, String password) throws RemoteException;

	public boolean login(String username, String password) throws RemoteException;

	public boolean logout(String username) throws RemoteException;

	public Map<String, String> getUserMethodMap(String userName) throws RemoteException;

	public boolean putUserMethodMap(String userName, String methodName, String code) throws RemoteException;

	public boolean removeUserMethodMap(String userName, String methodName) throws RemoteException;
}
