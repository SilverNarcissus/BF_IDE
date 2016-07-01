package ui;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class CodeTextPanel extends JTextPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static StyleContext cont = StyleContext.getDefaultStyleContext();
	private static AttributeSet attrRed = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
	private static AttributeSet attrBlue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLUE);
	private static AttributeSet attrYellow = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.CYAN);
	private static AttributeSet attrOrange = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.ORANGE);
	private static AttributeSet attrGreen = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
			Color.GREEN);
	private static AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
			Color.BLACK);
	private static DefaultStyledDocument doc = new DefaultStyledDocument() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
			super.insertString(offset, str, a);
			String text = getText(0, getLength());
			System.out.println(text);
			for (int i = 0; i < getLength(); i++) {
				switch (text.substring(i, i + 1)) {
				case "^":
					setCharacterAttributes(i, 1, attrRed, false);
					break;
				case "+":
				case "-":
					setCharacterAttributes(i, 1, attrBlue, false);
					break;
				case ">":
				case "<":
					setCharacterAttributes(i, 1, attrGreen, false);
					break;
				case "[":
				case "]":
					setCharacterAttributes(i, 1, attrYellow, false);
					break;
				case ",":
				case ".":
					setCharacterAttributes(i, 1, attrOrange, false);
					break;
				default:
					setCharacterAttributes(i, 1, attrBlack, false);
					break;
				}
			}
		}
	};

	public CodeTextPanel() {
		super(doc);
	}
	@Override
	public void setText(String t) {
		super.setText(t);
		
	}
}
