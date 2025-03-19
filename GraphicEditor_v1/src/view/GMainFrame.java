package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import command.GCommandManager;

public class GMainFrame extends JFrame implements GContainerInterface {
	private static final long serialVersionUID = 1L;
	private GCommandManager commandManager;

	private GDrawingPanel drawingPanel;
	private GToolBar toolBar;
	private GMenuBar menuBar;
	private List<GContainerInterface> components = new ArrayList<>();

	public GMainFrame(GCommandManager commandManager) {
		this.commandManager = commandManager;
	}

	@Override
	public void createComponents() {
		drawingPanel = new GDrawingPanel(commandManager);
		toolBar = new GToolBar(drawingPanel);
		menuBar = new GMenuBar();
		components.add(drawingPanel);
		components.add(toolBar);
		components.add(menuBar);
	}

	@Override
	public void arrangeComponents() {
		setLayout(new BorderLayout());
		add(toolBar, BorderLayout.NORTH);
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(drawingPanel, BorderLayout.CENTER);
		add(centerPanel, BorderLayout.CENTER);
	}

	@Override
	public void setAttributes() {
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

	@Override
	public void initialize() {
		createComponents();
		arrangeComponents();
		setAttributes();
		addEventHandler();
		for (GContainerInterface component : components) {
			component.initialize();
		}
	}

}