package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import state.GZoomManager;

public class GMainFrame extends JFrame implements GContainerInterface {
	private static final long serialVersionUID = 1L;

	private GDrawingPanel drawingPanel;
	private GToolBar toolBar;
	private GMenuBar menuBar;
	private GStatusBar statusBar;
	private JScrollPane scrollPane;
	private List<GContainerInterface> components = new ArrayList<>();

	public GMainFrame() {

	}

	@Override
	public void createComponents() {
		this.drawingPanel = new GDrawingPanel();
		this.toolBar = new GToolBar();
		this.menuBar = new GMenuBar(drawingPanel);
		this.statusBar = new GStatusBar();
		this.scrollPane = new JScrollPane(drawingPanel);
		this.components.add(drawingPanel);
		this.components.add(toolBar);
		this.components.add(menuBar);
		this.components.add(statusBar);
	}

	@Override
	public void arrangeComponents() {
		this.setLayout(new BorderLayout());
		this.setJMenuBar(menuBar);
		this.add(toolBar, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		this.add(centerPanel, BorderLayout.CENTER);

		this.add(statusBar, BorderLayout.SOUTH);

		GZoomManager.getInstance().addObserver(statusBar);
	}

	@Override
	public void setAttributes() {
		this.setTitle("Graphic Editor_V1");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int) (dim.width / 2.), dim.height * 2 / 3);
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
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