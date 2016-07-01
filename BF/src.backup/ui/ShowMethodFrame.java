package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import rmi.RemoteHelper;

public class ShowMethodFrame {
	private JFrame frame;

	public ShowMethodFrame(String userName) {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screensize.getWidth();
		int height = (int) screensize.getHeight();
		frame = new JFrame("NewMethodFrame");
		frame.setLayout(new BorderLayout());
		JPanel backgroundPanel = new JPanel();
		backgroundPanel.setLayout(new BorderLayout());
		//
		TitledBorder tBorder1 = (BorderFactory.createTitledBorder("DisplayMethod"));
		tBorder1.setTitleFont(new Font("Monaco", Font.PLAIN, 15));
		tBorder1.setTitleColor(Color.blue);
		backgroundPanel.setBorder(tBorder1);
		//
		JTextArea displayArea = new JTextArea(10, 20);
		displayArea.setEditable(false);
		displayArea.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(displayArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// 初始化方法信息
		try {
			Map<String, String> methodMap = RemoteHelper.getInstance().getUserService().getUserMethodMap(userName);
			for (String key : methodMap.keySet()) {
				System.out.println(key);
				displayArea.append(key + ": ");
				displayArea.append(methodMap.get(key) + "\n");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new CancelListener());
		//
		backgroundPanel.add(scrollPane, BorderLayout.CENTER);
		backgroundPanel.add(cancelButton,BorderLayout.SOUTH);
		frame.add(backgroundPanel, BorderLayout.CENTER);
		frame.setSize(300, 200);
		frame.setLocation(width / 2 - frame.getWidth() / 2, height / 2 - frame.getHeight() / 2 - 50);
		frame.setVisible(true);
	}

	class CancelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
		}
	}
}
