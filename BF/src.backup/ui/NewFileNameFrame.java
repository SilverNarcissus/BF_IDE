package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import fileHelper.FileName;
import ioMethod.ReadFileListInFullName;
import ioMethod.WriteFileInNewestVersion;
import ioMethod.WriteFileWithIDAndFileName;
import rmi.RemoteHelper;
import service.IOService;

/**
 * 新建文件面板
 * 
 * @author SilverNarcissus
 */
public class NewFileNameFrame {
	private JFrame frame;
	private JTextArea fileNameArea;
	private JLabel warningLabel;
	private FileName fileName;
	private String userID;
	private MainFrame mainFrame;

	/**
	 * 构建UI面板
	 * 
	 * @author SilverNarcissus
	 */
	public NewFileNameFrame(FileName fileName, String userID, MainFrame mainFrame) {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screensize.getWidth();
		int height = (int) screensize.getHeight();
		this.mainFrame = mainFrame;
		this.fileName = fileName;
		this.userID = userID;
		frame = new JFrame("NewFile");
		frame.setLayout(new BorderLayout());
		JPanel backgroundPanel = new JPanel();
		//
		TitledBorder tBorder1 = (BorderFactory.createTitledBorder("NewFile"));
		tBorder1.setTitleFont(new Font("Monaco", Font.PLAIN, 15));
		tBorder1.setTitleColor(Color.blue);
		backgroundPanel.setBorder(tBorder1);
		//
		Box box = new Box(BoxLayout.Y_AXIS);
		//
		JPanel panel0 = new JPanel();
		JLabel promoteLabel = new JLabel("请输入要新建的文件名，其中不得包含“/”字符");
		promoteLabel.setForeground(Color.BLUE);
		panel0.add(promoteLabel);
		//
		JPanel panel1 = new JPanel();
		JLabel label1 = new JLabel("FileName:");
		fileNameArea = new JTextArea(1, 10);
		fileNameArea.setBorder(BorderFactory.createEtchedBorder());
		panel1.add(label1);
		panel1.add(fileNameArea);
		//
		JPanel panel2 = new JPanel();
		JButton createButton = new JButton("创建新文件");
		createButton.addActionListener(new CreateListener());
		JButton cancelButton = new JButton("取消");
		cancelButton.addActionListener(new CancelListener());
		panel2.add(createButton);
		panel2.add(cancelButton);
		//
		JPanel panel3 = new JPanel();
		warningLabel = new JLabel();
		warningLabel.setForeground(Color.RED);
		panel3.add(warningLabel);
		//
		box.add(panel0);
		box.add(panel1);
		box.add(panel3);
		box.add(panel2);
		//
		backgroundPanel.add(box);
		frame.add(backgroundPanel, BorderLayout.CENTER);
		frame.setSize(300, 200);
		frame.pack();
		frame.setLocation(width / 2 - frame.getWidth() / 2, height / 2 - frame.getHeight() / 2 - 50);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
	}

	/**
	 * 新建按钮的监听
	 * 
	 * @author SilverNarcissus
	 */
	class CreateListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 检查是否有重名文件
			if (mainFrame.getSaveFlag()
					|| JOptionPane.showConfirmDialog(frame, "You will lose all unsaved file, go ahead?", "Prompt",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				String fileNames = "";
				try {
					IOService ioService = RemoteHelper.getInstance().getIOService();
					ioService.setReadFileListMethod(new ReadFileListInFullName());
					fileNames = ioService.readFileList(userID, "");
				} catch (Exception ex) {
					// TODO: handle exception
					ex.printStackTrace();
				}
				// 防御性编程
				if (fileNameArea.getText().length() == 0) {
					warningLabel.setText("文件名不能为空，请重新输入文件名");
					frame.pack();
					return;
				}
				for (char c : fileNameArea.getText().toCharArray()) {
					if (c == '/') {
						warningLabel.setText("文件名包含非法字符，请重新输入文件名");
						frame.pack();
						return;
					}
				}
				if (!fileNames.equals("")) {
					for (String fileName : fileNames.split("/")) {
						if (fileName.split("_")[1].equals(fileNameArea.getText())) {
							warningLabel.setText("文件名重复，请重新输入文件名");
							frame.pack();
							return;
						}
					}
				}
				// System.out.println(fileNames);
				fileName.setFileName(fileNameArea.getText());
				Calendar calendar = Calendar.getInstance();
				try {
					String name = fileName.getFileName() + "_1_" + String.valueOf(calendar.get(Calendar.YEAR)) + "-"
							+ String.valueOf(calendar.get(Calendar.MONTH) + 1) + "-"
							+ String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "~"
							+ String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + "-"
							+ String.valueOf(calendar.get(Calendar.MINUTE)) + "-"
							+ String.valueOf(calendar.get(Calendar.SECOND));
					System.out.println("sajkfgashgqw:" + mainFrame.getUserName());
					IOService ioService = RemoteHelper.getInstance().getIOService();
					ioService.setWriteFileMethod(new WriteFileWithIDAndFileName());
					ioService.writeFile("", mainFrame.getUserName(), name);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				mainFrame.setTitle("BF Client--" + fileName);
				mainFrame.setEnable();
				mainFrame.setMenuEnable();
				mainFrame.changeSaveFlag();
				frame.dispose();
			}
		}
	}

	/**
	 * 取消按钮的监听
	 * 
	 * @author SilverNarcissus
	 */
	class CancelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			mainFrame.setFocusable(true);
		}
	}
}
