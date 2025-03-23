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
import state.GClipboard;
import state.GDrawingStateManager;
import state.GEventStateMananger;
import state.GObserver;
import state.GZoomManager;
import type.GMode;
import type.GZoomType;

public class GMenuBar extends JMenuBar implements GContainerInterface, GObserver {
	private static final long serialVersionUID = 1L;

	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu viewMenu;
	private JMenu graphicMenu;

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
	private JMenuItem zoomResetMenuItem;

	private JMenu lineWidthMenu;
	private JMenu lineStyleMenu;
	private JMenu fontStyleMenu;
	private JMenu fontSizeMenu;

	private GDrawingPanel drawingPanel;
	private GCommandManager commandManager;
	private GClipboard clipboard;
	private GZoomManager zoomManager;

	public GMenuBar(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		this.commandManager = GEventStateMananger.getInstance().getCommandManager();
		this.clipboard = GClipboard.getInstance();
		this.zoomManager = GZoomManager.getInstance();

		this.commandManager.addObserver(this);
		this.clipboard.addObserver(this);
		this.zoomManager.addObserver(this);
	}

	@Override
	public void createComponents() {
		fileMenu = new JMenu("파일");
		editMenu = new JMenu("편집");
		viewMenu = new JMenu("보기");
		graphicMenu = new JMenu("그래픽");

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
		zoomResetMenuItem = new JMenuItem("기본 크기(100%)");

		lineWidthMenu = new JMenu("선 굵기");
		lineStyleMenu = new JMenu("선 스타일");
		fontStyleMenu = new JMenu("폰트 스타일");
		fontSizeMenu = new JMenu("폰트 사이즈");

		JMenuItem thinLineItem = new JMenuItem("얇게");
		JMenuItem normalLineItem = new JMenuItem("보통");
		JMenuItem thickLineItem = new JMenuItem("굵게");
		lineWidthMenu.add(thinLineItem);
		lineWidthMenu.add(normalLineItem);
		lineWidthMenu.add(thickLineItem);

		JMenuItem solidLineItem = new JMenuItem("실선");
		JMenuItem dashedLineItem = new JMenuItem("점선");
		JMenuItem dottedLineItem = new JMenuItem("점선(작은)");
		lineStyleMenu.add(solidLineItem);
		lineStyleMenu.add(dashedLineItem);
		lineStyleMenu.add(dottedLineItem);

		JMenuItem plainFontItem = new JMenuItem("일반");
		JMenuItem boldFontItem = new JMenuItem("굵게");
		JMenuItem italicFontItem = new JMenuItem("기울임");
		JMenuItem boldItalicFontItem = new JMenuItem("굵게 기울임");
		fontStyleMenu.add(plainFontItem);
		fontStyleMenu.add(boldFontItem);
		fontStyleMenu.add(italicFontItem);
		fontStyleMenu.add(boldItalicFontItem);

		JMenuItem size8Item = new JMenuItem("8pt");
		JMenuItem size10Item = new JMenuItem("10pt");
		JMenuItem size12Item = new JMenuItem("12pt");
		JMenuItem size14Item = new JMenuItem("14pt");
		JMenuItem size16Item = new JMenuItem("16pt");
		JMenuItem size20Item = new JMenuItem("20pt");
		JMenuItem size24Item = new JMenuItem("24pt");
		JMenuItem size32Item = new JMenuItem("32pt");
		fontSizeMenu.add(size8Item);
		fontSizeMenu.add(size10Item);
		fontSizeMenu.add(size12Item);
		fontSizeMenu.add(size14Item);
		fontSizeMenu.add(size16Item);
		fontSizeMenu.add(size20Item);
		fontSizeMenu.add(size24Item);
		fontSizeMenu.add(size32Item);
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
		viewMenu.add(zoomResetMenuItem);

		graphicMenu.add(lineWidthMenu);
		graphicMenu.add(lineStyleMenu);
		graphicMenu.add(new JSeparator());
		graphicMenu.add(fontStyleMenu);
		graphicMenu.add(fontSizeMenu);

		this.add(fileMenu);
		this.add(editMenu);
		this.add(viewMenu);
		this.add(graphicMenu);
	}

	@Override
	public void setAttributes() {
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
		this.setBackground(new Color(245, 245, 245));

		Font menuFont = new Font("맑은 고딕", Font.PLAIN, 12);

		fileMenu.setFont(menuFont);
		editMenu.setFont(menuFont);
		viewMenu.setFont(menuFont);
		graphicMenu.setFont(menuFont);

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

		zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ActionEvent.CTRL_MASK));
		zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
		zoomResetMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.CTRL_MASK));

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
		setMenuItemFont(zoomResetMenuItem, menuFont);

		lineWidthMenu.setFont(menuFont);
		lineStyleMenu.setFont(menuFont);
		fontStyleMenu.setFont(menuFont);
		fontSizeMenu.setFont(menuFont);

		for (int i = 0; i < lineWidthMenu.getItemCount(); i++)
			setMenuItemFont(lineWidthMenu.getItem(i), menuFont);
		for (int i = 0; i < lineStyleMenu.getItemCount(); i++)
			setMenuItemFont(lineStyleMenu.getItem(i), menuFont);
		for (int i = 0; i < fontStyleMenu.getItemCount(); i++)
			setMenuItemFont(fontStyleMenu.getItem(i), menuFont);
		for (int i = 0; i < fontSizeMenu.getItemCount(); i++)
			setMenuItemFont(fontSizeMenu.getItem(i), menuFont);

		updateUndoRedoState();
		updateClipboardState();
	}

	private void setMenuItemFont(JMenuItem item, Font font) {
		item.setFont(font);
	}

	@Override
	public void update() {
		updateUndoRedoState();
		updateClipboardState();
	}

	public void updateUndoRedoState() {
		boolean canUndo = commandManager.canUndo();
		boolean canRedo = commandManager.canRedo();
		undoMenuItem.setEnabled(canUndo);
		redoMenuItem.setEnabled(canRedo);
	}

	public void updateClipboardState() {
		boolean hasSelection = !GDrawingStateManager.getInstance().getSelectedShapes().isEmpty();
		cutMenuItem.setEnabled(hasSelection);
		copyMenuItem.setEnabled(hasSelection);

		boolean hasClipboardContent = clipboard.hasContents();
		pasteMenuItem.setEnabled(hasClipboardContent);
	}

	@Override
	public void addEventHandler() {
		ActionListener dummyAction = e -> System.out.println("메뉴 선택: " + e.getActionCommand());

		saveMenuItem.addActionListener(e -> {
			File currentFile = GDrawingStateManager.getInstance().getCurrentFile();
			if (currentFile != null) {
				GFileManager.saveToFile(currentFile);
			} else {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter("Graphic Editor Files (*.ged)", "ged"));
				if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					if (!file.getName().toLowerCase().endsWith(".ged")) {
						file = new File(file.getPath() + ".ged");
					}
					GFileManager.saveToFile(file);
					GDrawingStateManager.getInstance().setCurrentFile(file);
				}
			}
		});

		saveAsMenuItem.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("Graphic Editor Files (*.ged)", "ged"));
			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				if (!file.getName().toLowerCase().endsWith(".ged")) {
					file = new File(file.getPath() + ".ged");
				}
				GFileManager.saveToFile(file);
			}
		});

		saveAsImageMenuItem.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("PNG Images (*.png)", "png"));
			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				if (!file.getName().toLowerCase().endsWith(".png")) {
					file = new File(file.getPath() + ".png");
				}
				GFileManager.saveAsImage(file, drawingPanel);
			}
		});

		loadImageMenuItem.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("이미지 파일", "jpg", "jpeg", "png", "bmp", "gif");
			chooser.setFileFilter(filter);
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				BufferedImage image = GFileManager.loadImage(file);
				if (image != null) {
					drawingPanel.setBackgroundImage(image);
				}
			}
		});

		openMenuItem.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("Graphic Editor Files (*.ged)", "ged"));
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				GFileManager.loadFromFile(file);
			}
		});

		newMenuItem.addActionListener(e -> {
			drawingPanel.setBackgroundImage(null);
			GDrawingStateManager.getInstance().setShapes(java.util.Collections.emptyList());
			GDrawingStateManager.getInstance().setCurrentFile(null);
		});

		exitMenuItem.addActionListener(e -> System.exit(0));

		undoMenuItem.addActionListener(e -> {
			if (commandManager.canUndo()) {
				commandManager.undo();
				updateUndoRedoState();
			}
		});

		redoMenuItem.addActionListener(e -> {
			if (commandManager.canRedo()) {
				commandManager.redo();
				updateUndoRedoState();
			}
		});

		cutMenuItem.addActionListener(e -> commandManager.executeAndStore(GMode.CUT));
		copyMenuItem.addActionListener(e -> commandManager.execute(GMode.COPY));
		pasteMenuItem.addActionListener(e -> commandManager.executeAndStore(GMode.PASTE));

		zoomInMenuItem.addActionListener(e -> commandManager.executeAndStore(GZoomType.ZOOM_IN));
		zoomOutMenuItem.addActionListener(e -> commandManager.executeAndStore(GZoomType.ZOOM_OUT));
		zoomResetMenuItem.addActionListener(e -> commandManager.executeAndStore(GZoomType.ZOOM_RESET));

		for (int i = 0; i < lineWidthMenu.getItemCount(); i++)
			lineWidthMenu.getItem(i).addActionListener(dummyAction);
		for (int i = 0; i < lineStyleMenu.getItemCount(); i++)
			lineStyleMenu.getItem(i).addActionListener(dummyAction);
		for (int i = 0; i < fontStyleMenu.getItemCount(); i++)
			fontStyleMenu.getItem(i).addActionListener(dummyAction);
		for (int i = 0; i < fontSizeMenu.getItemCount(); i++)
			fontSizeMenu.getItem(i).addActionListener(dummyAction);
	}
}
