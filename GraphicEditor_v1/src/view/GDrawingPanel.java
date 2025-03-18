package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import command.GCommandManager;
import eventhandler.GMouseEventHandler;
import eventhandler.GMouseEventHandlerRepository;
import global.GMode;
import global.GShapeType;
import shapes.GShape;

public class GDrawingPanel extends JPanel implements GContainerInterface {
	private static final long serialVersionUID = 1L;

	private GCommandManager commandManager;
	private GMouseEventHandlerRepository mouseEventHandlers;;
	private GMouseEventHandler currentMouseEventHandler;
	// 상태 필드, 나중에 별도의 상태관리 클래스로 분리
	private GMode currentMode;
	private GShapeType currentShapeType;

	private boolean isDragging = false;

	private List<GShape> shapes = new ArrayList<>(); // 저장된 도형들
	private GShape previewShape = null; // 드래그 중 미리보기 도형

	public GDrawingPanel(GCommandManager commandManager) {
		this.commandManager = commandManager;
		this.mouseEventHandlers = new GMouseEventHandlerRepository(commandManager, this);
		initialize();
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
	public void setProperties() {
		this.setPreferredSize(new java.awt.Dimension(800, 600));
		this.setBackground(Color.WHITE);
		this.setBorder(new LineBorder(Color.LIGHT_GRAY));

		this.currentMode = GMode.DEFAULT; // 기본 모드
		this.currentShapeType = null; // 기본 도형 타입
		this.currentMouseEventHandler = mouseEventHandlers.get(GMode.DEFAULT); // 기본 핸들러
	}

	@Override
	public void addEventHandler() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					currentMouseEventHandler.mousePressed(e);
					isDragging = true;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && isDragging) {
					currentMouseEventHandler.mouseReleased(e);
					isDragging = false;
					repaint();
				}
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isDragging) {
					currentMouseEventHandler.mouseDragged(e);
				}
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// 저장된 모든 도형 그리기
		for (GShape shape : shapes) {
			shape.draw(g);
		}

		// 미리보기 도형 그리기
		if (previewShape != null) {
			previewShape.draw(g);
		}
	}

	// 최종 도형 추가 메서드
	public void addShape(GShape shape) {
		if (shape != null) {
			shapes.add(shape);
			repaint();
		}
	}

	// 미리보기 도형 설정 메서드
	public void setPreviewShape(GShape shape) {
		this.previewShape = shape;
		repaint();
	}

	// 현재 모드 설정 메서드
	public void setCurrentMode(GMode mode) {
		this.currentMode = mode;
		this.currentMouseEventHandler = this.mouseEventHandlers.get(mode);
	}

	// 현재 도형 타입 설정 메서드
	public void setCurrentShapeType(GShapeType shapeType) {
		this.currentShapeType = shapeType;
	}

	// 현재 도형 타입 반환 메서드
	public GShapeType getCurrentShapeType() {
		return currentShapeType;
	}
}