package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import command.GCommandManager;

public class GMainFrame extends JFrame implements GContainerInterface {
	private static final long serialVersionUID = 1L;
	private GCommandManager commandManager;

	private GDrawingPanel drawingPanel;
	private GToolsBar toolsBar;

	public GMainFrame(GCommandManager commandManager) {
		this.commandManager = commandManager;
		initialize();
	}

	@Override
	public void createComponents() {
		drawingPanel = new GDrawingPanel(commandManager);
		toolsBar = new GToolsBar(drawingPanel);
	}

	@Override
	public void arrangeComponents() {
		setLayout(new BorderLayout());
		add(toolsBar, BorderLayout.NORTH);
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(drawingPanel, BorderLayout.CENTER);
		add(centerPanel, BorderLayout.CENTER);
	}

	@Override
	public void setProperties() {
		this.setTitle("Graphic Editor_V1");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(1600, 900);
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	@Override
	public void addEventHandler() {
		//

	}

}