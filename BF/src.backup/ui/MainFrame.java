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
import java.util.Map;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import compiler.*;
import compiler.Compiler;
import fileHelper.FileName;
import ioMethod.WriteFileInNewestVersion;
import rmi.RemoteHelper;
import service.IOService;

/**
 * 主要面板，包含各个分功能面板
 * 
 * @author SilverNarcissus
 */
public class MainFrame extends JFrame {

	private JLabel compileLabel;
	private int pointer;
	private ArrayList<MemoryCell> memorycells;
	private static final long serialVersionUID = 1L;
	private CodeTextPanel codeTextPane;
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
	private JTextField addField;
	private JTextField subField;
	private JTextField loopField;
	private Compiler compiler;
	private JMenuItem saveMenuItem;
	private JMenu versionMenu;
	private JMenu codeMenu;
	private boolean saveFlag = true;

	/**
	 * 构建UI面板
	 * 
	 * @author SilverNarcissus
	 */
	public MainFrame(String userName) {
		// 一些初始化
		memorycells = new ArrayList<MemoryCell>();
		memorycells.add(new MemoryCell(0));
		compiler = new Compiler(memorycells);
		//
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
		versionMenu = new JMenu("Version");
		versionMenu.setEnabled(false);
		codeMenu = new JMenu("Code");
		codeMenu.setEnabled(false);
		JMenu methodMenu = new JMenu("Method");
		JMenu debugMenu = new JMenu("Debug");
		JMenu windowMenu = new JMenu("Window");
		JMenu userMenu = new JMenu("User");
		menuBar.add(fileMenu);
		menuBar.add(versionMenu);
		menuBar.add(codeMenu);
		menuBar.add(methodMenu);
		menuBar.add(debugMenu);
		menuBar.add(windowMenu);
		menuBar.add(userMenu);
		// File
		JMenuItem newMenuItem = new JMenuItem("New");
		fileMenu.add(newMenuItem);
		JMenuItem openMenuItem = new JMenuItem("Open");
		fileMenu.add(openMenuItem);
		saveMenuItem = new JMenuItem("Save");
		saveMenuItem.setEnabled(false);
		fileMenu.add(saveMenuItem);
		//
		// Version
		JMenuItem showVersionMenuItem = new JMenuItem("Version");
		versionMenu.add(showVersionMenuItem);
		//
		// code
		JMenuItem revokeMenuItem = new JMenuItem("Revoke");
		codeMenu.add(revokeMenuItem);
		JMenuItem redoMenuItem = new JMenuItem("Redo");
		codeMenu.add(redoMenuItem);
		JMenuItem runMenuItem = new JMenuItem("Run");
		codeMenu.add(runMenuItem);
		//
		// Method
		JMenuItem newMethodMenuItem = new JMenuItem("NewMethod");
		methodMenu.add(newMethodMenuItem);
		JMenuItem displayMethodMenuItem = new JMenuItem("DisplayMethod");
		methodMenu.add(displayMethodMenuItem);
		JMenuItem changeMethodMenuItem = new JMenuItem("ChangeMethod");
		methodMenu.add(changeMethodMenuItem);
		//
		// Debug
		JMenuItem totheNextBreakpointDebugMenuItem = new JMenuItem("ToTheNextBreakpoint");
		JMenuItem stepIntoDebugMenuItem = new JMenuItem("StepInto");
		JMenuItem stepOverDebugMenuItem = new JMenuItem("StepOver");
		JMenuItem stepOutDebugMenuItem = new JMenuItem("StepOut");
		JMenuItem debugInitializeDebugMenuItem = new JMenuItem("DebugInitialize");
		debugMenu.add(totheNextBreakpointDebugMenuItem);
		debugMenu.add(stepIntoDebugMenuItem);
		debugMenu.add(stepOverDebugMenuItem);
		debugMenu.add(stepOutDebugMenuItem);
		debugMenu.add(debugInitializeDebugMenuItem);
		// Window
		JMenuItem completeViewMenuItem = new JMenuItem("CompleteView");
		windowMenu.add(completeViewMenuItem);
		JMenuItem conciseViewMenuItem = new JMenuItem("ConciseView");
		windowMenu.add(conciseViewMenuItem);
		//
		// User
		JMenuItem LogoutMenuItem = new JMenuItem("Logout");
		userMenu.add(LogoutMenuItem);
		//
		frame.setJMenuBar(menuBar);
		// File
		newMenuItem.addActionListener(new MenuItemActionListener());
		openMenuItem.addActionListener(new MenuItemActionListener());
		saveMenuItem.addActionListener(new MenuItemActionListener());
		runMenuItem.addActionListener(new MenuItemActionListener());
		// Version
		showVersionMenuItem.addActionListener(new MenuItemActionListener());
		// Code
		revokeMenuItem.addActionListener(new MenuItemActionListener());
		redoMenuItem.addActionListener(new MenuItemActionListener());
		// Method
		newMethodMenuItem.addActionListener(new MenuItemActionListener());
		displayMethodMenuItem.addActionListener(new MenuItemActionListener());
		changeMethodMenuItem.addActionListener(new MenuItemActionListener());
		// Debug
		totheNextBreakpointDebugMenuItem.addActionListener(new MenuItemActionListener());
		stepIntoDebugMenuItem.addActionListener(new MenuItemActionListener());
		stepOverDebugMenuItem.addActionListener(new MenuItemActionListener());
		stepOutDebugMenuItem.addActionListener(new MenuItemActionListener());
		debugInitializeDebugMenuItem.addActionListener(new MenuItemActionListener());
		// Window
		completeViewMenuItem.addActionListener(new MenuItemActionListener());
		conciseViewMenuItem.addActionListener(new MenuItemActionListener());
		//
		// User
		LogoutMenuItem.addActionListener(new MenuItemActionListener());
		// 文本域的设计代码
		codeTextPane = new CodeTextPanel();
		JScrollPane scrollPane = new JScrollPane(codeTextPane);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		codeTextPane.setMargin(new Insets(10, 10, 10, 10));
		codeTextPane.setBackground(Color.LIGHT_GRAY);
		codeTextPane.setEditable(false);
		codeTextPane.addKeyListener(new RevokeListener());
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
		visiablePanel.setDividerSize(2);
		JPanel visiablePanelAbove = new JPanel();
		JPanel compilePanel = new JPanel();
		JPanel displayPanel = new JPanel();
		visiablePanelAbove.setLayout(new BorderLayout());
		visiablePanelAbove.add(compilePanel, BorderLayout.NORTH);
		visiablePanelAbove.add(displayPanel, BorderLayout.SOUTH);

		JButton compileButton = new JButton("Compile");
		compileButton.addActionListener(new CompileListener());
		compileLabel = new JLabel("请点击编译按钮编译");
		compilePanel.add(compileButton);
		compilePanel.add(compileLabel);

		displayPanel.setLayout(new GridLayout(6, 5));
		JPanel visiablePanelBelow = new JPanel();
		visiablePanel.setLeftComponent(visiablePanelAbove);
		visiablePanel.setBottomComponent(visiablePanelBelow);

		TitledBorder tBorder3 = (BorderFactory.createTitledBorder("Visiable"));
		tBorder3.setTitleFont(new Font("Monaco", Font.PLAIN, 15));
		tBorder3.setTitleColor(Color.GREEN);
		visiablePanelAbove.setBorder(tBorder3);

		boxes = new ArrayList<Box>();
		for (int i = 0; i < 30; i++) {
			Box memorycell = new Box(BoxLayout.Y_AXIS);
			JLabel intLabel = new JLabel("int:");
			JLabel charLabel = new JLabel("char:");
			MemoryCellButton memoryCellButoon = new MemoryCellButton();
			memoryCellButoon.addActionListener(new MoveListener());
			memoryCellButoon.setText("M(" + String.valueOf((i + 1)) + ")");
			memoryCellButoon.setNumber(i + 1);
			memorycell.add(intLabel);
			memorycell.add(charLabel);
			memorycell.add(memoryCellButoon);
			boxes.add(memorycell);
			displayPanel.add(memorycell);
		}

		//
		// 显示辅助面板
		JPanel addPanel = new JPanel();
		JPanel subPanel = new JPanel();
		JPanel loopPanel = new JPanel();
		JButton addButton = new JButton();
		JButton subButton = new JButton();
		JButton loopButton = new JButton();
		addButton.setText("+");
		subButton.setText("-");
		loopButton.setText("loop");
		addButton.addActionListener(new AssistantListener());
		subButton.addActionListener(new AssistantListener());
		loopButton.addActionListener(new AssistantListener());
		addButton.setForeground(Color.RED);
		subButton.setForeground(Color.green);
		loopButton.setForeground(Color.BLUE);
		addField = new JTextField(5);
		subField = new JTextField(5);
		loopField = new JTextField(5);

		addPanel.add(addButton);
		addPanel.add(addField);
		subPanel.add(subButton);
		subPanel.add(subField);
		loopPanel.add(loopButton);
		loopPanel.add(loopField);
		visiablePanelBelow.setLayout(new GridLayout(3, 1));
		visiablePanelBelow.add(addPanel);
		visiablePanelBelow.add(subPanel);
		visiablePanelBelow.add(loopPanel);
		TitledBorder tBorder4 = (BorderFactory.createTitledBorder("Assist"));
		tBorder4.setTitleFont(new Font("Monaco", Font.PLAIN, 15));
		tBorder4.setTitleColor(Color.YELLOW);
		visiablePanelBelow.setBorder(tBorder4);
		//
		// 显示结果
		frame.getContentPane().add(leaderPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1100, 650);
		frame.setLocation(width / 2 - frame.getWidth() / 2, height / 2 - frame.getHeight() / 2 - 50);
		frame.setVisible(true);
		codeTextPane.setText("");
		setConciseView();
	}

	/**
	 * 菜单按钮的监听
	 * 
	 * @author SilverNarcissus
	 */
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
				codeTextPane.setText("");
				revokeStack.push(codeTextPane.getText());
				clearBoxes();
				compiler.initialize();
				pointer = compiler.getPointer();
				break;
			case "Save":
				String code = codeTextPane.getText();
				saveFlag = true;
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
				new OpenFrame(fileName, userName, codeTextPane, frame, mainFrame);
				newFlag = true;
				clearBoxes();
				compiler.initialize();
				pointer = compiler.getPointer();
				break;
			case "Run":
				String result = "";
				try {
					result = RemoteHelper.getInstance().getExecuteService()
							.execute(replaceMethod(codeTextPane.getText()), inputTextArea.getText());
					outputResult.setText(result);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				break;
			case "Version":
				revokeStack = new Stack<String>();
				redoStack = new Stack<String>();
				new ShowVersionFrame(fileName, userName, codeTextPane, frame, mainFrame);
				newFlag = true;
				clearBoxes();
				compiler.initialize();
				pointer = compiler.getPointer();
				break;
			case "Revoke":
				if (!revoke) {
					redoStack.push(revokeStack.pop());
					revoke = true;
				}
				if (!((revokeStack.size() == 1 && !revoke) || revokeStack.empty())) {
					System.out.println("in");
					redo = false;
					String temp1 = revokeStack.pop();
					codeTextPane.setText(temp1);
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
					codeTextPane.setText(temp2);
					revokeStack.push(temp2);
				}
				break;
			case "NewMethod":
				new NewMethodFrame(userName);
				break;
			case "DisplayMethod":
				new ShowMethodFrame(userName);
				break;
			case "ChangeMethod":
				new ChangeMethodFrame(userName);
				break;
			case "ToTheNextBreakpoint":
				clearBoxes();
				removeMark();
				compileLabel.setText(compiler.toTheNextBreakpoint(codeTextPane.getText(), inputTextArea.getText()));
				pointer = compiler.getPointer();
				changeCompilerLabelColor();
				setMemoryCell();
				changeDisplay();
				setMark();
				//
				// outputResult.setText(String.valueOf(compiler.getProgramCounter()));
				//
				break;
			case "StepInto":
				clearBoxes();
				removeMark();
				// System.out.println("qwieqwudasj:" + codeTextPane.getText());
				compileLabel.setText(compiler.stepInto(codeTextPane.getText(), inputTextArea.getText()));
				pointer = compiler.getPointer();
				changeCompilerLabelColor();
				setMemoryCell();
				changeDisplay();
				setMark();
				//
				// outputResult.setText(String.valueOf(compiler.getProgramCounter()));
				//
				break;
			case "StepOver":
				clearBoxes();
				removeMark();
				compileLabel.setText(compiler.stepOver(codeTextPane.getText(), inputTextArea.getText()));
				pointer = compiler.getPointer();
				changeCompilerLabelColor();
				setMemoryCell();
				changeDisplay();
				setMark();
				//
				// outputResult.setText(String.valueOf(compiler.getProgramCounter()));
				//
				break;
			case "StepOut":
				clearBoxes();
				removeMark();
				compileLabel.setText(compiler.stepOut(codeTextPane.getText(), inputTextArea.getText()));
				pointer = compiler.getPointer();
				changeCompilerLabelColor();
				setMemoryCell();
				changeDisplay();
				setMark();
				//
				// outputResult.setText(String.valueOf(compiler.getProgramCounter()));
				//
				break;
			case "DebugInitialize":
				compiler.initialize();
				pointer = compiler.getPointer();
				clearBoxes();
				removeMark();
				//
				// outputResult.setText(String.valueOf(compiler.getProgramCounter()));
				//
				break;
			case "CompleteView":
				// System.out.println("!!!");
				completeView();
				break;
			case "ConciseView":
				setConciseView();
				break;
			case "Logout":
				if (saveFlag || JOptionPane.showConfirmDialog(frame, "You will lose all unsaved file, go ahead?",
						"Prompt", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					frame.dispose();
					new LoginFrame();
				}
				break;
			default:
				break;
			}
		}

		/**
		 * @param code
		 * @param x
		 * @return
		 */
	}

	/**
	 * 设定编辑区的可编辑性
	 * 
	 * @author SilverNarcissus
	 */
	public void setEnable() {
		codeTextPane.setEditable(true);
		codeTextPane.setBackground(Color.WHITE);
	}

	/**
	 * 返回用户的名字
	 * 
	 * @author SilverNarcissus
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 撤销重做的监听
	 * 
	 * @author SilverNarcissus
	 */
	class RevokeListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			// saveFlag = false;
			revoke = false;
			// revokeStack.push(codeTextPane.getText());
			//
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			revoke = false;
			if (newFlag) {
				saveFlag = false;
				// revokeStack.push(codeTextPane.getText());
				newFlag = false;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			saveFlag = false;
			revoke = false;
			revokeStack.push(codeTextPane.getText());
			// System.out.println(codeTextArea.getText());
		}
	}

	/**
	 * 实时编译策略
	 * 
	 * @author SilverNarcissus
	 */
	private String compile(String code, String param) {
		// TODO Auto-generated method stub
		String out = "";
		int inputPointer = 0;
		pointer = 0;
		int programCounter = 0;
		memorycells.clear();
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
					System.out.println("编译错误：未输入足够的参数！");
					return "编译错误：未输入足够的参数！";
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
					if (programCounter == 0) {
						return "编译错误：找不到对应的“]”";
					}
				} else {
					programCounter++;
				}
				break;
			case ']':
				if (memorycells.get(pointer).getValue() != 0) {
					programCounter = liftShift(programCounter, code) + 1;
					if (programCounter == 0) {
						return "编译错误：找不到对应的“[”";
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

	/**
	 * 实时编译策略中的右移
	 * 
	 * @author SilverNarcissus
	 */
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
		return -1;
	}

	/**
	 * 实时编译策略中的左移
	 * 
	 * @author SilverNarcissus
	 */
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
		return -1;
	}

	/**
	 * 编译按钮的监听
	 * 
	 * @author SilverNarcissus
	 */
	class CompileListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			compileLabel.setText(compile(replaceMethod(codeTextPane.getText()), inputTextArea.getText()));
			// 判断是否有编译错误
			if (compileLabel.getText().contains("编译错误：")) {
				compileLabel.setForeground(Color.RED);
			} else {
				compileLabel.setForeground(Color.BLACK);
			}
			clearBoxes();
			setMemoryCell();
			changeDisplay();
		}
	}

	/**
	 * 辅助按钮的监听
	 * 
	 * @author SilverNarcissus
	 */
	class AssistantListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 两者是控制前后括号的
			boolean flag1 = true;
			boolean flag2 = false;
			//
			try {
				if (e.getActionCommand() == "+") {
					for (int i = Integer.parseInt((addField.getText())); i > 0; i--) {
						if (flag1) {
							codeTextPane.setText(codeTextPane.getText() + "(");
							flag1 = false;
						}
						codeTextPane.setText(codeTextPane.getText() + "+");
						compileLabel.setForeground(Color.BLACK);
						flag2 = true;
					}
					if (flag2) {
						codeTextPane.setText(codeTextPane.getText() + ")");
					}
					addField.setText("");
				} else if (e.getActionCommand() == "-") {
					for (int i = Integer.parseInt((subField.getText())); i > 0; i--) {
						if (flag1) {
							codeTextPane.setText(codeTextPane.getText() + "(");
							flag1 = false;
						}
						codeTextPane.setText(codeTextPane.getText() + "-");
						compileLabel.setForeground(Color.BLACK);
					}
					if (flag2) {
						codeTextPane.setText(codeTextPane.getText() + ")");
					}
					subField.setText("");
				} else {
					for (int i = Integer.parseInt((loopField.getText())); i > 0; i--) {
						if (flag1) {
							codeTextPane.setText(codeTextPane.getText() + "(>");
							flag1 = false;
						}
						codeTextPane.setText(codeTextPane.getText() + "+");
						compileLabel.setForeground(Color.BLACK);
						flag2 = true;
					}
					if (flag2) {
						codeTextPane.setText(codeTextPane.getText() + "[<(Your code)>-]<");
						codeTextPane.setText(codeTextPane.getText() + ")");
					}
					loopField.setText("");
				}
			} catch (Exception ex) {
				compileLabel.setText("错误的辅助信息输入");
				compileLabel.setForeground(Color.RED);
				addField.setText("");
				subField.setText("");
				loopField.setText("");
			}
		}
	}

	/**
	 * 移动内存位置按钮的监听
	 * 
	 * @author SilverNarcissus
	 */
	class MoveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			MemoryCellButton button = (MemoryCellButton) e.getSource();
			int distance = button.getNumber() - pointer - 1;
			if (distance == 0) {
				return;
			} else if (distance > 0) {
				for (; distance > 0; distance--) {
					codeTextPane.setText(codeTextPane.getText() + ">");
				}
				compile(codeTextPane.getText(), inputTextArea.getText());
				changeDisplay();
			} else {
				for (; distance < 0; distance++) {
					codeTextPane.setText(codeTextPane.getText() + "<");
				}
				compile(codeTextPane.getText(), inputTextArea.getText());
				changeDisplay();
			}
		}
	}

	/**
	 * 改变编译提示板的颜色
	 * 
	 * @author SilverNarcissus
	 */
	private void changeCompilerLabelColor() {
		if (compileLabel.getText().contains("编译错误") || compileLabel.getText().contains("运行错误")) {
			compileLabel.setForeground(Color.RED);
		} else {
			compileLabel.setForeground(Color.BLACK);
		}
	}

	/**
	 * 改变可视化编程的位置状态
	 * 
	 * @author SilverNarcissus
	 */
	private void changeDisplay() {
		for (Box eachBox : boxes) {
			MemoryCellButton memoryCellButoon = (MemoryCellButton) eachBox.getComponent(2);
			if (memoryCellButoon.getNumber() == pointer + 1) {
				memoryCellButoon.setForeground(Color.GREEN);
			} else {
				memoryCellButoon.setForeground(Color.BLACK);
			}
		}
	}

	/**
	 * 清空可视化面板
	 * 
	 * @author SilverNarcissus
	 */
	private void clearBoxes() {
		for (int count = 0; count < 30; count++) {
			JLabel intLabel = (JLabel) boxes.get(count).getComponents()[0];
			JLabel charLabel = (JLabel) boxes.get(count).getComponents()[1];
			JButton button = (JButton) boxes.get(count).getComponents()[2];
			intLabel.setText("int:");
			charLabel.setText("char:");
			button.setForeground(Color.BLACK);
		}
	}

	/**
	 * 替换用户方法
	 * 
	 * @author SilverNarcissus
	 */
	private String replaceMethod(String code) {
		try {
			Map<String, String> methodMap = RemoteHelper.getInstance().getUserService().getUserMethodMap(userName);
			for (String key : methodMap.keySet()) {
				code = code.replaceAll(key, methodMap.get(key));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return code;
	}

	/**
	 * 设定可视化编程的内存区域
	 * 
	 * @author SilverNarcissus
	 */
	private void setMemoryCell() {
		int count = 0;
		for (MemoryCell memoryCell : memorycells) {
			if (count >= 30) {
				break;
			}
			JLabel intLabel = (JLabel) boxes.get(count).getComponents()[0];
			JLabel charLabel = (JLabel) boxes.get(count).getComponents()[1];
			intLabel.setText("int:" + String.valueOf(memoryCell.getValue()));
			charLabel.setText("char:" + memoryCell.getValueInChar());
			count++;
		}
	}

	/**
	 * Debug模式中设定指针位置
	 * 
	 * @author SilverNarcissus
	 */
	private void setMark() {
		String first = codeTextPane.getText().substring(0, compiler.getProgramCounter());
		String second = codeTextPane.getText().substring(compiler.getProgramCounter());
		codeTextPane.setText(first + "(^now^)" + second);
	}

	/**
	 * Debug模式中更改指针位置
	 * 
	 * @author SilverNarcissus
	 */
	private void removeMark() {
		System.out.println("new:" + codeTextPane.getText().replaceAll("\\(\\^now\\^\\)", ""));
		codeTextPane.setText(codeTextPane.getText().replaceAll("\\(\\^now\\^\\)", ""));
	}

	/**
	 * 设定菜单可编辑性
	 * 
	 * @author SilverNarcissus
	 */
	public void setMenuEnable() {
		saveMenuItem.setEnabled(true);
		versionMenu.setEnabled(true);
		codeMenu.setEnabled(true);
	}

	/**
	 * 获取文件的保存状态
	 * 
	 * @author SilverNarcissus
	 */
	public boolean getSaveFlag() {
		return saveFlag;
	}

	/**
	 * 改变文件的保存状态
	 * 
	 * @author SilverNarcissus
	 */
	public void changeSaveFlag() {
		saveFlag = true;
	}

	/**
	 * 设定简洁模式
	 * 
	 * @author SilverNarcissus
	 */
	private void setConciseView() {
		visiablePanel.setVisible(false);
		leaderPanel.setDividerSize(0);
		outerPanel.setDividerLocation(400);
		innerPanel.setDividerLocation(350);
		frame.setSize(700, 600);
		frame.setLocation(width / 2 - frame.getWidth() / 2, height / 2 - frame.getHeight() / 2 - 50);
		leaderPanel.repaint();
		visiablePanel.repaint();
		outerPanel.repaint();
		innerPanel.repaint();
		frame.repaint();
	}

	private void completeView() {
		frame.setSize(1100, 650);
		frame.setLocation(width / 2 - frame.getWidth() / 2, height / 2 - frame.getHeight() / 2 - 50);
		leaderPanel.setDividerLocation(700);
		leaderPanel.setDividerSize(3);
		visiablePanel.setDividerLocation(450);
		outerPanel.setDividerLocation(450);
		innerPanel.setDividerLocation(350);
		visiablePanel.setVisible(true);
		innerPanel.setVisible(true);
		leaderPanel.repaint();
		visiablePanel.repaint();
		outerPanel.repaint();
		innerPanel.repaint();
		frame.repaint();
	}
}
