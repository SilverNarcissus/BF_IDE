package compiler;
/**
 * 内存空间属性
 * 
 * @author SilverNarcissus
 */
public class MemoryCell {
	private int location;
	private int value;

	public MemoryCell(int location) {
		this.location = location;
		value = 0;
	}

	public void add() {
		value++;
	}

	public void sub() {
		value--;
	}

	public int getValue() {
		return value;
	}

	public char getValueInChar() {
		return (char) value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
