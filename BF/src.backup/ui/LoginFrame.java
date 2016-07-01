package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import rmi.RemoteHelper;

public class LoginFrame {
	JTextArea userNameArea;
	JPasswordField passwordField;
	JFrame frame;
	JLabel warningLabel;

	public LoginFrame() {
		Dimension   screensize   =   Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screensize.getWidth();
		int height = (int)screensize.getHeight();
		System.out.println(width);
		System.out.println(height);
		frame = new JFrame("Login");
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
		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		passwordField.setEchoChar('*');
		panel2.add(label2);
		panel2.add(passwordField);
		//
		JPanel panel3 = new JPanel();
		JButton createButton = new JButton("创建新用户");
		createButton.addActionListener(new CreateListener());
		JButton loginButton = new JButton("登录");
		loginButton.addActionListener(new LoginListener());
		JButton cancelButton = new JButton("退出");
		cancelButton.addActionListener(new CancelListener());
		panel3.add(createButton);
		panel3.add(loginButton);
		panel3.add(cancelButton);
		//
		warningLabel= new JLabel("");
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
		frame.setLocation(width/2-frame.getWidth()/2,height/2-frame.getHeight()/2-50);
		frame.setVisible(true);

	}

	class CreateListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			new CreateNewUserFrame();
			frame.dispose();
		}
	}

	class LoginListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				if (RemoteHelper.getInstance().getUserService().login(userNameArea.getText(),
						String.valueOf(passwordField.getPassword()))) {
					frame.dispose();
					new MainFrame(userNameArea.getText());
				} else {
                warningLabel.setText("用户名不存在或密码错误，请重试");
				}
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
