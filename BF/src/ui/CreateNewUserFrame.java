package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JPasswordField;
import javax.swing.JTextArea;

import rmi.RemoteHelper;

public class CreateNewUserFrame {
	private JLabel wrongLabel;
	private JTextArea userNameArea;
	private JPasswordField passwordField1;
	private JPasswordField passwordField2;
	private JFrame frame;

	public CreateNewUserFrame() {
		Dimension   screensize   =   Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screensize.getWidth();
		int height = (int)screensize.getHeight();
		frame = new JFrame("CreateNewUser");
		frame.setLayout(new BorderLayout());
		JPanel backgroundPanel = new JPanel();
		//
		JPanel panel1 = new JPanel();
		JLabel label1 = new JLabel("UserName:");
		userNameArea = new JTextArea(1, 10);
		userNameArea.setBorder(BorderFactory.createEtchedBorder());
		panel1.add(label1);
		panel1.add(userNameArea);
		//
		JPanel panel2 = new JPanel();
		JLabel label2 = new JLabel("Password:");
		passwordField1 = new JPasswordField();
		passwordField1.setColumns(10);
		passwordField1.setEchoChar('*');
		panel2.add(label2);
		panel2.add(passwordField1);
		//
		JPanel panel3 = new JPanel();
		JLabel label3 = new JLabel("Confirm:");
		passwordField2 = new JPasswordField();
		passwordField2.setColumns(10);
		passwordField2.setEchoChar('*');
		panel3.add(label3);
		panel3.add(passwordField2);
		//
		JPanel panel4 = new JPanel();
		JButton createButton = new JButton("创建新用户");
		createButton.addActionListener(new CreateListener());
		JButton backButton = new JButton("返回");
		backButton.addActionListener(new BackListener());
		panel4.add(createButton);
		panel4.add(backButton);
		//
		Box box = new Box(BoxLayout.Y_AXIS);
		JLabel promoteLabel = new JLabel("用户名和密码必须均为长度大于1小于10的英文和数字");
		wrongLabel = new JLabel("");
		wrongLabel.setForeground(Color.RED);
		box.add(promoteLabel);
		box.add(panel1);
		box.add(panel2);
		box.add(panel3);
		box.add(wrongLabel);
		box.add(panel4);
		backgroundPanel.add(box);
		frame.add(backgroundPanel, BorderLayout.CENTER);
		frame.setSize(300, 200);
		frame.setLocation(width/2-frame.getWidth()/2,height/2-frame.getHeight()/2-50);
		frame.pack();
		frame.setVisible(true);
	}

	class CreateListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String userName = userNameArea.getText();
			if (userName.length() < 2 || userName.length() > 10) {
				wrongLabel.setText("用户名长度不合法！");
				wrongLabel.setForeground(Color.RED);
				return;
			}
			String password = String.valueOf(passwordField1.getPassword());
			if (password.length() < 2 || password.length() > 10) {
				wrongLabel.setText("密码长度不合法！");
				wrongLabel.setForeground(Color.RED);
				return;
			}
			if (!password.equals(String.valueOf(passwordField2.getPassword()))) {
				wrongLabel.setText("两次输入的密码不一致");
				wrongLabel.setForeground(Color.RED);
				return;
			}
			try {
				if (RemoteHelper.getInstance().getUserService().creatNewUser(userName, password)) {
					wrongLabel.setText("用户创建成功");
					wrongLabel.setForeground(Color.BLUE);
					return;
				} else {
					wrongLabel.setText("用户名已存在");
					wrongLabel.setForeground(Color.RED);
					return;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	class BackListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			new LoginFrame();
			frame.dispose();
		}
	}
}
