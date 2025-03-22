package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import command.GCommandManager;
import file.GFileManager;
import state.GDrawingStateManager;
import state.GEventStateMananger;
import state.GObserver;

public class GMenuBar extends JMenuBar implements GContainerInterface, GObserver {
	private static final long serialVersionUID = 1L;

	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu viewMenu;

	private JMenuItem newMenuItem;
	private JMenuItem openMenuItem;
	private JMenuItem loadImageMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem saveAsImageMenuItem;
	private JMenuItem exitMenuItem;

	private JMenuItem cutMenuItem;
	private JMenuItem copyMenuItem;
	private JMenuItem pasteMenuItem;
	private JMenuItem undoMenuItem;
	private JMenuItem redoMenuItem;

	private JMenuItem zoomInMenuItem;
	private JMenuItem zoomOutMenuItem;

	private GDrawingPanel drawingPanel;
	private GCommandManager commandManager;

	public GMenuBar(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		this.commandManager = GEventStateMananger.getInstance().getCommandManager();
		this.commandManager.addObserver(this); // 옵저버로 등록
	}

	@Override
	public void createComponents() {
		fileMenu = new JMenu("파일");
		editMenu = new JMenu("편집");
		viewMenu = new JMenu("보기");

		newMenuItem = new JMenuItem("새로 만들기");
		openMenuItem = new JMenuItem("열기");
		loadImageMenuItem = new JMenuItem("배경 이미지 불러오기");
		saveMenuItem = new JMenuItem("저장");
		saveAsMenuItem = new JMenuItem("다른 이름으로 저장");
		saveAsImageMenuItem = new JMenuItem("이미지로 저장");
		exitMenuItem = new JMenuItem("끝내기");

		cutMenuItem = new JMenuItem("자르기");
		copyMenuItem = new JMenuItem("복사");
		pasteMenuItem = new JMenuItem("붙여넣기");
		undoMenuItem = new JMenuItem("실행 취소");
		redoMenuItem = new JMenuItem("다시 실행");

		zoomInMenuItem = new JMenuItem("확대");
		zoomOutMenuItem = new JMenuItem("축소");
	}

	@Override
	public void arrangeComponents() {
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(loadImageMenuItem);
		fileMenu.add(new JSeparator());
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);
		fileMenu.add(saveAsImageMenuItem);
		fileMenu.add(new JSeparator());
		fileMenu.add(exitMenuItem);

		editMenu.add(undoMenuItem);
		editMenu.add(redoMenuItem);
		editMenu.add(new JSeparator());
		editMenu.add(cutMenuItem);
		editMenu.add(copyMenuItem);
		editMenu.add(pasteMenuItem);

		viewMenu.add(zoomInMenuItem);
		viewMenu.add(zoomOutMenuItem);

		this.add(fileMenu);
		this.add(editMenu);
		this.add(viewMenu);
	}

	@Override
	public void setAttributes() {
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
		this.setBackground(new Color(245, 245, 245));

		Font menuFont = new Font("맑은 고딕", Font.PLAIN, 12);

		fileMenu.setFont(menuFont);
		editMenu.setFont(menuFont);
		viewMenu.setFont(menuFont);

		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveAsMenuItem
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));

		zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ActionEvent.CTRL_MASK));
		zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));

		setMenuItemFont(newMenuItem, menuFont);
		setMenuItemFont(openMenuItem, menuFont);
		setMenuItemFont(loadImageMenuItem, menuFont);
		setMenuItemFont(saveMenuItem, menuFont);
		setMenuItemFont(saveAsMenuItem, menuFont);
		setMenuItemFont(saveAsImageMenuItem, menuFont);
		setMenuItemFont(exitMenuItem, menuFont);

		setMenuItemFont(cutMenuItem, menuFont);
		setMenuItemFont(copyMenuItem, menuFont);
		setMenuItemFont(pasteMenuItem, menuFont);
		setMenuItemFont(undoMenuItem, menuFont);
		setMenuItemFont(redoMenuItem, menuFont);

		setMenuItemFont(zoomInMenuItem, menuFont);
		setMenuItemFont(zoomOutMenuItem, menuFont);

		updateUndoRedoState();
	}

	private void setMenuItemFont(JMenuItem item, Font font) {
		item.setFont(font);
	}

	@Override
	public void update() {
		updateUndoRedoState();
	}

	// GMenuBar.java에서 updateUndoRedoState 메서드
	public void updateUndoRedoState() {
		boolean canUndo = commandManager.canUndo();
		boolean canRedo = commandManager.canRedo();

		System.out.println("메뉴 상태 업데이트 - canUndo: " + canUndo + ", canRedo: " + canRedo);

		undoMenuItem.setEnabled(canUndo);
		redoMenuItem.setEnabled(canRedo);
	}

	@Override
	public void addEventHandler() {
		ActionListener dummyAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("메뉴 선택: " + e.getActionCommand());
			}
		};

		saveMenuItem.addActionListener(e -> {
			File currentFile = GDrawingStateManager.getInstance().getCurrentFile();

			if (currentFile != null) {
				GFileManager.saveToFile(currentFile);
			} else {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter("Graphic Editor Files (*.ged)", "ged"));

				if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					if (!file.getName().toLowerCase().endsWith(".ged")) {
						file = new File(file.getPath() + ".ged");
					}
					GFileManager.saveToFile(file);
					GDrawingStateManager.getInstance().setCurrentFile(file);
				}
			}
		});

		saveAsMenuItem.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("Graphic Editor Files (*.ged)", "ged"));

			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (!file.getName().toLowerCase().endsWith(".ged")) {
					file = new File(file.getPath() + ".ged");
				}
				GFileManager.saveToFile(file);
			}
		});

		saveAsImageMenuItem.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images (*.png)", "png"));

			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (!file.getName().toLowerCase().endsWith(".png")) {
					file = new File(file.getPath() + ".png");
				}
				GFileManager.saveAsImage(file, drawingPanel);
			}
		});

		loadImageMenuItem.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("이미지 파일", "jpg", "jpeg", "png", "bmp", "gif");
			fileChooser.setFileFilter(filter);

			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				BufferedImage image = GFileManager.loadImage(file);
				if (image != null) {
					drawingPanel.setBackgroundImage(image);
				}
			}
		});

		openMenuItem.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("Graphic Editor Files (*.ged)", "ged"));

			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				GFileManager.loadFromFile(file);
			}
		});

		newMenuItem.addActionListener(e -> {
			drawingPanel.setBackgroundImage(null);
			GDrawingStateManager.getInstance().setShapes(java.util.Collections.emptyList());
			GDrawingStateManager.getInstance().setCurrentFile(null);
		});

		exitMenuItem.addActionListener(e -> System.exit(0));

		// addEventHandler 메서드 내부에서
		undoMenuItem.addActionListener(e -> {
			System.out.println("Undo 메뉴 클릭됨");
			if (commandManager.canUndo()) {
				commandManager.undo();
				updateUndoRedoState();
			}
		});

		redoMenuItem.addActionListener(e -> {
			System.out.println("Redo 메뉴 클릭됨");
			if (commandManager.canRedo()) {
				commandManager.redo();
				updateUndoRedoState();
			}
		});

		cutMenuItem.addActionListener(dummyAction);
		copyMenuItem.addActionListener(dummyAction);
		pasteMenuItem.addActionListener(dummyAction);

		zoomInMenuItem.addActionListener(dummyAction);
		zoomOutMenuItem.addActionListener(dummyAction);
	}
}