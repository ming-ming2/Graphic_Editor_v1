package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import command.GCommandManager;
import state.GEventStateMananger;
import state.GObserver;
import state.GZoomManager;
import type.GZoomType;

public class GStatusBar extends JPanel implements GContainerInterface, GObserver {
	private static final long serialVersionUID = 1L;

	private JLabel statusLabel;
	private JLabel zoomLabel;
	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JButton zoomResetButton;

	public GStatusBar() {
	}

	@Override
	public void createComponents() {
		statusLabel = new JLabel("준비");
		zoomLabel = new JLabel(GZoomManager.getInstance().getZoomPercentage(), SwingConstants.CENTER);
		zoomInButton = new JButton("+");
		zoomOutButton = new JButton("-");
		zoomResetButton = new JButton("100%");
	}

	@Override
	public void arrangeComponents() {
		this.setLayout(new BorderLayout());

		// 상태 메시지 영역 (왼쪽)
		this.add(statusLabel, BorderLayout.WEST);

		// 줌 컨트롤 영역 (오른쪽)
		JPanel zoomPanel = new JPanel();
		zoomPanel.add(zoomOutButton);
		zoomPanel.add(zoomLabel);
		zoomPanel.add(zoomInButton);
		zoomPanel.add(Box.createHorizontalStrut(5));
		zoomPanel.add(zoomResetButton);

		this.add(zoomPanel, BorderLayout.EAST);
	}

	@Override
	public void setAttributes() {
		this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
		this.setPreferredSize(new Dimension(0, 25));
		this.setBackground(new Color(240, 240, 240));

		// 상태 레이블 설정
		statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		statusLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));

		// 줌 레이블 설정
		zoomLabel.setPreferredSize(new Dimension(60, 20));
		zoomLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));

		// 줌 버튼 설정
		zoomInButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		zoomOutButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		zoomResetButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12));

		zoomInButton.setFocusPainted(false);
		zoomOutButton.setFocusPainted(false);
		zoomResetButton.setFocusPainted(false);

		zoomInButton.setPreferredSize(new Dimension(30, 20));
		zoomOutButton.setPreferredSize(new Dimension(30, 20));
		zoomResetButton.setPreferredSize(new Dimension(50, 20));
	}

	@Override
	public void addEventHandler() {
		GCommandManager commandManager = GEventStateMananger.getInstance().getCommandManager();

		zoomInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				commandManager.executeAndStore(GZoomType.ZOOM_IN);
			}
		});

		zoomOutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				commandManager.executeAndStore(GZoomType.ZOOM_OUT);
			}
		});

		zoomResetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				commandManager.executeAndStore(GZoomType.ZOOM_RESET);
			}
		});
	}

	@Override
	public void update() {
		// 줌 레벨 업데이트
		zoomLabel.setText(GZoomManager.getInstance().getZoomPercentage());
	}

	public void setStatusMessage(String message) {
		statusLabel.setText(message);
	}
}