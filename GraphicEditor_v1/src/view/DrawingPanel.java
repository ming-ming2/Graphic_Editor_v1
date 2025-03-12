package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import command.CommandContext;
import command.CommandManager;
import global.Context;
import global.ContextKey;
import global.ShapeType;
import shapes.Shape;

public class DrawingPanel extends JPanel implements ContainerInterface {
    private static final long serialVersionUID = 1L;
    
    private CommandManager commandManager;
    private Context currentContext;
    private ShapeType currentShapeType;
    
    private MouseEvent startMouseEvent;
    private Point currentPoint;
    private boolean isDragging = false;
    
    private List<Shape> shapes = new ArrayList<>(); // 저장된 도형들
    private Shape previewShape = null;             // 드래그 중 미리보기 도형
    
    public DrawingPanel(CommandManager commandManager) {
        this.commandManager = commandManager;
        createComponents();
        arrangeComponents();
    }
    
    @Override
    public void initialize() {
        this.setPreferredSize(new java.awt.Dimension(800, 600));
        this.currentContext = Context.SHAPE; // 기본 컨텍스트
        this.currentShapeType = ShapeType.Rectangle; // 기본 도형 타입
        this.setBackground(Color.WHITE);
        this.setBorder(new LineBorder(Color.LIGHT_GRAY));
        initializeMouseListeners();
    }
    
    @Override
    public void createComponents() {
        // DrawingPanel에는 별도의 컴포넌트 생성이 필요 없음
    }
    
    @Override
    public void arrangeComponents() {
        // DrawingPanel에는 별도의 컴포넌트 배치가 필요 없음
    }
    
    private void initializeMouseListeners() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startMouseEvent = e;
                isDragging = true;
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDragging && currentContext != null) {
                    CommandContext cmdContext = new CommandContext();
                    List<MouseEvent> events = new ArrayList<>();
                    events.add(startMouseEvent);
                    events.add(e);
                    cmdContext.put(ContextKey.MOUSE_EVENTS, events);
                    cmdContext.put(ContextKey.SHAPE_TYPE, currentShapeType);
                    // DrawingPanel.this를 전달하여 올바른 참조 사용
                    cmdContext.put(ContextKey.DRAWING_PANEL, DrawingPanel.this);
                    
                    commandManager.execute(currentContext, cmdContext);
                    
                    isDragging = false;
                    // 최종 도형 추가 후 미리보기 도형은 제거
                    previewShape = null;
                    repaint();
                }
            }
        });
        
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    currentPoint = e.getPoint();
                    CommandContext previewContext = new CommandContext();
                    List<MouseEvent> previewEvents = new ArrayList<>();
                    previewEvents.add(startMouseEvent);
                    previewEvents.add(e);
                    previewContext.put(ContextKey.MOUSE_EVENTS, previewEvents);
                    previewContext.put(ContextKey.SHAPE_TYPE, currentShapeType);
                    previewContext.put(ContextKey.DRAWING_PANEL, DrawingPanel.this);
                    
                    // 미리보기 도형 업데이트
                    commandManager.execute(currentContext, previewContext);
                }
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 저장된 모든 도형 그리기
        for (Shape shape : shapes) {
            shape.draw(g);
        }
        
        // 미리보기 도형 그리기 (존재하는 경우)
        if (previewShape != null) {
            previewShape.draw(g);
        }
    }
    
    // 최종 도형 추가 메서드
    public void addShape(Shape shape) {
        if (shape != null) {
            shapes.add(shape);
            repaint();
        }
    }
    
    // 미리보기 도형 설정 메서드
    public void setPreviewShape(Shape shape) {
        this.previewShape = shape;
        repaint();
    }
    
    // 현재 컨텍스트 설정 메서드
    public void setCurrentContext(Context context) {
        this.currentContext = context;
    }
    
    // 현재 도형 타입 설정 메서드
    public void setCurrentShapeType(ShapeType shapeType) {
        this.currentShapeType = shapeType;
    }
}
