package ui;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * 用于更改字符的颜色属性
 * 
 * @author SilverNarcissus
 */
public class CodeTextPanel extends JTextPane {
	/**
	 * 
	 */
	// 字符集的设定
	private static final long serialVersionUID = 1L;
	private static StyleContext cont = StyleContext.getDefaultStyleContext();
	private static AttributeSet attrRed = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
	private static AttributeSet attrBlue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLUE);
	private static AttributeSet attrYellow = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
			Color.CYAN);
	private static AttributeSet attrGreen = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
			Color.GREEN);
	private static AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
			Color.BLACK);
	private static DefaultStyledDocument doc = new DefaultStyledDocument() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 插入字符并设定字符属性的方法
		 * 
		 * @author SilverNarcissus
		 */
		public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
			super.insertString(offset, str, a);
			String text = getText(0, getLength());
			for (int i = 0; i < getLength(); i++) {
				switch (text.substring(i, i + 1)) {
				case "^":
					setCharacterAttributes(i, 1, CodeTextPanel.attrRed, false);
					break;
				case "+":
				case "-":
					setCharacterAttributes(i, 1, CodeTextPanel.attrBlue, false);
					break;
				case ">":
				case "<":
					setCharacterAttributes(i, 1, CodeTextPanel.attrGreen, false);
					break;
				case "[":
				case "]":
					setCharacterAttributes(i, 1, CodeTextPanel.attrRed, false);
					break;
				case ",":
				case ".":
					setCharacterAttributes(i, 1, CodeTextPanel.attrYellow, false);
					break;
				default:
					setCharacterAttributes(i, 1, CodeTextPanel.attrBlack, false);
					break;
				}
			}
		}
	};

	/**
	 * 加入文档的构造方法
	 * 
	 * @author SilverNarcissus
	 */
	public CodeTextPanel() {
		super(doc);
	}

	/**
	 * 设定文字的方法
	 * 
	 * @author SilverNarcissus
	 */
	@Override
	public void setText(String t) {
		super.setText(t);
	}
}
