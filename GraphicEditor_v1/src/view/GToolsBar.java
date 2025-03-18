package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import global.GMode;
import global.GShapeType;

public class GToolsBar extends JToolBar implements GContainerInterface {
	private static final long serialVersionUID = 1L;

	private GDrawingPanel drawingPanel;

	private JButton rectangleButton;

	public GToolsBar(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		initialize();
	}

	@Override
	public void createComponents() {
		JPanel shapesSection = createShapesSection();
		add(shapesSection);
	}

	@Override
	public void arrangeComponents() {
		//
	}

	@Override
	public void setProperties() {
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

		setPreferredSize(new Dimension(0, 60));
		setBackground(new Color(240, 240, 240));
	}

	@Override
	public void addEventHandler() {
		//

	}

	private JPanel createShapesSection() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
		panel.setOpaque(false);

		JLabel sectionLabel = new JLabel("도형: ");
		panel.add(sectionLabel);

		rectangleButton = new JButton("직사각형");
		rectangleButton.setPreferredSize(new Dimension(100, 30));
		rectangleButton.addActionListener(e -> {
			drawingPanel.setCurrentShapeType(GShapeType.Rectangle);
			drawingPanel.setCurrentMode(GMode.SHAPE);
		});
		panel.add(rectangleButton);

		return panel;
	}

}