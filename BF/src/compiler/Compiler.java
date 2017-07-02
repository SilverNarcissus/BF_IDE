package compiler;

import java.util.ArrayList;
/**
 * Dubug编译器
 * 
 * @author SilverNarcissus
 */
public class Compiler {
	private int pointer;
	private int programCounter = 0;
	private int inputPointer;
	private String out;
	boolean jumpOut = false;
	private ArrayList<MemoryCell> memorycells;

	public Compiler(ArrayList<MemoryCell> memorycells) {
		this.memorycells = memorycells;
	}

	public int getPointer() {
		return pointer;
	}

	// 初始化debug编译器的状态
	public void initialize() {
		out = "";
		inputPointer = 0;
		pointer = 0;
		programCounter = 0;
		memorycells.clear();
		memorycells.add(new MemoryCell(0));
	}

	public int getProgramCounter() {
		return programCounter;
	}

	private void setJumpOut() {
		jumpOut = true;
	}

	public ArrayList<MemoryCell> getMemorycells() {
		return memorycells;
	}

	// 运行到下一断点
	public String toTheNextBreakpoint(String code, String param) {
		// 初始化部分
		if (programCounter == 0) {
			initialize();
		}
		//
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
					System.out.println("编译错误：未输入足够的参数！");
					return "编译错误：未输入足够的参数！";
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
					programCounter = rightShift(programCounter, code) + 1;
					if (programCounter == 0) {
						return "编译错误：找不到对应的“]”";
					}
				} else {
					programCounter++;
				}
				break;
			case ']':
				if (memorycells.get(pointer).getValue() != 0) {
					programCounter = liftShift(programCounter, code) + 1;
					if (programCounter == 0) {
						return "编译错误：找不到对应的“[”";
					}
				} else {
					programCounter++;
				}
				break;
			case '`':
				programCounter++;
				return out;
			case '{':
				programCounter++;
				setJumpOut();
				break;
			default:
				System.out.println("Ignore charcter");
				programCounter++;
				break;
			}
		}
		return out;
	}

	public String stepOver(String code, String param) {
		// 初始化部分
		if (programCounter == 0) {
			initialize();
		}
		//
		if (programCounter < code.length()) {
			char command = code.charAt(programCounter);
			if (command == '{') {
				System.out.println("in");
				setJumpOut();
			} else {
				stepInto(code, param);
			}
		}
		//
		while (programCounter < code.length() && jumpOut) {
			char command = code.charAt(programCounter);
			System.out.println("in2");
			switch (command) {
			case '+':
				memorycells.get(pointer).add();
				programCounter++;
				System.out.println(command + "hhah" + jumpOut);
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
					System.out.println("编译错误：未输入足够的参数！");
					return "编译错误：未输入足够的参数！";
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
					programCounter = rightShift(programCounter, code) + 1;
					if (programCounter == 0) {
						return "编译错误：找不到对应的“]”";
					}
				} else {
					programCounter++;
				}
				break;
			case ']':
				if (memorycells.get(pointer).getValue() != 0) {
					programCounter = liftShift(programCounter, code) + 1;
					if (programCounter == 0) {
						return "编译错误：找不到对应的“[”";
					}
				} else {
					programCounter++;
				}
				break;
			case '}':
				jumpOut = false;
				programCounter++;
				return out;
			case '{':
				programCounter++;
				setJumpOut();
				break;
			default:
				System.out.println("Ignore charcter");
				programCounter++;
				break;
			}
		}
		return out;
	}

	private int rightShift(int programCounter, String code) {
		int count = 0;
		for (int i = programCounter + 1; i < code.length(); i++) {
			if (code.charAt(i) == '[') {
				count++;
				continue;
			}
			if (code.charAt(i) == ']' && count == 0) {
				return i;
			} else if (code.charAt(i) == ']') {
				count--;
			}
		}
		System.out.println("找不到对应的“]”");
		return -1;
	}

	// 单步执行程序
	public String stepInto(String code, String param) {
		// 初始化部分
		if (programCounter == 0) {
			initialize();
		}
		//
		if (programCounter < code.length()) {
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
					System.out.println("编译错误：未输入足够的参数！");
					return "编译错误：未输入足够的参数！";
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
					programCounter = rightShift(programCounter, code) + 1;
					if (programCounter == 0) {
						return "编译错误：找不到对应的“]”";
					}
				} else {
					programCounter++;
				}
				break;
			case ']':
				if (memorycells.get(pointer).getValue() != 0) {
					programCounter = liftShift(programCounter, code) + 1;
					if (programCounter == 0) {
						return "编译错误：找不到对应的“[”";
					}
				} else {
					programCounter++;
				}
				break;
			case '`':
				programCounter++;
				return out;
			case '{':
				programCounter++;
				setJumpOut();
				break;
			default:
				System.out.println("Ignore charcter");
				programCounter++;
				break;
			}
		}
		return out;
	}

	private int liftShift(int programCounter, String code) {
		int count = 0;
		for (int i = programCounter - 1; i >= 0; i--) {
			if (code.charAt(i) == ']') {
				count++;
				continue;
			}
			if (code.charAt(i) == '[' && count == 0) {
				return i;
			} else if (code.charAt(i) == '[') {
				count--;
			}
		}
		System.out.println("找不到对应的“[”");
		return -1;
	}

	// 跳出当前区块
	public String stepOut(String code, String param) {
		// 初始化部分
		if (programCounter == 0) {
			initialize();
		}
		//
		if (jumpOut) {
			while (programCounter < code.length() && jumpOut) {
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
						System.out.println("编译错误：未输入足够的参数！");
						return "编译错误：未输入足够的参数！";
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
						programCounter = rightShift(programCounter, code) + 1;
						if (programCounter == 0) {
							return "编译错误：找不到对应的“]”";
						}
					} else {
						programCounter++;
					}
					break;
				case ']':
					if (memorycells.get(pointer).getValue() != 0) {
						programCounter = liftShift(programCounter, code) + 1;
						if (programCounter == 0) {
							return "编译错误：找不到对应的“[”";
						}
					} else {
						programCounter++;
					}
					break;
				case '}':
					programCounter++;
					return out;
				case '{':
					programCounter++;
					setJumpOut();
					break;
				default:
					System.out.println("Ignore charcter");
					programCounter++;
					break;
				}
			}
			return out;
		} else {
			return stepInto(code, param);
		}
	}
}
