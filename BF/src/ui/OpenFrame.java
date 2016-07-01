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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import rmi.RemoteHelper;
import service.IOService;
import serviceToolKit.ReadFileInNewestVersion;
import serviceToolKit.ReadFileListInOnlyFileName;
import toolKit.FileName;

public class OpenFrame {
	private FileName fileName;
	private String userID;
	private JList<String> fileList;
	private JLabel warningLabel;
	private JFrame frame;
	private CodeTextPanel textArea;
	private JFrame mainFrame;
	private MainFrame mainFrame2;

	public OpenFrame(FileName fileName, String userID, CodeTextPanel textArea, JFrame mainFrame, MainFrame mainFrame2) {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screensize.getWidth();
		int height = (int) screensize.getHeight();
		//
		JPanel backgroundPanel = new JPanel();
		this.textArea = textArea;
		this.fileName = fileName;
		this.userID = userID;
		this.mainFrame = mainFrame;
		this.mainFrame2 = mainFrame2;
		frame = new JFrame("Open");
		frame.setLayout(new BorderLayout());
		Box box = new Box(BoxLayout.Y_AXIS);
		//
		JPanel panel0 = new JPanel();
		JLabel promoteLabel = new JLabel("请选择您要读取的文件");
		promoteLabel.setHorizontalAlignment(SwingConstants.LEFT);
		promoteLabel.setLocation(frame.getWidth() + 10, frame.getHeight() + 20);
		promoteLabel.setForeground(Color.blue);
		panel0.add(promoteLabel);
		// 设置JList
		ArrayList<String> fileListNames = new ArrayList<String>();
		try {
			IOService ioService = RemoteHelper.getInstance().getIOService();
			ioService.setReadFileListMethod(new ReadFileListInOnlyFileName());
			String row = ioService.readFileList(userID, "");
			if (row.length() != 0) {
				for (String listName : row.split("/")) {
					fileListNames.add(listName);
				}
			}
			if (fileListNames.size() == 0) {
				fileListNames.add("<空>");
				promoteLabel.setText("您当前没有可读取的文件");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Collections.sort(fileListNames);
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
		TitledBorder tBorder1 = (BorderFactory.createTitledBorder("OpenFile"));
		tBorder1.setTitleFont(new Font("Monaco", Font.PLAIN, 15));
		tBorder1.setTitleColor(Color.blue);
		backgroundPanel.setBorder(tBorder1);
		//
		JPanel panel2 = new JPanel();
		warningLabel = new JLabel("");
		warningLabel.setForeground(Color.RED);
		warningLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel2.add(warningLabel);
		//
		JPanel panel1 = new JPanel();
		JButton createButton = new JButton("读取");
		createButton.addActionListener(new LoadListener());
		JButton cancelButton = new JButton("取消");
		cancelButton.addActionListener(new CancelListener());
		panel1.add(createButton);
		panel1.add(cancelButton);
		//
		box.add(panel0);
		box.add(scrollPane);
		box.add(panel2);
		box.add(panel1);
		//
		backgroundPanel.add(box);
		frame.add(backgroundPanel);
		frame.setSize(300, 240);
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
			if (mainFrame2.getSaveFlag()||JOptionPane.showConfirmDialog(frame, "You will lose all unsaved file, go ahead?", "Prompt",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				String fileName = fileList.getSelectedValue();
				if (fileName == null) {
					warningLabel.setText("请选择要读取的文件");
					return;
				}
				String code = "";
				try {
					IOService ioService = RemoteHelper.getInstance().getIOService();
					ioService.setReadFileMethod(new ReadFileInNewestVersion());
					code = ioService.readFile(userID, fileName);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				OpenFrame.this.fileName.setFileName(fileName);
				if (code.split("_").length == 2) {
					textArea.setText(code.split("_")[1]);
				}
				textArea.setEditable(true);
				textArea.setBackground(Color.WHITE);
				mainFrame.setTitle("BF Client--" + fileName + "-" + code.split("_")[0]);
				mainFrame2.setMenuEnable();
				mainFrame2.changeSaveFlag();
				frame.dispose();
			}
		}
	}
}
