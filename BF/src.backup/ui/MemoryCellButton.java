package ui;

import javax.swing.JButton;

public class MemoryCellButton extends JButton {
	private int number;

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
}
