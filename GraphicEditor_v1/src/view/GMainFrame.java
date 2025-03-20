package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GMainFrame extends JFrame implements GContainerInterface {
	private static final long serialVersionUID = 1L;

	private GDrawingPanel drawingPanel;
	private GToolBar toolBar;
	private GMenuBar menuBar;
	private List<GContainerInterface> components = new ArrayList<>();

	public GMainFrame() {

	}

	@Override
	public void createComponents() {
		this.drawingPanel = new GDrawingPanel();
		this.toolBar = new GToolBar();
		this.menuBar = new GMenuBar();
		this.components.add(drawingPanel);
		this.components.add(toolBar);
		this.components.add(menuBar);
	}

	@Override
	public void arrangeComponents() {
		this.setLayout(new BorderLayout());
		this.setJMenuBar(menuBar);
		this.add(toolBar, BorderLayout.NORTH);
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(drawingPanel, BorderLayout.CENTER);
		this.add(centerPanel, BorderLayout.CENTER);
	}

	@Override
	public void setAttributes() {
		this.setTitle("Graphic Editor_V1");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int) (dim.width / 2.), dim.height / 2);
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