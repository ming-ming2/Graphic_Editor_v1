package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import state.GDrawingStateManager;
import type.GMode;
import type.GShapeType;

public class GToolBar extends JToolBar implements GContainerInterface {
	private static final long serialVersionUID = 1L;

	private JButton currentSelectedButton = null;
	private Color selectedColor = new Color(230, 230, 250);
	private List<JButton> shapeButtons = new ArrayList<>();

	private JPanel leftPanel;
	private JPanel centerPanel;
	private JPanel rightPanel;

	public GToolBar() {
	}

	@Override
	public void createComponents() {
		leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JPanel shapesSection = createShapesSection();
		leftPanel.add(shapesSection);
	}

	@Override
	public void arrangeComponents() {
		this.add(leftPanel);
		this.add(centerPanel);
		this.add(rightPanel);
		this.setLayout(new GridLayout(1, 3));
	}

	@Override
	public void setAttributes() {
		this.setFloatable(false);
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		this.setPreferredSize(new Dimension(0, getToolbarHeight()));
		this.setBackground(new Color(240, 240, 240));

		setAreaAttributes(leftPanel);
		setAreaAttributes(centerPanel);
		setAreaAttributes(rightPanel);
	}

	private void setAreaAttributes(JPanel panel) {
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(getSmallSpacing(), getSmallSpacing(), getSmallSpacing(), getSmallSpacing()));
	}

	@Override
	public void addEventHandler() {
	}

	private JPanel createShapesSection() {
		JPanel sectionPanel = new JPanel();
		sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.X_AXIS));
		sectionPanel.setOpaque(false);

		JLabel sectionLabel = new JLabel("도형: ");
		sectionPanel.add(sectionLabel);
		sectionPanel.add(Box.createHorizontalStrut(getSmallSpacing()));

		int columns = 4;
		int rows = (int) Math.ceil((double) getTotalShapeCount() / columns);

		JPanel shapesPanel = new JPanel();
		shapesPanel.setLayout(new GridLayout(rows, columns, getSmallSpacing(), getSmallSpacing()));
		shapesPanel.setBackground(Color.WHITE);

		int panelWidth = columns * getButtonSize() + (columns - 1) * getSmallSpacing() + 2 * getSmallSpacing();
		shapesPanel.setPreferredSize(new Dimension(panelWidth, rows * getButtonSize()));

		createShapeButtons(shapesPanel);

		JScrollPane scrollPane = new JScrollPane(shapesPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		int scrollHeight = getToolbarHeight() - 2 * getToolbarMargin();
		scrollPane.setPreferredSize(new Dimension(panelWidth + getScrollBarWidth(), scrollHeight));
		scrollPane.getVerticalScrollBar().setUnitIncrement(getButtonSize() / 2);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(getScrollBarWidth(), 0));
		scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

		sectionPanel.add(scrollPane);
		return sectionPanel;
	}

	private void createShapeButtons(JPanel panel) {
		addShapeButton(panel, "직선", GShapeType.Line);
		addShapeButton(panel, "화살표", GShapeType.Arrow);
		addShapeButton(panel, "사각형", GShapeType.Rectangle);
		addShapeButton(panel, "둥근 사각형", GShapeType.RoundRectangle);
		addShapeButton(panel, "타원", GShapeType.Oval);
		addShapeButton(panel, "다이아몬드", GShapeType.Diamond);
		addShapeButton(panel, "삼각형", GShapeType.Triangle);
		addShapeButton(panel, "직각 삼각형", GShapeType.RightTriangle);
		addShapeButton(panel, "오각형", GShapeType.Pentagon);
		addShapeButton(panel, "육각형", GShapeType.Hexagon);
		addShapeButton(panel, "별", GShapeType.Star);
		addShapeButton(panel, "텍스트상자", null);
	}

	private JButton addShapeButton(JPanel panel, String tooltip, GShapeType shapeType) {
		JButton button = new JButton();
		button.setToolTipText(tooltip);

		Dimension buttonSize = new Dimension(getButtonSize(), getButtonSize());
		button.setPreferredSize(buttonSize);
		button.setMinimumSize(buttonSize);
		button.setMaximumSize(buttonSize);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setFocusPainted(false);
		button.setContentAreaFilled(true);
		button.setBorderPainted(false);
		button.setBackground(Color.WHITE);
		button.setOpaque(true);

		if (tooltip.equals("텍스트상자")) {
			button.setIcon(createTextBoxIcon());
		} else {
			button.setIcon(createShapeIcon(shapeType));
		}

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (currentSelectedButton != button) {
					button.setBackground(new Color(245, 245, 255));
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (currentSelectedButton != button) {
					button.setBackground(Color.WHITE);
				}
			}
		});

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GDrawingStateManager drawingStateManager = GDrawingStateManager.getInstance();

				if (button == currentSelectedButton) {
					button.setBackground(Color.WHITE);
					currentSelectedButton = null;

					drawingStateManager.setCurrentMode(GMode.DEFAULT);
					drawingStateManager.setCurrentShapeType(null);
				} else {
					if (currentSelectedButton != null) {
						currentSelectedButton.setBackground(Color.WHITE);
					}

					button.setBackground(selectedColor);
					currentSelectedButton = button;

					if (tooltip.equals("텍스트상자")) {
					} else {
						drawingStateManager.setCurrentShapeType(shapeType);
						drawingStateManager.setCurrentMode(GMode.SHAPE);
					}
				}
			}
		});

		panel.add(button);
		shapeButtons.add(button);
		return button;
	}

	private Icon createTextBoxIcon() {
		return new Icon() {
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(Color.BLACK);

				int iconSize = getIconSize();
				int padding = (getButtonSize() - iconSize) / 2;

				int width = iconSize;
				int height = iconSize;

				int offsetX = x + padding;
				int offsetY = y + padding;

				float[] dash = { 2.0f, 2.0f };
				g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
				g2d.drawRect(offsetX, offsetY, width, height);

				int fontSize = iconSize / 2;
				g2d.setFont(new Font("맑은 고딕", Font.BOLD, fontSize));
				FontMetrics fm = g2d.getFontMetrics();
				String text = "T";
				int textWidth = fm.stringWidth(text);
				int textHeight = fm.getAscent() + fm.getDescent();

				int textX = offsetX + (width - textWidth) / 2;
				int textY = offsetY + (height + textHeight) / 2 - fm.getDescent() / 2;

				g2d.drawString(text, textX, textY);
				g2d.dispose();
			}

			@Override
			public int getIconWidth() {
				return getButtonSize();
			}

			@Override
			public int getIconHeight() {
				return getButtonSize();
			}
		};
	}

	private Icon createShapeIcon(GShapeType shapeType) {
		return new Icon() {
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(Color.BLACK);
				g2d.setStroke(new BasicStroke(1.0f));

				int iconSize = getIconSize();
				int padding = (getButtonSize() - iconSize) / 2;

				int width = iconSize;
				int height = iconSize;

				int offsetX = x + padding;
				int offsetY = y + padding;

				switch (shapeType) {
				case Line -> g2d.drawLine(offsetX, offsetY, offsetX + width, offsetY + height);
				case Arrow -> drawArrow(g2d, offsetX, offsetY + height / 2, offsetX + width, offsetY + height / 2);
				case Rectangle -> g2d.drawRect(offsetX, offsetY, width, height);
				case RoundRectangle -> g2d.drawRoundRect(offsetX, offsetY, width, height, width / 4, height / 4);
				case Oval -> g2d.drawOval(offsetX, offsetY, width, height);
				case Diamond -> drawDiamond(g2d, offsetX, offsetY, width, height);
				case Triangle -> drawTriangle(g2d, offsetX, offsetY, width, height);
				case RightTriangle -> drawRightTriangle(g2d, offsetX, offsetY, width, height);
				case Pentagon -> drawPolygon(g2d, offsetX, offsetY, width, height, 5);
				case Hexagon -> drawPolygon(g2d, offsetX, offsetY, width, height, 6);
				case Star -> drawStar(g2d, offsetX, offsetY, width, height);
				default -> g2d.drawRect(offsetX, offsetY, width, height);
				}

				g2d.dispose();
			}

			@Override
			public int getIconWidth() {
				return getButtonSize();
			}

			@Override
			public int getIconHeight() {
				return getButtonSize();
			}
		};
	}

	private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2) {
		g2d.drawLine(x1, y1, x2, y2);
		int arrowSize = getIconSize() / 5;
		g2d.drawLine(x2, y2, x2 - arrowSize, y2 - arrowSize / 2);
		g2d.drawLine(x2, y2, x2 - arrowSize, y2 + arrowSize / 2);
	}

	private void drawDiamond(Graphics2D g2d, int x, int y, int width, int height) {
		int[] xPoints = { x + width / 2, x + width, x + width / 2, x };
		int[] yPoints = { y, y + height / 2, y + height, y + height / 2 };
		g2d.drawPolygon(xPoints, yPoints, 4);
	}

	private void drawTriangle(Graphics2D g2d, int x, int y, int width, int height) {
		int[] xPoints = { x + width / 2, x + width, x };
		int[] yPoints = { y, y + height, y + height };
		g2d.drawPolygon(xPoints, yPoints, 3);
	}

	private void drawRightTriangle(Graphics2D g2d, int x, int y, int width, int height) {
		int[] xPoints = { x, x + width, x };
		int[] yPoints = { y, y + height, y + height };
		g2d.drawPolygon(xPoints, yPoints, 3);
	}

	private void drawPolygon(Graphics2D g2d, int x, int y, int width, int height, int sides) {
		int[] xPoints = new int[sides];
		int[] yPoints = new int[sides];

		int centerX = x + width / 2;
		int centerY = y + height / 2;
		int radiusX = width / 2;
		int radiusY = height / 2;

		for (int i = 0; i < sides; i++) {
			double angle = 2 * Math.PI * i / sides - Math.PI / 2;
			xPoints[i] = (int) (centerX + radiusX * Math.cos(angle));
			yPoints[i] = (int) (centerY + radiusY * Math.sin(angle));
		}

		g2d.drawPolygon(xPoints, yPoints, sides);
	}

	private void drawStar(Graphics2D g2d, int x, int y, int width, int height) {
		int centerX = x + width / 2;
		int centerY = y + height / 2;

		int outerRadiusX = width / 2;
		int outerRadiusY = height / 2;
		int innerRadiusX = outerRadiusX / 2;
		int innerRadiusY = outerRadiusY / 2;

		int points = 5;
		int[] xPoints = new int[points * 2];
		int[] yPoints = new int[points * 2];

		for (int i = 0; i < points * 2; i++) {
			double angle = Math.PI / points * i - Math.PI / 2;
			int radiusX = (i % 2 == 0) ? outerRadiusX : innerRadiusX;
			int radiusY = (i % 2 == 0) ? outerRadiusY : innerRadiusY;

			xPoints[i] = (int) (centerX + radiusX * Math.cos(angle));
			yPoints[i] = (int) (centerY + radiusY * Math.sin(angle));
		}

		g2d.drawPolygon(xPoints, yPoints, points * 2);
	}

	public void clearAllSelection() {
		if (currentSelectedButton != null) {
			currentSelectedButton.setBackground(Color.WHITE);
			currentSelectedButton = null;
		}
	}

	private int getToolbarHeight() {
		return (int) (getFontMetrics(getFont()).getHeight() * 3.5);
	}

	private int getButtonSize() {
		return getFontMetrics(getFont()).getHeight() * 3 / 2;
	}

	private int getIconSize() {
		return getButtonSize() * 2 / 3;
	}

	private int getToolbarMargin() {
		return getFontMetrics(getFont()).getHeight() / 2;
	}

	private int getSmallSpacing() {
		return getFontMetrics(getFont()).getHeight() / 4;
	}

	private int getScrollBarWidth() {
		return getFontMetrics(getFont()).getHeight() / 2;
	}

	private int getTotalShapeCount() {
		return 12;
	}
}
