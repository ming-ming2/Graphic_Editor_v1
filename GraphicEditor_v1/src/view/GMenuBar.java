package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

public class GMenuBar extends JMenuBar implements GContainerInterface {
	private static final long serialVersionUID = 1L;

	// 메인 메뉴
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu viewMenu;

	// 파일 메뉴 항목들
	private JMenuItem newMenuItem;
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem exitMenuItem;

	// 편집 메뉴 항목들
	private JMenuItem cutMenuItem;
	private JMenuItem copyMenuItem;
	private JMenuItem pasteMenuItem;
	private JMenuItem undoMenuItem;
	private JMenuItem redoMenuItem;

	// 보기 메뉴 항목들
	private JMenuItem zoomInMenuItem;
	private JMenuItem zoomOutMenuItem;

	public GMenuBar() {
		// 기본 생성자
	}

	@Override
	public void createComponents() {
		// 메인 메뉴 생성
		fileMenu = new JMenu("파일");
		editMenu = new JMenu("편집");
		viewMenu = new JMenu("보기");

		// 파일 메뉴 항목 생성
		newMenuItem = new JMenuItem("새로 만들기");
		openMenuItem = new JMenuItem("열기");
		saveMenuItem = new JMenuItem("저장");
		saveAsMenuItem = new JMenuItem("다른 이름으로 저장");
		exitMenuItem = new JMenuItem("끝내기");

		// 편집 메뉴 항목 생성
		cutMenuItem = new JMenuItem("자르기");
		copyMenuItem = new JMenuItem("복사");
		pasteMenuItem = new JMenuItem("붙여넣기");
		undoMenuItem = new JMenuItem("실행 취소");
		redoMenuItem = new JMenuItem("다시 실행");

		// 보기 메뉴 항목 생성
		zoomInMenuItem = new JMenuItem("확대");
		zoomOutMenuItem = new JMenuItem("축소");
	}

	@Override
	public void arrangeComponents() {
		// 파일 메뉴 구성
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(new JSeparator());
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);
		fileMenu.add(new JSeparator());
		fileMenu.add(exitMenuItem);

		// 편집 메뉴 구성
		editMenu.add(undoMenuItem);
		editMenu.add(redoMenuItem);
		editMenu.add(new JSeparator());
		editMenu.add(cutMenuItem);
		editMenu.add(copyMenuItem);
		editMenu.add(pasteMenuItem);

		// 보기 메뉴 구성
		viewMenu.add(zoomInMenuItem);
		viewMenu.add(zoomOutMenuItem);

		// 메뉴바에 메인 메뉴 추가
		this.add(fileMenu);
		this.add(editMenu);
		this.add(viewMenu);
	}

	@Override
	public void setAttributes() {
		// 메뉴바 스타일 설정
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
		this.setBackground(new Color(245, 245, 245));

		// 폰트 설정
		Font menuFont = new Font("맑은 고딕", Font.PLAIN, 12);

		// 메인 메뉴 스타일 설정
		fileMenu.setFont(menuFont);
		editMenu.setFont(menuFont);
		viewMenu.setFont(menuFont);

		// 단축키 설정
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveAsMenuItem
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));

		zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ActionEvent.CTRL_MASK));
		zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));

		// 메뉴 항목 폰트 설정
		setMenuItemFont(newMenuItem, menuFont);
		setMenuItemFont(openMenuItem, menuFont);
		setMenuItemFont(saveMenuItem, menuFont);
		setMenuItemFont(saveAsMenuItem, menuFont);
		setMenuItemFont(exitMenuItem, menuFont);

		setMenuItemFont(cutMenuItem, menuFont);
		setMenuItemFont(copyMenuItem, menuFont);
		setMenuItemFont(pasteMenuItem, menuFont);
		setMenuItemFont(undoMenuItem, menuFont);
		setMenuItemFont(redoMenuItem, menuFont);

		setMenuItemFont(zoomInMenuItem, menuFont);
		setMenuItemFont(zoomOutMenuItem, menuFont);
	}

	private void setMenuItemFont(JMenuItem item, Font font) {
		item.setFont(font);
	}

	@Override
	public void addEventHandler() {
		// 이벤트 핸들러 추가 (나중에 기능 구현 시 추가 예정)
		ActionListener dummyAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 기능은 추후 구현
				System.out.println("메뉴 선택: " + e.getActionCommand());
			}
		};

		// 파일 메뉴 이벤트 핸들러 설정
		newMenuItem.addActionListener(dummyAction);
		openMenuItem.addActionListener(dummyAction);
		saveMenuItem.addActionListener(dummyAction);
		saveAsMenuItem.addActionListener(dummyAction);
		exitMenuItem.addActionListener(dummyAction);

		// 편집 메뉴 이벤트 핸들러 설정
		cutMenuItem.addActionListener(dummyAction);
		copyMenuItem.addActionListener(dummyAction);
		pasteMenuItem.addActionListener(dummyAction);
		undoMenuItem.addActionListener(dummyAction);
		redoMenuItem.addActionListener(dummyAction);

		// 보기 메뉴 이벤트 핸들러 설정
		zoomInMenuItem.addActionListener(dummyAction);
		zoomOutMenuItem.addActionListener(dummyAction);
	}
}