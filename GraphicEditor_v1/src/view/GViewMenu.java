package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import command.GCommandManager;
import type.GZoomType;

public class GViewMenu extends JMenu implements GContainerInterface {
	private static final long serialVersionUID = 1L;

	private JMenuItem zoomInMenuItem;
	private JMenuItem zoomOutMenuItem;
	private JMenuItem zoomResetMenuItem;

	private GCommandManager commandManager;

	public GViewMenu(GCommandManager commandManager) {
		super("보기");
		this.commandManager = commandManager;
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
		zoomInMenuItem = new JMenuItem("확대");
		zoomOutMenuItem = new JMenuItem("축소");
		zoomResetMenuItem = new JMenuItem("기본 크기(100%)");
	}

	@Override
	public void arrangeComponents() {
		this.add(zoomInMenuItem);
		this.add(zoomOutMenuItem);
		this.add(zoomResetMenuItem);
	}

	@Override
	public void setAttributes() {
		Font menuFont = new Font("맑은 고딕", Font.PLAIN, 12);

		zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ActionEvent.CTRL_MASK));
		zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
		zoomResetMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.CTRL_MASK));

		setMenuItemFont(zoomInMenuItem, menuFont);
		setMenuItemFont(zoomOutMenuItem, menuFont);
		setMenuItemFont(zoomResetMenuItem, menuFont);
	}

	private void setMenuItemFont(JMenuItem item, Font font) {
		item.setFont(font);
	}

	@Override
	public void addEventHandler() {
		zoomInMenuItem.addActionListener(e -> commandManager.executeAndStore(GZoomType.ZOOM_IN));
		zoomOutMenuItem.addActionListener(e -> commandManager.executeAndStore(GZoomType.ZOOM_OUT));
		zoomResetMenuItem.addActionListener(e -> commandManager.executeAndStore(GZoomType.ZOOM_RESET));
	}
}