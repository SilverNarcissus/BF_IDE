package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import rmi.RemoteHelper;
import toolKit.FileName;
import ui.OpenFrame.CancelListener;
import ui.OpenFrame.LoadListener;

public class ShowVersionFrame {
	private FileName fileName;
	private String userID;
	private JList<String> fileList;
	private JLabel warningLabel;
	private JFrame frame;
	private JTextArea textArea;
	private JFrame mainFrame;

	public ShowVersionFrame(FileName fileName, String userID, JTextArea textArea, JFrame mainFrame) {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screensize.getWidth();
		int height = (int) screensize.getHeight();
		//
		JPanel backgroundPanel = new JPanel();
		this.textArea = textArea;
		this.fileName = fileName;
		this.userID = userID;
		this.mainFrame = mainFrame;
		frame = new JFrame("ChangeVersion");
		frame.setLayout(new BorderLayout());
		Box box = new Box(BoxLayout.Y_AXIS);
		//
		JLabel promoteLabel = new JLabel("请选择您要读取的版本");
		promoteLabel.setHorizontalAlignment(SwingConstants.LEFT);
		promoteLabel.setLocation(frame.getWidth() + 10, frame.getHeight() + 20);
		promoteLabel.setForeground(Color.blue);
		//
		JPanel panel1 = new JPanel();
		JButton createButton = new JButton("读取");
		createButton.addActionListener(new LoadListener());
		JButton cancelButton = new JButton("取消");
		cancelButton.addActionListener(new CancelListener());
		panel1.add(createButton);
		panel1.add(cancelButton);
		// 设置JList
		ArrayList<String> fileListNames = new ArrayList<String>();
		try {
			String row = RemoteHelper.getInstance().getIOService().showVersion(userID, fileName.getFileName());
			if (row.length() != 0) {
				for (String listName : row.split("/")) {
					System.out.println("!!!");
					fileListNames.add(listName);
				}
			}
			if (fileListNames.size() == 0) {
				fileListNames.add("<空>");
				promoteLabel.setText("请先读取您的文件");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String[] fileListNamesArray = new String[fileListNames.size()];
		int i = 0;
		for (String cache : fileListNames) {
			fileListNamesArray[i] = cache;
			i++;
		}
		fileList = new JList<String>(fileListNamesArray);
		fileList.setVisibleRowCount(5);
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(fileList);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		//
		TitledBorder tBorder1 = (BorderFactory.createTitledBorder("ChangeVersion"));
		tBorder1.setTitleFont(new Font("Monaco", Font.PLAIN, 15));
		tBorder1.setTitleColor(Color.blue);
		backgroundPanel.setBorder(tBorder1);
		//
		warningLabel = new JLabel("");
		warningLabel.setForeground(Color.RED);
		warningLabel.setHorizontalAlignment(SwingConstants.CENTER);
		//
		//
		box.add(promoteLabel);
		box.add(scrollPane);
		box.add(warningLabel);
		box.add(panel1);
		//
		backgroundPanel.add(box);
		frame.add(backgroundPanel);
		frame.setSize(300, 200);
		frame.pack();
		frame.setLocation(width / 2 - frame.getWidth() / 2, height / 2 - frame.getHeight() / 2 - 50);
		frame.setVisible(true);
		promoteLabel.setLocation(frame.getWidth() + 10, frame.getHeight() + 20);
	}

	class CancelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
		}
	}

	class LoadListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String fileName = fileList.getSelectedValue();
			System.out.println(fileName);
			if (fileName == null || fileName == "<空>") {
				warningLabel.setText("请选择要读取的版本");
				return;
			}
			String code = "";
			try {
				code = RemoteHelper.getInstance().getIOService().readFile(userID,
						ShowVersionFrame.this.fileName.getFileName() + "_" + fileName);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			textArea.setText(code);
			textArea.setEditable(true);
			mainFrame.setTitle("BF Client--" +ShowVersionFrame.this.fileName.getFileName()+"-"+fileName.split("_")[0]);
			frame.dispose();
		}
	}
}
