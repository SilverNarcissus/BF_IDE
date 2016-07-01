package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import rmi.RemoteHelper;

public class NewMethodFrame {
	private String userName;
	private JTextArea methodName;
	private JTextArea codeArea;
	private JFrame frame;
	private JLabel warningLabel;

	public NewMethodFrame(String userName) {
		this.userName=userName;
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screensize.getWidth();
		int height = (int) screensize.getHeight();
		// System.out.println(width);
		// System.out.println(height);
		frame = new JFrame("");
		frame.setLayout(new BorderLayout());
		JPanel backgroundPanel = new JPanel();
		//
		TitledBorder tBorder1 = (BorderFactory.createTitledBorder("NewMethod"));
		tBorder1.setTitleFont(new Font("Monaco", Font.PLAIN, 15));
		tBorder1.setTitleColor(Color.blue);
		backgroundPanel.setBorder(tBorder1);
		//
		JPanel panel1 = new JPanel();
		JLabel label1 = new JLabel("MethodName:");
		methodName = new JTextArea(1, 10);
		methodName.setBorder(BorderFactory.createEtchedBorder());
		panel1.add(label1);
		panel1.add(methodName);
		//
		JPanel panel2 = new JPanel();
		JLabel label2 = new JLabel("Code");
		codeArea = new JTextArea(3, 10);
		codeArea.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(codeArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel2.add(label2);
		panel2.add(scrollPane);
		//
		JPanel panel3 = new JPanel();
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new SaveListener());
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new CancelListener());
		panel3.add(saveButton);
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
		frame.setSize(300, 200);
		frame.setLocation(width / 2 - frame.getWidth() / 2, height / 2 - frame.getHeight() / 2 - 50);
		frame.setVisible(true);

	}

	class SaveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String name = methodName.getText();
				//检查函数是否重名
				if (RemoteHelper.getInstance().getUserService().getUserMethodMap(userName).containsKey(name)) {
					warningLabel.setText("Duplicate Definition!");
					warningLabel.setForeground(Color.RED);
					return;
				}
				RemoteHelper.getInstance().getUserService().putUserMethodMap(userName, name, codeArea.getText());
				warningLabel.setForeground(Color.BLUE);
				warningLabel.setText("Success");
			} catch (Exception ex) {
				// TODO: handle exception
				ex.printStackTrace();
			}
		}
	}

	class CancelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
		}
	}
}
