package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import service.ExecuteService;
import service.IOService;
import service.UserService;
import serviceImpl.ExecuteServiceImpl;
import serviceImpl.IOServiceImpl;
import serviceImpl.UserServiceImpl;

public class DataRemoteObject extends UnicastRemoteObject implements IOService, UserService,ExecuteService{
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
		executeServiceImpl=new ExecuteServiceImpl();
	}

	@Override
	public boolean writeFile(String file, String userId, String fileName) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.writeFile(file, userId, fileName);
	}
	@Override
	public  String execute(String code, String param) throws RemoteException{
		// TODO Auto-generated method stub
		return executeServiceImpl.execute(code, param);
	}

	@Override
	public String readFile(String userId, String fileName) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.readFile(userId, fileName);
	}

	@Override
	public String readFileList(String userId) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.readFileList(userId);
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
	public boolean saveFile(String file, String userId, String fileName) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.saveFile(file, userId, fileName);
	}

	@Override
	public String showVersion(String userId, String fileName) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.showVersion(userId, fileName);
	}

	@Override
	public String readAllCanReadFileList(String userId) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.readAllCanReadFileList(userId);
	}

	@Override
	public String readNewestVersion(String userId, String fileName) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.readNewestVersion(userId, fileName);
	}

}
