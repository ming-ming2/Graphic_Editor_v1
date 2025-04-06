package view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import command.GCommand;
import command.GShapeCommand;
import shapes.GShape;
import state.GDrawingStateManager;
import state.GEventStateMananger;
import state.GObserver;
import state.GZoomManager;
import type.GMode;

public class GDrawingPanel extends JPanel implements GContainerInterface, GObserver {
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 600;

	private GDrawingStateManager drawingStateManager;
	private GEventStateMananger eventStateManager;
	private GZoomManager zoomManager;

	private BufferedImage backgroundImage = null;
	private MouseHandler mouseHandler;

	public GDrawingPanel() {
		this.drawingStateManager = GDrawingStateManager.getInstance();
		this.eventStateManager = GEventStateMananger.getInstance();
		this.zoomManager = GZoomManager.getInstance();
		this.mouseHandler = new MouseHandler();

		registerObservers();
		createComponents();
		setAttributes();
		arrangeComponents();
		addEventHandler();
	}

	private void registerObservers() {
		drawingStateManager.addObserver(this);
		eventStateManager.addObserver(this);
		zoomManager.addObserver(this);
	}

	public void setBackgroundImage(BufferedImage image) {
		this.backgroundImage = image;
		repaint();
	}

	public BufferedImage getBackgroundImage() {
		return backgroundImage;
	}

	@Override
	public void initialize() {
	}

	@Override
	public void createComponents() {
	}

	@Override
	public void arrangeComponents() {
	}

	@Override
	public void setAttributes() {
		this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		this.setBackground(Color.WHITE);
		this.setBorder(new LineBorder(Color.LIGHT_GRAY));
	}

	@Override
	public Dimension getPreferredSize() {
		double zoomFactor = zoomManager.getZoomFactor();
		return new Dimension((int) (DEFAULT_WIDTH * zoomFactor), (int) (DEFAULT_HEIGHT * zoomFactor));
	}

	@Override
	public void addEventHandler() {
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);
		this.addMouseWheelListener(mouseHandler);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		double zoomFactor = zoomManager.getZoomFactor();
		g2d.scale(zoomFactor, zoomFactor);

		if (backgroundImage != null) {
			g2d.drawImage(backgroundImage, 0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, null);
		}

		for (GShape shape : drawingStateManager.getShapes()) {
			shape.draw(g);
		}

		if (!drawingStateManager.isMouseReleased() && drawingStateManager.getCurrentMode() == GMode.SHAPE) {
			Graphics2D g2dXor = (Graphics2D) g2d.create();
			g2dXor.setXORMode(Color.LIGHT_GRAY);

			GCommand currentCommand = eventStateManager.getCommandManager().getActiveCommand(GMode.SHAPE);

			if (currentCommand instanceof GShapeCommand) {
				GShape previewShape = ((GShapeCommand) currentCommand).getLastPreviewShape();
				if (previewShape != null) {
					previewShape.draw(g2dXor);
				}
			}
			g2dXor.dispose();
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

	private void adjustScrollPosition(Point mousePoint) {
		if (getParent() != null && getParent().getParent() instanceof javax.swing.JScrollPane scrollPane) {
			Rectangle viewRect = scrollPane.getViewport().getViewRect();
			int newX = mousePoint.x - viewRect.width / 2;
			int newY = mousePoint.y - viewRect.height / 2;
			scrollPane.getViewport().setViewPosition(new Point(newX, newY));
		}
	}

	@Override
	public void update() {
		revalidate();
		repaint();
	}

	private class MouseHandler extends MouseAdapter {
		private boolean isDragging = false;

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				Point modelPoint = zoomManager.viewToModel(e.getPoint());
				MouseEvent modelEvent = createModelEvent(e, modelPoint);

				eventStateManager.setCurrentPoint(modelPoint);
				eventStateManager.getCurrentMouseEventHandler().mousePressed(modelEvent);
				isDragging = true;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 && isDragging) {
				Point modelPoint = zoomManager.viewToModel(e.getPoint());
				MouseEvent modelEvent = createModelEvent(e, modelPoint);

				eventStateManager.setCurrentPoint(modelPoint);
				eventStateManager.getCurrentMouseEventHandler().mouseReleased(modelEvent);
				isDragging = false;
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (isDragging) {
				Point modelPoint = zoomManager.viewToModel(e.getPoint());
				MouseEvent modelEvent = createModelEvent(e, modelPoint);

				eventStateManager.setCurrentPoint(modelPoint);
				eventStateManager.getCurrentMouseEventHandler().mouseDragged(modelEvent);
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Point modelPoint = zoomManager.viewToModel(e.getPoint());
			eventStateManager.setCurrentPoint(modelPoint);
			updateCursor(modelPoint);
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.isControlDown()) {
				if (e.getWheelRotation() < 0) {
					zoomManager.zoomIn();
				} else {
					zoomManager.zoomOut();
				}
				revalidate();
				adjustScrollPosition(e.getPoint());
				e.consume();
			}
		}

		private MouseEvent createModelEvent(MouseEvent e, Point modelPoint) {
			return new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), modelPoint.x,
					modelPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
		}

		private void updateCursor(Point modelPoint) {
			for (GShape shape : drawingStateManager.getSelectedShapes()) {
				GShape.ControlPoint cp = shape.getControlPointAt(modelPoint);
				if (cp != GShape.ControlPoint.NONE) {
					setCursorForControlPoint(cp);
					return;
				}
			}

			GShape shapeUnderMouse = drawingStateManager.findShapeAt(modelPoint);
			if (shapeUnderMouse != null) {
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			} else {
				setCursor(Cursor.getDefaultCursor());
			}
		}

		private void setCursorForControlPoint(GShape.ControlPoint cp) {
			switch (cp) {
			case TOP_LEFT, BOTTOM_RIGHT:
				setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
				break;
			case TOP_RIGHT, BOTTOM_LEFT:
				setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
				break;
			case TOP, BOTTOM:
				setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
				break;
			case LEFT, RIGHT:
				setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
				break;
			case ROTATE:
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				break;
			default:
				setCursor(Cursor.getDefaultCursor());
				break;
			}
		}
	}
}