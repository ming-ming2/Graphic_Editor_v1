package view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

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
	private BufferedImage backgroundImage = null;

	public GDrawingPanel() {
		this.drawingStateManager = GDrawingStateManager.getInstance();
		this.eventStateManager = GEventStateMananger.getInstance();
		drawingStateManager.addObserver(this);
		eventStateManager.addObserver(this);
	}

	public void setBackgroundImage(BufferedImage image) {
		this.backgroundImage = image;
		repaint();
	}

	public BufferedImage getBackgroundImage() {
		return backgroundImage;
	}

	@Override
	public void createComponents() {
	}

	@Override
	public void arrangeComponents() {
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
					eventStateManager.getCurrentMouseEventHandler().mousePressed(e);
					isDragging = true;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && isDragging) {
					eventStateManager.setCurrentPoint(e.getPoint());
					eventStateManager.getCurrentMouseEventHandler().mouseReleased(e);
					isDragging = false;
				}
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isDragging) {
					eventStateManager.setCurrentPoint(e.getPoint());
					eventStateManager.getCurrentMouseEventHandler().mouseDragged(e);
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				eventStateManager.setCurrentPoint(e.getPoint());

				// 제어점 위에 있는지 확인하고 커서 변경
				Point point = e.getPoint();
				for (GShape shape : drawingStateManager.getSelectedShapes()) {
					GShape.ControlPoint cp = shape.getControlPointAt(point);
					if (cp != GShape.ControlPoint.NONE) {
						// 제어점에 따라 적절한 커서 설정
						switch (cp) {
						case TOP_LEFT:
						case BOTTOM_RIGHT:
							setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
							break;
						case TOP_RIGHT:
						case BOTTOM_LEFT:
							setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
							break;
						case TOP:
						case BOTTOM:
							setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
							break;
						case LEFT:
						case RIGHT:
							setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
							break;
						case ROTATE:
							// 회전 커서 (자바에는 완벽한 회전 커서가 없어 유사한 것 사용)
							setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
							break;
						default:
							setCursor(Cursor.getDefaultCursor());
						}
						return;
					}
				}

				// 도형 위에 있는지 확인
				GShape shapeUnderMouse = drawingStateManager.findShapeAt(point);
				if (shapeUnderMouse != null) {
					setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				} else {
					// 아무것도 없으면 기본 커서
					setCursor(Cursor.getDefaultCursor());
				}
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		if (backgroundImage != null) {
			g2d.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
		}

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