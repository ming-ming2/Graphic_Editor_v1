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
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import shapes.GShape;
import state.GDrawingStateManager;
import state.GEventStateMananger;
import state.GObserver;
import state.GZoomManager;

public class GDrawingPanel extends JPanel implements GContainerInterface, GObserver {
	private static final long serialVersionUID = 1L;

	private GDrawingStateManager drawingStateManager;
	private GEventStateMananger eventStateManager;
	private GZoomManager zoomManager;
	private boolean isDragging = false;
	private BufferedImage backgroundImage = null;

	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 600;

	public GDrawingPanel() {
		this.drawingStateManager = GDrawingStateManager.getInstance();
		this.eventStateManager = GEventStateMananger.getInstance();
		this.zoomManager = GZoomManager.getInstance();

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
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					// 화면 좌표를 모델 좌표로 변환
					Point modelPoint = zoomManager.viewToModel(e.getPoint());

					// 변환된 좌표로 새 이벤트 생성
					MouseEvent modelEvent = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(),
							modelPoint.x, modelPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());

					eventStateManager.setCurrentPoint(modelPoint);
					eventStateManager.getCurrentMouseEventHandler().mousePressed(modelEvent);
					isDragging = true;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && isDragging) {
					// 화면 좌표를 모델 좌표로 변환
					Point modelPoint = zoomManager.viewToModel(e.getPoint());

					// 변환된 좌표로 새 이벤트 생성
					MouseEvent modelEvent = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(),
							modelPoint.x, modelPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());

					eventStateManager.setCurrentPoint(modelPoint);
					eventStateManager.getCurrentMouseEventHandler().mouseReleased(modelEvent);
					isDragging = false;
				}
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isDragging) {
					// 화면 좌표를 모델 좌표로 변환
					Point modelPoint = zoomManager.viewToModel(e.getPoint());

					// 변환된 좌표로 새 이벤트 생성
					MouseEvent modelEvent = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(),
							modelPoint.x, modelPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());

					eventStateManager.setCurrentPoint(modelPoint);
					eventStateManager.getCurrentMouseEventHandler().mouseDragged(modelEvent);
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// 화면 좌표를 모델 좌표로 변환
				Point modelPoint = zoomManager.viewToModel(e.getPoint());
				eventStateManager.setCurrentPoint(modelPoint);

				// 제어점 위에 있는지 확인하고 커서 변경
				for (GShape shape : drawingStateManager.getSelectedShapes()) {
					GShape.ControlPoint cp = shape.getControlPointAt(modelPoint);
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
							setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
							break;
						default:
							setCursor(Cursor.getDefaultCursor());
						}
						return;
					}
				}

				// 도형 위에 있는지 확인
				GShape shapeUnderMouse = drawingStateManager.findShapeAt(modelPoint);
				if (shapeUnderMouse != null) {
					setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				} else {
					// 아무것도 없으면 기본 커서
					setCursor(Cursor.getDefaultCursor());
				}
			}
		});

		// 마우스 휠로 확대/축소
		this.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// Ctrl 키가 눌려 있을 때만 확대/축소
				if (e.isControlDown()) {
					if (e.getWheelRotation() < 0) {
						// 위로 스크롤 (확대)
						zoomManager.zoomIn();
					} else {
						// 아래로 스크롤 (축소)
						zoomManager.zoomOut();
					}

					// 컴포넌트 크기 업데이트
					revalidate();

					// 스크롤 위치 조정 (현재 마우스 위치 중심으로)
					adjustScrollPosition(e.getPoint());

					e.consume(); // 이벤트 소비 (부모 스크롤에 전파 방지)
				}
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		// 안티앨리어싱 설정
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		// 확대/축소 적용
		double zoomFactor = zoomManager.getZoomFactor();
		g2d.scale(zoomFactor, zoomFactor);

		if (backgroundImage != null) {
			g2d.drawImage(backgroundImage, 0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, null);
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

	// 확대/축소 시 스크롤 위치 조정 (마우스 위치 중심으로)
	private void adjustScrollPosition(Point mousePoint) {
		// 부모 JScrollPane을 찾아서 스크롤 위치 조정
		if (getParent() != null && getParent().getParent() instanceof javax.swing.JScrollPane) {
			javax.swing.JScrollPane scrollPane = (javax.swing.JScrollPane) getParent().getParent();

			// 현재 뷰포트의 위치
			Rectangle viewRect = scrollPane.getViewport().getViewRect();

			// 마우스 위치를 중심으로 새 위치 계산
			int newX = mousePoint.x - viewRect.width / 2;
			int newY = mousePoint.y - viewRect.height / 2;

			// 위치 조정
			scrollPane.getViewport().setViewPosition(new Point(newX, newY));
		}
	}

	@Override
	public void update() {
		// 컴포넌트 크기 업데이트 (줌 변경시)
		revalidate();
		repaint();
	}
}