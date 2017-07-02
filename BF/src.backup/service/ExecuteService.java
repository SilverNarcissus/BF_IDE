package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * 执行代码的接口
 * 
 * @author SilverNarcissus
 */
public interface ExecuteService extends Remote {

	public String execute(String code, String param) throws RemoteException;
}
