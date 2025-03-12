package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import command.CommandManager;

public class MainFrame extends JFrame implements ContainerInterface {
    private static final long serialVersionUID = 1L;
    private List<ContainerInterface> compList = new ArrayList<>();
    private CommandManager commandManager;
    
    private DrawingPanel drawingPanel;
    private ToolsPanel toolsPanel;
    
    public MainFrame(CommandManager commandManager) {
        this.commandManager = commandManager;
        createComponents();
        arrangeComponents();
    }
    
    @Override
    public void initialize() {
        this.setTitle("Graphic Editor_V1");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(1600, 900);
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 각 컨테이너에 대해 initialize, createComponents, arrangeComponents 호출
        for (ContainerInterface container : compList) {
            container.initialize();
        }
    }
    
    @Override
    public void createComponents() {
        // DrawingPanel 생성
        drawingPanel = new DrawingPanel(commandManager);
        compList.add(drawingPanel);
        
        // ToolsPanel 생성
        toolsPanel = new ToolsPanel(commandManager, drawingPanel);
        compList.add(toolsPanel);
    }

    @Override
    public void arrangeComponents() {
        setLayout(new BorderLayout());
        // 상단에 도구 패널 배치
        add(toolsPanel, BorderLayout.NORTH);
        
        // 중앙에 드로잉 패널 배치
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(drawingPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    public DrawingPanel getDrawingPanel() {
        return drawingPanel;
    }
}
