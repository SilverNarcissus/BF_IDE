package serviceImpl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.Map;

import service.UserService;
import serviceToolKit.UserInformation;
import serviceToolKit.UserManager;

public class UserServiceImpl implements UserService {
	UserManager userManager;

	public UserServiceImpl() {
		userManager = loadUserInformation();
	}

	@Override
	public boolean creatNewUser(String userName, String password) throws RemoteException {
		if (userManager.getUserInfomation(userName) == null) {
			userManager.addUserInformation(new UserInformation(userName, password));
			saveUserInformation(userManager);
			return true;
		}
		return false;
	};

	@Override
	public boolean login(String userName, String password) throws RemoteException {
		if (userManager.getUserInfomation(userName) != null) {
			if (userManager.getUserInfomation(userName).getPassword().equals(password)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		return true;
	}

	@Override
	public Map<String, String> getUserMethodMap(String userName) throws RemoteException {
		if (userManager.getUserInfomation(userName) != null) {
			return userManager.getUserInfomation(userName).getMethodMap();
		} else {
			return null;
		}
	}

	@Override
	public boolean removeUserMethodMap(String userName, String methodName) throws RemoteException{
		if (userManager.getUserInfomation(userName) != null) {
			userManager.getUserInfomation(userName).getMethodMap().remove(methodName);
			saveUserInformation(userManager);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean putUserMethodMap(String userName, String methodName, String code) throws RemoteException {
		if (userManager.getUserInfomation(userName) != null) {
			userManager.getUserInfomation(userName).getMethodMap().put(methodName, code);
			saveUserInformation(userManager);
			return true;
		} else {
			return false;
		}
	}

	private void saveUserInformation(UserManager userManager) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream("UserInformation.ser");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(userManager);
			objectOutputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private UserManager loadUserInformation() {
		UserManager userManager = new UserManager();
		try {
			FileInputStream fileInputStream = new FileInputStream("UserInformation.ser");
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			userManager = (UserManager) objectInputStream.readObject();
			objectInputStream.close();
		} catch (Exception ex) {
			return userManager;
		}
		return userManager;
	}
}
