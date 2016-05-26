package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import rmi.RemoteHelper;
import toolKit.FileName;

public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea codeTextArea;
	private JTextArea inputTextArea;
	private JLabel outputResult;
	private String userName;
	private FileName fileName=new FileName("");
	private MainFrame mainFrame=this;

	public MainFrame(String userName) {
		this.userName = userName;
		// 创建窗体
		JFrame frame = new JFrame("BF Client");
		frame.setLayout(new BorderLayout());

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenuItem newMenuItem = new JMenuItem("New");
		fileMenu.add(newMenuItem);
		JMenuItem openMenuItem = new JMenuItem("Open");
		fileMenu.add(openMenuItem);
		JMenuItem saveMenuItem = new JMenuItem("Save");
		fileMenu.add(saveMenuItem);
		JMenuItem runMenuItem = new JMenuItem("Run");
		fileMenu.add(runMenuItem);
		frame.setJMenuBar(menuBar);

		newMenuItem.addActionListener(new MenuItemActionListener());
		openMenuItem.addActionListener(new MenuItemActionListener());
		saveMenuItem.addActionListener(new MenuItemActionListener());
		runMenuItem.addActionListener(new MenuItemActionListener());

		codeTextArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(codeTextArea);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		codeTextArea.setMargin(new Insets(10, 10, 10, 10));
		codeTextArea.setBackground(Color.LIGHT_GRAY);
		codeTextArea.setEditable(false);
		;
		frame.add(scrollPane, BorderLayout.CENTER);
		// 生成输入输出面板
		JPanel IOPanel = new JPanel();
		IOPanel.setLayout(new BorderLayout());
		IOPanel.setBackground(Color.white);
		inputTextArea = new JTextArea(3, 10);
		inputTextArea.setBorder(new LineBorder(Color.green));
		inputTextArea.setMargin(new Insets(10, 10, 10, 10));
		outputResult = new JLabel();
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		JPanel outputPanel = new JPanel();
		outputPanel.setLayout(new BorderLayout());
		// 设置边框
		TitledBorder tBorder1 = (BorderFactory.createTitledBorder("Input"));
		tBorder1.setTitleFont(new Font("Monaco", Font.PLAIN, 15));
		tBorder1.setTitleColor(Color.blue);
		inputPanel.setBorder(tBorder1);
		TitledBorder tBorder2 = (BorderFactory.createTitledBorder("Output"));
		tBorder2.setTitleColor(Color.red);
		tBorder2.setTitleFont(new Font("Monaco", Font.PLAIN, 15));
		tBorder2.setTitleJustification(TitledBorder.RIGHT);
		outputPanel.setBorder(tBorder2);
		inputPanel.add(inputTextArea, BorderLayout.CENTER);
		outputPanel.add(outputResult, BorderLayout.CENTER);
		IOPanel.add(inputPanel, BorderLayout.WEST);
		IOPanel.add(outputPanel, BorderLayout.CENTER);
		// 显示结果
		frame.add(IOPanel, BorderLayout.SOUTH);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 400);
		frame.setLocation(400, 200);
		frame.setVisible(true);
	}

	class MenuItemActionListener implements ActionListener {
		/**
		 * 子菜单响应事件
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			switch (cmd) {
			case "New":
				new NewFileNameFrame(fileName, userName, mainFrame);
				break;
			case "Save":
				String code = codeTextArea.getText();
				try {
					RemoteHelper.getInstance().getIOService().saveFile(code, userName, fileName.getFileName());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				break;
			case "Open":
				try {
					RemoteHelper.getInstance().getIOService().showVersion(userName, fileName.getFileName());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				break;
			case "Run":
				String result = "";
				try {
					result = RemoteHelper.getInstance().getExecuteService().execute(codeTextArea.getText(),
							inputTextArea.getText());
					outputResult.setText(result);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
	}

	class SaveActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String code = codeTextArea.getText();
			try {
				RemoteHelper.getInstance().getIOService().writeFile(code, "adm", "code");
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

	}

	class LoadActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String fileString = "";
			try {
				fileString = RemoteHelper.getInstance().getIOService().readFileList("AAA");
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			codeTextArea.setText(fileString);
		}
	}

	public void setEnable() {
		codeTextArea.setEditable(true);
	}
	public String getUserName(){
		return userName;
	}
}
