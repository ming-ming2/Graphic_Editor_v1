package view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import shapes.GShape;
import state.GDrawingStateManager;
import state.GEventStateMananger;
import state.GObserver;

public class GDrawingPanel extends JPanel implements GContainerInterface, GObserver {
	private static final long serialVersionUID = 1L;

	private GDrawingStateManager drawingStateManager;
	private GEventStateMananger eventStateManager;
	private boolean isDragging = false;

	public GDrawingPanel() {
		this.drawingStateManager = GDrawingStateManager.getInstance();
		this.eventStateManager = GEventStateMananger.getInstance();
		drawingStateManager.addObserver(this);
		eventStateManager.addObserver(this);
	}

	@Override
	public void createComponents() {
		//
	}

	@Override
	public void arrangeComponents() {
		//
	}

	@Override
	public void setAttributes() {
		this.setPreferredSize(new java.awt.Dimension(800, 600));
		this.setBackground(Color.WHITE);
		this.setBorder(new LineBorder(Color.LIGHT_GRAY));
	}

	@Override
	public void addEventHandler() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					eventStateManager.setCurrentPoint(e.getPoint());
					drawingStateManager.getCurrentMouseEventHandler().mousePressed(e);
					isDragging = true;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && isDragging) {
					eventStateManager.setCurrentPoint(e.getPoint());
					drawingStateManager.getCurrentMouseEventHandler().mouseReleased(e);
					isDragging = false;
				}
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isDragging) {
					eventStateManager.setCurrentPoint(e.getPoint());
					drawingStateManager.getCurrentMouseEventHandler().mouseDragged(e);
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				eventStateManager.setCurrentPoint(e.getPoint());
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		for (GShape shape : drawingStateManager.getShapes()) {
			shape.draw(g);
		}

		if (drawingStateManager.getPreviewShape() != null) {
			drawingStateManager.getPreviewShape().draw(g);
		}

		drawSelectionArea(g2d);
	}

	private void drawSelectionArea(Graphics2D g2d) {
		Rectangle selectionArea = drawingStateManager.getSelectionArea();
		if (selectionArea != null && selectionArea.width > 0 && selectionArea.height > 0) {
			Composite originalComposite = g2d.getComposite();

			g2d.setColor(new Color(100, 150, 255, 50));
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
			g2d.fill(selectionArea);

			g2d.setComposite(originalComposite);
			g2d.setColor(new Color(70, 130, 230));
			g2d.draw(selectionArea);
		}
	}

	@Override
	public void update() {
		repaint();
	}
}