//请不要修改本文件名
package serviceImpl;

import java.rmi.RemoteException;
import java.util.ArrayList;

import compiler.MemoryCell;
import service.ExecuteService;
import service.UserService;
/**
 * 执行代码的接口的实例
 * 
 * @author SilverNarcissus
 */
public class ExecuteServiceImpl implements ExecuteService {

	ArrayList<MemoryCell> memorycells;

	/**
	 * 执行代码的方法
	 */
	@Override
	public String execute(String code, String param) throws RemoteException {
		// TODO Auto-generated method stub
		String out = "";
		int inputPointer = 0;
		int pointer = 0;
		int programCounter = 0;
		memorycells=new ArrayList<MemoryCell>();
		memorycells.add(new MemoryCell(0));
		while (programCounter < code.length()) {
			char command = code.charAt(programCounter);
			switch (command) {
			case '+':
				memorycells.get(pointer).add();
				programCounter++;
				break;
			case '-':
				memorycells.get(pointer).sub();
				programCounter++;
				break;
			case '>':
				pointer++;
				if (pointer > memorycells.size() - 1) {
					memorycells.add(new MemoryCell(pointer));
				}
				programCounter++;
				break;
			case '<':
				pointer--;
				if (pointer < 0) {
					System.out.println("运行错误：指针移动到了不存在的内存空间！");
				}
				programCounter++;
				break;
			case ',':
				if (inputPointer >= param.length()) {
					System.out.println("未输入足够的参数！");
				}
				memorycells.get(pointer).setValue((int) param.charAt(inputPointer));
				inputPointer++;
				programCounter++;
				break;
			case '.':
				out = out + memorycells.get(pointer).getValueInChar();
				programCounter++;
				break;
			case '[':
				if (memorycells.get(pointer).getValue() == 0) {
					programCounter = rightShift(programCounter,code)+1;
				} else {
					programCounter++;
				}
				break;
			case ']':
				if (memorycells.get(pointer).getValue() != 0) {
					programCounter = liftShift(programCounter,code)+1;
				} else {
					programCounter++;
				}
				break;
			default:
				System.out.println("Ignore charcter");
				programCounter++;
				break;
			}
		}
		return out;
	}


	private int rightShift(int programCounter,String code) {
		int count=0;
		for(int i=programCounter+1;i<code.length();i++){
			if(code.charAt(i)=='['){
				count++;
				continue;
			}
			if(code.charAt(i)==']'&&count==0){
				return i;
			}
			else if(code.charAt(i)==']'){
				count--;
			}
		}
		System.out.println("找不到对应的“]”");
		return -1;
	}

	private int liftShift(int programCounter,String code) {
		int count=0;
		for(int i=programCounter-1;i>=0;i--){
			if(code.charAt(i)==']'){
				count++;
				continue;
			}
			if(code.charAt(i)=='['&&count==0){
				return i;
			}
			else if(code.charAt(i)=='['){
				count--;
			}
		}
		System.out.println("找不到对应的“[”");
		return -1;
	}
}
