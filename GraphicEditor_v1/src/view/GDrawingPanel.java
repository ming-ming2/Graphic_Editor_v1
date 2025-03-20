package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import shapes.GShape;
import state.GDrawingStateManager;
import state.GObserver;

public class GDrawingPanel extends JPanel implements GContainerInterface, GObserver {
	private static final long serialVersionUID = 1L;

	private GDrawingStateManager drawingStateManager;
	private boolean isDragging = false;

	public GDrawingPanel() {
		this.drawingStateManager = GDrawingStateManager.getInstance();
		GDrawingStateManager.getInstance().addObserver(this);
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
					drawingStateManager.getCurrentMouseEventHandler().mousePressed(e);
					isDragging = true;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && isDragging) {
					drawingStateManager.getCurrentMouseEventHandler().mouseReleased(e);
					isDragging = false;
					repaint();
				}
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isDragging) {
					drawingStateManager.getCurrentMouseEventHandler().mouseDragged(e);
				}
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// 저장된 모든 도형 그리기
		for (GShape shape : drawingStateManager.getShapes()) {
			shape.draw(g);
		}

		// 미리보기 도형 그리기
		if (drawingStateManager.getPreviewShape() != null) {
			drawingStateManager.getPreviewShape().draw(g);
		}
	}

	@Override
	public void update() {
		repaint();
	}

}