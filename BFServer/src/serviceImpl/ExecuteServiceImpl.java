//请不要修改本文件名
package serviceImpl;

import java.rmi.RemoteException;
import java.util.ArrayList;

import ToolKit.MemoryCell;
import service.ExecuteService;
import service.UserService;

public class ExecuteServiceImpl implements ExecuteService {
	public static void main(String[] args) {
		ExecuteServiceImpl executeServiceImpl = new ExecuteServiceImpl();
		try {
			String result=executeServiceImpl.execute(
					"++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<++++++++++++ +++.>.+++.------.--------.>+.>.",
					"");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	ArrayList<MemoryCell> memorycells = new ArrayList<MemoryCell>();
	ArrayList<Integer> leftBracketsLocations;
	ArrayList<Integer> rightBracketsLocations;

	/**
	 * 请实现该方法
	 */
	@Override
	public String execute(String code, String param) throws RemoteException {
		// TODO Auto-generated method stub
		int count=1000;
		String out = "";
		int inputPointer = 0;
		int pointer = 0;
		int programCounter = 0;
		memorycells.add(new MemoryCell(0));
		leftBracketsLocations = new ArrayList<Integer>();
		rightBracketsLocations = new ArrayList<Integer>();
		inputLeftBracketsLocations(code);
		inputRightBracketsLocations(code);
		while (programCounter < code.length()&&count>=0) {
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
					programCounter += rightShift(programCounter);
				} else {
					programCounter++;
				}
				break;
			case ']':
				if (memorycells.get(pointer).getValue() != 0) {
					programCounter -= liftShift(programCounter);
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

	private void inputLeftBracketsLocations(String code) {
		for (int i = 0; i < code.length(); i++) {
			if (code.charAt(i) == '[') {
				Integer integer = new Integer(i);
				leftBracketsLocations.add(integer);
			}
		}
	}

	private void inputRightBracketsLocations(String code) {
		for (int i = 0; i < code.length(); i++) {
			if (code.charAt(i) == ']') {
				Integer integer = new Integer(i);
				rightBracketsLocations.add(integer);
			}
		}
	}

	private int rightShift(int programCounter) {
		int result = -1;
		if (rightBracketsLocations.isEmpty()) {
			System.out.println("编译错误：找不到对应的“]”！");
			return -1;
		}
		for (Integer integer : rightBracketsLocations) {
			if (integer - programCounter > 0) {
				result = integer - programCounter;
				break;
			}
		}
		for (Integer integer : rightBracketsLocations) {
			if (integer - programCounter < result || integer - programCounter > 0) {
				result = integer - programCounter;
			}
		}
		if (result == -1) {
			System.out.println("编译错误：找不到对应的“]”！");
		}
		return result + 1;
	}

	private int liftShift(int programCounter) {
		int result = -1;
		if (leftBracketsLocations.isEmpty()) {
			System.out.println("编译错误：找不到对应的“[”！");
			return -1;
		}
		for (Integer integer : leftBracketsLocations) {
			if (programCounter - integer > 0) {
				result = programCounter - integer;
				break;
			}
		}
		for (Integer integer : leftBracketsLocations) {
			if (programCounter - integer < result || programCounter - integer > 0) {
				result = programCounter - integer;
			}
		}
		if (result == -1) {
			System.out.println("编译错误：找不到对应的“]”！");
		}
		return result - 1;
	}
}
