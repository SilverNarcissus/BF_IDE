package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import ioMethod.ReadFileListMethod;
import ioMethod.ReadFileMethod;
import ioMethod.WriteFileMethod;
import service.ExecuteService;
import service.IOService;
import service.UserService;
import serviceImpl.ExecuteServiceImpl;
import serviceImpl.IOServiceImpl;
import serviceImpl.UserServiceImpl;
/**
 * RMI服务器端存根类
 * 
 * @author SilverNarcissus
 */
public class DataRemoteObject extends UnicastRemoteObject implements IOService, UserService, ExecuteService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4029039744279087114L;
	private IOService iOService;
	private UserService userService;
	private ExecuteServiceImpl executeServiceImpl;

	protected DataRemoteObject() throws RemoteException {
		iOService = new IOServiceImpl();
		userService = new UserServiceImpl();
		executeServiceImpl = new ExecuteServiceImpl();
	}

	@Override
	public boolean writeFile(String file, String userID, String fileName) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.writeFile(file, userID, fileName);
	}

	@Override
	public String execute(String code, String param) throws RemoteException {
		// TODO Auto-generated method stub
		return executeServiceImpl.execute(code, param);
	}

	@Override
	public String readFile(String userID, String fileName) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.readFile(userID, fileName);
	}

	@Override
	public boolean login(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.login(username, password);
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.logout(username);
	}

	@Override
	public boolean creatNewUser(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.creatNewUser(username, password);
	}

	@Override
	public String readFileList(String userID, String fileName) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.readFileList(userID, fileName);
	}

	@Override
	public void setReadFileListMethod(ReadFileListMethod readFileListMethod) throws RemoteException {
		iOService.setReadFileListMethod(readFileListMethod);
	}

	@Override
	public void setReadFileMethod(ReadFileMethod readFileMethod) throws RemoteException {
		iOService.setReadFileMethod(readFileMethod);
	}

	@Override
	public void setWriteFileMethod(WriteFileMethod writeFileMethod) throws RemoteException {
		iOService.setWriteFileMethod(writeFileMethod);
	}

	@Override
	public Map<String, String> getUserMethodMap(String userName) throws RemoteException {
		return userService.getUserMethodMap(userName);
	}

	@Override
	public boolean putUserMethodMap(String userName, String methodName, String code) throws RemoteException {
		return userService.putUserMethodMap(userName, methodName, code);
	}

	@Override
	public boolean removeUserMethodMap(String userName, String methodName) throws RemoteException {
		return userService.removeUserMethodMap(userName, methodName);
	}
}
