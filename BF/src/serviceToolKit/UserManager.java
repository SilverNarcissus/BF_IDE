package serviceToolKit;

import java.io.Serializable;
import java.util.ArrayList;

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
