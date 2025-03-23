package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import command.GCommandManager;
import state.GClipboard;
import state.GDrawingStateManager;
import type.GMode;

public class GEditMenu extends JMenu implements GContainerInterface {
	private static final long serialVersionUID = 1L;

	private JMenuItem cutMenuItem;
	private JMenuItem copyMenuItem;
	private JMenuItem pasteMenuItem;
	private JMenuItem undoMenuItem;
	private JMenuItem redoMenuItem;

	private GCommandManager commandManager;

	public GEditMenu(GCommandManager commandManager) {
		super("편집");
		this.commandManager = commandManager;
	}

	@Override
	public void createComponents() {
		cutMenuItem = new JMenuItem("자르기");
		copyMenuItem = new JMenuItem("복사");
		pasteMenuItem = new JMenuItem("붙여넣기");
		undoMenuItem = new JMenuItem("실행 취소");
		redoMenuItem = new JMenuItem("다시 실행");
	}

	@Override
	public void arrangeComponents() {
		this.add(undoMenuItem);
		this.add(redoMenuItem);
		this.add(new JSeparator());
		this.add(cutMenuItem);
		this.add(copyMenuItem);
		this.add(pasteMenuItem);
	}

	@Override
	public void setAttributes() {
		Font menuFont = new Font("맑은 고딕", Font.PLAIN, 12);

		cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));

		setMenuItemFont(cutMenuItem, menuFont);
		setMenuItemFont(copyMenuItem, menuFont);
		setMenuItemFont(pasteMenuItem, menuFont);
		setMenuItemFont(undoMenuItem, menuFont);
		setMenuItemFont(redoMenuItem, menuFont);

		updateUndoRedoState();
		updateClipboardState();
	}

	private void setMenuItemFont(JMenuItem item, Font font) {
		item.setFont(font);
	}

	@Override
	public void addEventHandler() {
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

		boolean hasClipboardContent = GClipboard.getInstance().hasContents();
		pasteMenuItem.setEnabled(hasClipboardContent);
	}
}