package serviceImpl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;

import service.UserService;
import toolKit.UserInformation;
import toolKit.UserManager;

public class UserServiceImpl implements UserService {
	UserManager userManager;

	public UserServiceImpl() {
		userManager = loadUserInformation();
	}

	@Override
	public boolean creatNewUser(String userName, String password) throws RemoteException {
		if (userManager.getUserInfomation(userName) == null) {
			userManager.addUserInformation(new UserInformation(userName, password));
			saveUserInformation();
			return true;
		}
		return false;
	};

	@Override
	public boolean login(String userName, String password) throws RemoteException {
		if(userManager.getUserInfomation(userName)!=null){
			if(userManager.getUserInfomation(userName).getPassword().equals(password)){
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		return true;
	}

	private void saveUserInformation() {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream("UserInformation.ser");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(objectOutputStream);
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
