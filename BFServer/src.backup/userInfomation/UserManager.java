package userInfomation;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 掌握所有用户信息的类
 * 
 * @author SilverNarcissus
 */
public class UserManager implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<UserInformation> userInformations = new ArrayList<UserInformation>();

	public void addUserInformation(UserInformation userInfomation) {
		userInformations.add(userInfomation);
	}
	public UserInformation getUserInfomation(String userName){
		for(UserInformation userInformation:userInformations){
			if(userInformation.getUserName().equals(userName)){
				return userInformation;
			}
		}
		return null;
	}
}
