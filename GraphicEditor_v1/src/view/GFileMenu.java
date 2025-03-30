package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import file.GFileManager;
import state.GDrawingStateManager;

public class GFileMenu extends JMenu implements GContainerInterface {
	private static final long serialVersionUID = 1L;

	private JMenuItem newMenuItem;
	private JMenuItem openMenuItem;
	private JMenuItem loadImageMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem saveAsImageMenuItem;
	private JMenuItem exitMenuItem;

	private GDrawingPanel drawingPanel;

	public GFileMenu(GDrawingPanel drawingPanel) {
		super("파일");
		this.drawingPanel = drawingPanel;
		this.createComponents();
		this.setAttributes();
		this.arrangeComponents();
		this.addEventHandler();
	}

	@Override
	public void initialize() {

	}

	@Override
	public void createComponents() {
		newMenuItem = new JMenuItem("새로 만들기");
		openMenuItem = new JMenuItem("열기");
		loadImageMenuItem = new JMenuItem("배경 이미지 불러오기");
		saveMenuItem = new JMenuItem("저장");
		saveAsMenuItem = new JMenuItem("다른 이름으로 저장");
		saveAsImageMenuItem = new JMenuItem("이미지로 저장");
		exitMenuItem = new JMenuItem("끝내기");
	}

	@Override
	public void arrangeComponents() {
		this.add(newMenuItem);
		this.add(openMenuItem);
		this.add(loadImageMenuItem);
		this.add(new JSeparator());
		this.add(saveMenuItem);
		this.add(saveAsMenuItem);
		this.add(saveAsImageMenuItem);
		this.add(new JSeparator());
		this.add(exitMenuItem);
	}

	@Override
	public void setAttributes() {
		Font menuFont = new Font("맑은 고딕", Font.PLAIN, 12);

		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveAsMenuItem
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		setMenuItemFont(newMenuItem, menuFont);
		setMenuItemFont(openMenuItem, menuFont);
		setMenuItemFont(loadImageMenuItem, menuFont);
		setMenuItemFont(saveMenuItem, menuFont);
		setMenuItemFont(saveAsMenuItem, menuFont);
		setMenuItemFont(saveAsImageMenuItem, menuFont);
		setMenuItemFont(exitMenuItem, menuFont);
	}

	private void setMenuItemFont(JMenuItem item, Font font) {
		item.setFont(font);
	}

	@Override
	public void addEventHandler() {
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
	}
}