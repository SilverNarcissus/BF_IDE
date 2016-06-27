package serviceToolKit;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserInformation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	private Map<String,String> methodMap;
	public UserInformation(String userName,String password){
		this.userName=userName;
		this.password=password;
		methodMap=new HashMap<String,String>();
	}
	public String getUserName(){
		return userName;
	}
	public String getPassword(){
		return password;
	}
	public void setUserName(String userName){
		this.userName=userName;
	}
	public void setPassword(String password){
		this.password=password;
	}
	public Map<String, String> getMethodMap(){
		return methodMap;
	}
}
