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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import rmi.RemoteHelper;
import ui.NewMethodFrame.CancelListener;
import ui.NewMethodFrame.SaveListener;

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
		JButton changeButton = new JButton("Change");
		changeButton.addActionListener(new ChangeListener());
		JButton removeButton = new JButton("Delete");
		removeButton.addActionListener(new RemoveListener());
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new CancelListener());
		panel3.add(changeButton);
		panel3.add(removeButton);
		panel3.add(cancelButton);
		//
		warningLabel = new JLabel("");
		warningLabel.setForeground(Color.red);
		//
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(panel1);
		box.add(panel2);
		box.add(warningLabel);
		box.add(panel3);
		backgroundPanel.add(box);
		frame.add(backgroundPanel, BorderLayout.CENTER);
		frame.setSize(300, 250);
		frame.setLocation(width / 2 - frame.getWidth() / 2, height / 2 - frame.getHeight() / 2 - 50);
		frame.setVisible(true);

	}

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
				warningLabel.setText("Success");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			replaceJComboBox();
		}
	}

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
				warningLabel.setText("Success");		
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			replaceJComboBox();
		}
	}

	class CancelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
		}
	}

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
