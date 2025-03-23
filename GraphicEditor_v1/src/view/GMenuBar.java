package view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JMenuBar;

import command.GCommandManager;
import state.GClipboard;
import state.GEventStateMananger;
import state.GObserver;
import state.GZoomManager;

public class GMenuBar extends JMenuBar implements GContainerInterface, GObserver {
	private static final long serialVersionUID = 1L;

	private GFileMenu fileMenu;
	private GEditMenu editMenu;
	private GViewMenu viewMenu;
	private GGraphicMenu graphicMenu;

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
		fileMenu = new GFileMenu(drawingPanel);
		editMenu = new GEditMenu(commandManager);
		viewMenu = new GViewMenu(commandManager);
		graphicMenu = new GGraphicMenu();
	}

	@Override
	public void arrangeComponents() {
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
	}

	@Override
	public void addEventHandler() {
		fileMenu.initialize();
		editMenu.initialize();
		viewMenu.initialize();
		graphicMenu.initialize();
	}

	@Override
	public void update() {
		editMenu.updateUndoRedoState();
		editMenu.updateClipboardState();
	}
}