package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.util.Stack;

import javax.swing.Action;
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
	private FileName fileName = new FileName("");
	private MainFrame mainFrame = this;
	private JFrame frame;
	private Stack<String> revokeStack = new Stack<String>();
	private Stack<String> redoStack = new Stack<String>();
	private boolean revoke=false;
	private boolean redo=false;
	private boolean newFlag=false;

	public MainFrame(String userName) {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screensize.getWidth();
		int height = (int) screensize.getHeight();
		this.userName = userName;
		// 创建窗体
		frame = new JFrame("BF Client");
		frame.setLayout(new BorderLayout());

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu versionMenu = new JMenu("Version");
		JMenu codeMenu=new JMenu("Code");
		menuBar.add(fileMenu);
		menuBar.add(versionMenu);
		menuBar.add(codeMenu);
		JMenuItem newMenuItem = new JMenuItem("New");
		fileMenu.add(newMenuItem);
		JMenuItem openMenuItem = new JMenuItem("Open");
		fileMenu.add(openMenuItem);
		JMenuItem saveMenuItem = new JMenuItem("Save");
		fileMenu.add(saveMenuItem);
		JMenuItem runMenuItem = new JMenuItem("Run");
		fileMenu.add(runMenuItem);
		JMenuItem showVersionMenuItem = new JMenuItem("Version");
		versionMenu.add(showVersionMenuItem);
		JMenuItem  revokeMenuItem = new JMenuItem("Revoke");
		codeMenu.add(revokeMenuItem);
		JMenuItem  redoMenuItem = new JMenuItem("Redo");
		codeMenu.add(redoMenuItem);

		frame.setJMenuBar(menuBar);

		newMenuItem.addActionListener(new MenuItemActionListener());
		openMenuItem.addActionListener(new MenuItemActionListener());
		saveMenuItem.addActionListener(new MenuItemActionListener());
		runMenuItem.addActionListener(new MenuItemActionListener());
		showVersionMenuItem.addActionListener(new MenuItemActionListener());
		revokeMenuItem.addActionListener(new MenuItemActionListener());
		redoMenuItem.addActionListener(new MenuItemActionListener());

		codeTextArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(codeTextArea);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		codeTextArea.setMargin(new Insets(10, 10, 10, 10));
		codeTextArea.setBackground(Color.LIGHT_GRAY);
		codeTextArea.setEditable(false);
		codeTextArea.addKeyListener(new RevokeListener());
		frame.add(scrollPane, BorderLayout.CENTER);
		// 生成输入输出面板
		JPanel IOPanel = new JPanel();
		IOPanel.setLayout(new BorderLayout());
		IOPanel.setBackground(Color.white);
		inputTextArea = new JTextArea(3, 10);
		inputTextArea.setBorder(new LineBorder(Color.green));
		inputTextArea.setMargin(new Insets(10, 10, 10, 10));
		// inputTextArea.addKeyListener(new RevokeListener());
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
		frame.setLocation(width / 2 - frame.getWidth() / 2, height / 2 - frame.getHeight() / 2 - 50);
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
				revokeStack=new Stack<String>();
				redoStack=new Stack<String>();
				new NewFileNameFrame(fileName, userName, mainFrame);
				codeTextArea.setText("");
				revokeStack.push(codeTextArea.getText());
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
				revokeStack=new Stack<String>();
				redoStack=new Stack<String>();
				new OpenFrame(fileName, userName, codeTextArea, frame);
				newFlag=true;
				// try {
				// RemoteHelper.getInstance().getIOService().showVersion(userName,
				// "123");
				// } catch (RemoteException e1) {
				// e1.printStackTrace();
				// }
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
			case "Version":
				revokeStack=new Stack<String>();
				redoStack=new Stack<String>();
				new ShowVersionFrame(fileName, userName, codeTextArea, frame);
				newFlag=true;
				break;
			case "Revoke":
				if(!((revokeStack.size()==1&&!revoke)||revokeStack.empty())){
				redo=false;
				if(!revoke){
					redoStack.push(revokeStack.pop());
					revoke=true;
				}
				String temp1=revokeStack.pop();
				codeTextArea.setText(temp1);
				redoStack.push(temp1);
				}
				break;
			case "Redo":
				if(!((redoStack.size()==1&&!redo)||redoStack.empty())){
				revoke=false;
				if(!redo){
					revokeStack.push(redoStack.pop());
					redo=true;
				}
				String temp2=redoStack.pop();
				codeTextArea.setText(temp2);
				revokeStack.push(temp2);
				}
				break;
			default:
				break;
			}
		}
	}

	public void setEnable() {
		codeTextArea.setEditable(true);
		codeTextArea.setBackground(Color.WHITE);
	}

	public String getUserName() {
		return userName;
	}

	class RevokeListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if(newFlag){
				revokeStack.push(codeTextArea.getText());
				newFlag=false;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			revoke=false;
			revokeStack.push(codeTextArea.getText());
			System.out.println(codeTextArea.getText());
		}
	}
}
