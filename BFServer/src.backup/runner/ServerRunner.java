package runner;

import rmi.RemoteHelper;
/**
 * 服务器端入口
 * 
 * @author SilverNarcissus
 */
public class ServerRunner {
	
	public ServerRunner() {
		new RemoteHelper();
	}
	
	public static void main(String[] args) {
		new ServerRunner();
	}
}
