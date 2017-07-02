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

/**
 * 新建用户的面板
 * 
 * @author SilverNarcissus
 */
public class CreateNewUserFrame {
	private JLabel wrongLabel;
	private JTextArea userNameArea;
	private JPasswordField passwordField1;
	private JPasswordField passwordField2;
	private JFrame frame;
	private JPanel panel5;

	/**
	 * 构建UI面板
	 * 
	 * @author SilverNarcissus
	 */
	public CreateNewUserFrame() {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screensize.getWidth();
		int height = (int) screensize.getHeight();
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
		panel5 = new JPanel();
		JButton createButton = new JButton("创建新用户");
		createButton.addActionListener(new CreateListener());
		JButton backButton = new JButton("返回");
		backButton.addActionListener(new BackListener());
		panel5.add(createButton);
		panel5.add(backButton);
		//
		Box box = new Box(BoxLayout.Y_AXIS);
		JPanel panel0 = new JPanel();
		JLabel promoteLabel = new JLabel("用户名和密码必须均为长度大于1小于10的英文和数字");
		panel0.add(promoteLabel);
		//
		JPanel panel4 = new JPanel();
		wrongLabel = new JLabel("");
		wrongLabel.setForeground(Color.RED);
		panel4.add(wrongLabel);
		//
		box.add(panel0);
		box.add(panel1);
		box.add(panel2);
		box.add(panel3);
		box.add(panel4);
		box.add(panel5);
		backgroundPanel.add(box);
		frame.add(backgroundPanel, BorderLayout.CENTER);
		frame.setSize(300, 200);
		frame.setLocation(width / 2 - frame.getWidth() / 2, height / 2 - frame.getHeight() / 2 - 50);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * 新建按钮的监听
	 * 
	 * @author SilverNarcissus
	 */
	class CreateListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 完成的情况
			JButton button = (JButton) e.getSource();
			if (button.getText().equals("完成")) {
				new LoginFrame();
				frame.dispose();
			}
			//
			String userName = userNameArea.getText();
			if (userName.length() < 2 || userName.length() > 10) {
				wrongLabel.setText("用户名长度不合法！");
				wrongLabel.setForeground(Color.RED);
				frame.pack();
				return;
			}
			String password = String.valueOf(passwordField1.getPassword());
			if (password.length() < 2 || password.length() > 10) {
				wrongLabel.setText("密码长度不合法！");
				wrongLabel.setForeground(Color.RED);
				frame.pack();
				return;
			}
			if (!password.equals(String.valueOf(passwordField2.getPassword()))) {
				wrongLabel.setText("两次输入的密码不一致");
				wrongLabel.setForeground(Color.RED);
				frame.pack();
				return;
			}
			try {
				if (RemoteHelper.getInstance().getUserService().creatNewUser(userName, password)) {
					wrongLabel.setText("用户创建成功");
					wrongLabel.setForeground(Color.BLUE);
					panel5.remove(1);
					button.setText("完成");
					frame.pack();
					Thread thread=new Thread(){
						public void run(){
							try {
								Thread.sleep(1000);
								frame.dispose();
								new LoginFrame();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					thread.start();
					return;
				} else {
					wrongLabel.setText("用户名已存在");
					wrongLabel.setForeground(Color.RED);
					frame.pack();
					return;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 返回按钮的监听
	 * 
	 * @author SilverNarcissus
	 */
	class BackListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			new LoginFrame();
			frame.dispose();
		}
	}
}
