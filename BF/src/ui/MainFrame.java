package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import rmi.RemoteHelper;
import service.IOService;
import serviceToolKit.WriteFileInNewestVersion;
import toolKit.FileName;
import toolKit.MemoryCell;

public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private JLabel compileLabel;
	private int pointer;
	private ArrayList<MemoryCell> memorycells;
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
	private boolean revoke = false;
	private boolean redo = false;
	private boolean newFlag = false;
	private int width;
	private int height;
	private JSplitPane outerPanel;
	private JSplitPane visiablePanel;
	private JSplitPane innerPanel;
	private ArrayList<Box> boxes;
	private JSplitPane leaderPanel;

	public MainFrame(String userName) {
		leaderPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		outerPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		innerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		leaderPanel.setLeftComponent(outerPanel);
		leaderPanel.setDividerLocation(700);
		outerPanel.setBottomComponent(innerPanel);
		outerPanel.setDividerLocation(450);
		outerPanel.setDividerSize(2);
		innerPanel.setDividerLocation(350);
		innerPanel.setDividerSize(1);
		//
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) screensize.getWidth();
		height = (int) screensize.getHeight();
		this.userName = userName;
		// 创建窗体
		frame = new JFrame("BF Client");
		frame.setLayout(new BorderLayout());
		// 和菜单有关的界面代码
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu versionMenu = new JMenu("Version");
		JMenu codeMenu = new JMenu("Code");
		JMenu windowMenu = new JMenu("Window");
		menuBar.add(fileMenu);
		menuBar.add(versionMenu);
		menuBar.add(codeMenu);
		menuBar.add(windowMenu);
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
		JMenuItem revokeMenuItem = new JMenuItem("Revoke");
		codeMenu.add(revokeMenuItem);
		JMenuItem redoMenuItem = new JMenuItem("Redo");
		codeMenu.add(redoMenuItem);
		JMenuItem initializationMenuItem = new JMenuItem("Initialize");
		windowMenu.add(initializationMenuItem);

		frame.setJMenuBar(menuBar);

		newMenuItem.addActionListener(new MenuItemActionListener());
		openMenuItem.addActionListener(new MenuItemActionListener());
		saveMenuItem.addActionListener(new MenuItemActionListener());
		runMenuItem.addActionListener(new MenuItemActionListener());
		showVersionMenuItem.addActionListener(new MenuItemActionListener());
		revokeMenuItem.addActionListener(new MenuItemActionListener());
		redoMenuItem.addActionListener(new MenuItemActionListener());
		initializationMenuItem.addActionListener(new MenuItemActionListener());

		// 文本域的设计代码
		codeTextArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(codeTextArea);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		codeTextArea.setMargin(new Insets(10, 10, 10, 10));
		codeTextArea.setBackground(Color.LIGHT_GRAY);
		codeTextArea.setEditable(false);
		codeTextArea.addKeyListener(new RevokeListener());
		outerPanel.setTopComponent(scrollPane);

		// frame.add(scrollPane, BorderLayout.CENTER);
		// 生成输入输出面板
		// JPanel IOPanel = new JPanel();
		// IOPanel.setLayout(new BorderLayout());
		// IOPanel.setBackground(Color.white);
		inputTextArea = new JTextArea(3, 10);
		inputTextArea.setMargin(new Insets(10, 10, 10, 10));
		// inputTextArea.addKeyListener(new RevokeListener());
		outputResult = new JLabel();
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.setForeground(Color.WHITE);
		inputPanel.setBackground(Color.WHITE);
		JPanel outputPanel = new JPanel();
		outputPanel.setLayout(new BorderLayout());
		outputPanel.setBackground(Color.WHITE);
		outputPanel.setForeground(Color.WHITE);
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
		innerPanel.setLeftComponent(inputPanel);
		innerPanel.setRightComponent(outputPanel);

		// 接下来是可视化部分的设计
		//
		//
		visiablePanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		leaderPanel.setBottomComponent(visiablePanel);
		visiablePanel.setDividerLocation(450);
		JPanel visiablePanelAbove = new JPanel();
		JPanel compilePanel=new JPanel();
		JPanel displayPanel=new JPanel();
		visiablePanelAbove.setLayout(new BorderLayout());
		visiablePanelAbove.add(compilePanel,BorderLayout.NORTH);
		visiablePanelAbove.add(displayPanel,BorderLayout.SOUTH);
		
		JButton compileButton=new JButton("Compile");
		compileButton.addActionListener(new CompileListener());
		compileLabel=new JLabel("请点击编译按钮编译");
		compilePanel.add(compileButton);
		compilePanel.add(compileLabel);
		
		displayPanel.setLayout(new GridLayout(6, 5));
		JPanel visiablePanelBelow = new JPanel();
		visiablePanel.setLeftComponent(visiablePanelAbove);
		visiablePanel.setBottomComponent(visiablePanelBelow);
		
        boxes=new ArrayList<Box>();
        for(int i=0;i<30;i++){
		Box memorycell = new Box(BoxLayout.Y_AXIS);
		JLabel intLabel=new JLabel("int:");
		JLabel charLabel=new JLabel("char:");
		MemoryCellButoon memoryCellButoon=new MemoryCellButoon();
		memoryCellButoon.setText("M("+String.valueOf((i+1))+")");
		memoryCellButoon.setNumber(i+1);
		memorycell.add(intLabel);
		memorycell.add(charLabel);
		memorycell.add(memoryCellButoon);
		boxes.add(memorycell);
		displayPanel.add(memorycell);
        }
        
		//
		//

		// 显示结果
		frame.getContentPane().add(leaderPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1100, 650);
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
				revokeStack = new Stack<String>();
				redoStack = new Stack<String>();
				new NewFileNameFrame(fileName, userName, mainFrame);
				codeTextArea.setText("");
				revokeStack.push(codeTextArea.getText());
				break;
			case "Save":
				String code = codeTextArea.getText();
				try {
					IOService ioService = RemoteHelper.getInstance().getIOService();
					ioService.setWriteFileMethod(new WriteFileInNewestVersion());
					ioService.writeFile(code, userName, fileName.getFileName());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				break;
			case "Open":
				revokeStack = new Stack<String>();
				redoStack = new Stack<String>();
				new OpenFrame(fileName, userName, codeTextArea, frame);
				newFlag = true;
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
				revokeStack = new Stack<String>();
				redoStack = new Stack<String>();
				new ShowVersionFrame(fileName, userName, codeTextArea, frame);
				newFlag = true;
				break;
			case "Revoke":
				if (!((revokeStack.size() == 1 && !revoke) || revokeStack.empty())) {
					redo = false;
					if (!revoke) {
						redoStack.push(revokeStack.pop());
						revoke = true;
					}
					String temp1 = revokeStack.pop();
					codeTextArea.setText(temp1);
					redoStack.push(temp1);
				}
				break;
			case "Redo":
				if (!((redoStack.size() == 1 && !redo) || redoStack.empty())) {
					revoke = false;
					if (!redo) {
						revokeStack.push(redoStack.pop());
						redo = true;
					}
					String temp2 = redoStack.pop();
					codeTextArea.setText(temp2);
					revokeStack.push(temp2);
				}
				break;
			case "Initialize":
				// System.out.println("!!!");
				frame.setSize(700, 500);
				frame.setLocation(width / 2 - frame.getWidth() / 2, height / 2 - frame.getHeight() / 2 - 50);
				outerPanel.setDividerLocation(300);
				innerPanel.setDividerLocation(335);
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
			if (newFlag) {
				revokeStack.push(codeTextArea.getText());
				newFlag = false;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			revoke = false;
			revokeStack.push(codeTextArea.getText());
			// System.out.println(codeTextArea.getText());
		}
	}

	// 实时编译策略
	private String compile(String code, String param) {
		// TODO Auto-generated method stub
		String out = "";
		int inputPointer = 0;
		pointer = 0;
		int programCounter = 0;
		memorycells = new ArrayList<MemoryCell>();
		memorycells.add(new MemoryCell(0));
		while (programCounter < code.length()) {
			char command = code.charAt(programCounter);
			switch (command) {
			case '+':
				memorycells.get(pointer).add();
				programCounter++;
				break;
			case '-':
				memorycells.get(pointer).sub();
				programCounter++;
				break;
			case '>':
				pointer++;
				if (pointer > memorycells.size() - 1) {
					memorycells.add(new MemoryCell(pointer));
				}
				programCounter++;
				break;
			case '<':
				pointer--;
				if (pointer < 0) {
					System.out.println("运行错误：指针移动到了不存在的内存空间！");
				}
				programCounter++;
				break;
			case ',':
				if (inputPointer >= param.length()) {
					System.out.println("未输入足够的参数！");
				}
				memorycells.get(pointer).setValue((int) param.charAt(inputPointer));
				inputPointer++;
				programCounter++;
				break;
			case '.':
				out = out + memorycells.get(pointer).getValueInChar();
				programCounter++;
				break;
			case '[':
				if (memorycells.get(pointer).getValue() == 0) {
					programCounter = rightShift(programCounter, code) + 1;
					if(pointer==0){
						return "error";
					}
				} else {
					programCounter++;
				}
				break;
			case ']':
				if (memorycells.get(pointer).getValue() != 0) {
					programCounter = liftShift(programCounter, code) + 1;
					if(pointer==0){
						return "error";
					}
				} else {
					programCounter++;
				}
				break;
			default:
				System.out.println("Ignore charcter");
				programCounter++;
				break;
			}
		}
		return out;
	}

	private int rightShift(int programCounter, String code) {
		int count = 0;
		for (int i = programCounter + 1; i < code.length(); i++) {
			if (code.charAt(i) == '[') {
				count++;
				continue;
			}
			if (code.charAt(i) == ']' && count == 0) {
				return i;
			} else if (code.charAt(i) == ']') {
				count--;
			}
		}
		System.out.println("找不到对应的“]”");
		compileLabel.setText("编译错误：找不到对应的“]”");
		return -1;
	}

	private int liftShift(int programCounter, String code) {
		int count = 0;
		for (int i = programCounter - 1; i >= 0; i--) {
			if (code.charAt(i) == ']') {
				count++;
				continue;
			}
			if (code.charAt(i) == '[' && count == 0) {
				return i;
			} else if (code.charAt(i) == '[') {
				count--;
			}
		}
		System.out.println("找不到对应的“[”");
		compileLabel.setText("编译错误：找不到对应的“]”");
		return -1;
	}
	class CompileListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			compile(codeTextArea.getText(), inputTextArea.getText());
			int count=0;
			for(MemoryCell memoryCell:memorycells){
				if(count>=30){
					break;
				}
				JLabel intLabel=(JLabel)boxes.get(count).getComponents()[0];
				JLabel charLabel=(JLabel)boxes.get(count).getComponents()[1];
				intLabel.setText("int:"+String.valueOf(memoryCell.getValue()));
				charLabel.setText("char:"+memoryCell.getValueInChar());
				count++;
			}
		}
	}
}
