package ui;

import javax.swing.JButton;

public class MemoryCellButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int number;

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
}
