package ui;

import javax.swing.JButton;

/**
 * 可视化编程用的可视化内存空间
 * 
 * @author SilverNarcissus
 */
public class MemoryCellButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int number;

	/**
	 * 设定内存块的位置
	 * 
	 * @author SilverNarcissus
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	/**
	 * 得到内存块的位置
	 * 
	 * @author SilverNarcissus
	 */
	public int getNumber() {
		return number;
	}
}
