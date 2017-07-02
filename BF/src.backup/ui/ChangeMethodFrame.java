package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import rmi.RemoteHelper;

/**
 * 用于更改方法设定的面板
 * 
 * @author SilverNarcissus
 */
public class ChangeMethodFrame {
	private String userName;
	private JComboBox<String> methodName;
	private JTextArea codeArea;
	private JFrame frame;
	private JLabel warningLabel;

	public ChangeMethodFrame(String userName) {
		this.userName = userName;
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screensize.getWidth();
		int height = (int) screensize.getHeight();
		// System.out.println(width);
		// System.out.println(height);
		frame = new JFrame("ChangeMethodFrame");
		frame.setLayout(new BorderLayout());
		JPanel backgroundPanel = new JPanel();
		//
		TitledBorder tBorder1 = (BorderFactory.createTitledBorder("ChangeMethod"));
		tBorder1.setTitleFont(new Font("Monaco", Font.PLAIN, 15));
		tBorder1.setTitleColor(Color.blue);
		backgroundPanel.setBorder(tBorder1);
		//
		JPanel panel1 = new JPanel();
		JLabel label1 = new JLabel("MethodName:");
		
		codeArea = new JTextArea(3, 10);
		methodName = new JComboBox<String>();
		methodName.setBorder(BorderFactory.createEtchedBorder());
		methodName.addItemListener(new ComboItemListener());
		try {
			Map<String, String> methodMap = RemoteHelper.getInstance().getUserService().getUserMethodMap(userName);
			for (String method : methodMap.keySet()) {
				if (method != null) {
					methodName.addItem(method);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		panel1.add(label1);
		panel1.add(methodName);
		//
		JPanel panel2 = new JPanel();
		JLabel label2 = new JLabel("Code");
		codeArea.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(codeArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel2.add(label2);
		panel2.add(scrollPane);
		//
		JPanel panel3 = new JPanel();
		JButton changeButton = new JButton("修改");
		changeButton.addActionListener(new ChangeListener());
		JButton removeButton = new JButton("删除");
		removeButton.addActionListener(new RemoveListener());
		JButton cancelButton = new JButton("取消");
		cancelButton.addActionListener(new CancelListener());
		panel3.add(changeButton);
		panel3.add(removeButton);
		panel3.add(cancelButton);
		//
		JPanel panel4 = new JPanel();
		warningLabel = new JLabel("");
		warningLabel.setForeground(Color.red);
		panel4.add(warningLabel);
		//
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(panel1);
		box.add(panel2);
		box.add(panel4);
		box.add(panel3);
		backgroundPanel.add(box);
		frame.add(backgroundPanel, BorderLayout.CENTER);
		frame.setSize(300, 250);
		frame.setLocation(width / 2 - frame.getWidth() / 2, height / 2 - frame.getHeight() / 2 - 50);
		frame.setVisible(true);

	}
	/**
	 * 更改按钮监听
	 * 
	 * @author SilverNarcissus
	 */
	class ChangeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String name = (String) methodName.getSelectedItem();
				if (name == null || name == "") {
					warningLabel.setText("请选择要更改的方法");
					return;
				}
				//
				RemoteHelper.getInstance().getUserService().removeUserMethodMap(userName, name);
				RemoteHelper.getInstance().getUserService().putUserMethodMap(userName, name, codeArea.getText());
				warningLabel.setForeground(Color.BLUE);
				warningLabel.setText("修改成功");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			replaceJComboBox();
		}
	}
	/**
	 * 删除按钮监听
	 * 
	 * @author SilverNarcissus
	 */
	class RemoveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String name = (String) methodName.getSelectedItem();
				if (name == null || name == "") {
					warningLabel.setText("请选择要更改的方法");
					return;
				}
				//
				RemoteHelper.getInstance().getUserService().removeUserMethodMap(userName, name);
				warningLabel.setForeground(Color.BLUE);
				warningLabel.setText("删除成功");		
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			replaceJComboBox();
		}
	}
	/**
	 * 取消按钮监听
	 * 
	 * @author SilverNarcissus
	 */
	class CancelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
		}
	}
	/**
	 * 方法名选取改变监听
	 * 
	 * @author SilverNarcissus
	 */
	class ComboItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			String methodName = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();
			if (methodName != null) {
				try {
					codeArea.setText(
							RemoteHelper.getInstance().getUserService().getUserMethodMap(userName).get(methodName));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	/**
	 * 替换方法列表的方法
	 * 
	 * @author SilverNarcissus
	 */
	private void replaceJComboBox(){
		methodName.removeAllItems();
		try {
			Map<String, String> methodMap = RemoteHelper.getInstance().getUserService().getUserMethodMap(userName);
			for (String method : methodMap.keySet()) {
				if (method != null) {
					methodName.addItem(method);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
