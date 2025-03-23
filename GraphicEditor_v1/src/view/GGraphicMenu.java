package view;

import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

public class GGraphicMenu extends JMenu implements GContainerInterface {
	private static final long serialVersionUID = 1L;

	private JMenu lineWidthMenu;
	private JMenu lineStyleMenu;
	private JMenu fontStyleMenu;
	private JMenu fontSizeMenu;

	public GGraphicMenu() {
		super("그래픽");
	}

	@Override
	public void createComponents() {
		lineWidthMenu = new JMenu("선 굵기");
		lineStyleMenu = new JMenu("선 스타일");
		fontStyleMenu = new JMenu("폰트 스타일");
		fontSizeMenu = new JMenu("폰트 사이즈");

		JMenuItem thinLineItem = new JMenuItem("얇게");
		JMenuItem normalLineItem = new JMenuItem("보통");
		JMenuItem thickLineItem = new JMenuItem("굵게");
		lineWidthMenu.add(thinLineItem);
		lineWidthMenu.add(normalLineItem);
		lineWidthMenu.add(thickLineItem);

		JMenuItem solidLineItem = new JMenuItem("실선");
		JMenuItem dashedLineItem = new JMenuItem("점선");
		JMenuItem dottedLineItem = new JMenuItem("점선(작은)");
		lineStyleMenu.add(solidLineItem);
		lineStyleMenu.add(dashedLineItem);
		lineStyleMenu.add(dottedLineItem);

		JMenuItem plainFontItem = new JMenuItem("일반");
		JMenuItem boldFontItem = new JMenuItem("굵게");
		JMenuItem italicFontItem = new JMenuItem("기울임");
		JMenuItem boldItalicFontItem = new JMenuItem("굵게 기울임");
		fontStyleMenu.add(plainFontItem);
		fontStyleMenu.add(boldFontItem);
		fontStyleMenu.add(italicFontItem);
		fontStyleMenu.add(boldItalicFontItem);

		JMenuItem size8Item = new JMenuItem("8pt");
		JMenuItem size10Item = new JMenuItem("10pt");
		JMenuItem size12Item = new JMenuItem("12pt");
		JMenuItem size14Item = new JMenuItem("14pt");
		JMenuItem size16Item = new JMenuItem("16pt");
		JMenuItem size20Item = new JMenuItem("20pt");
		JMenuItem size24Item = new JMenuItem("24pt");
		JMenuItem size32Item = new JMenuItem("32pt");
		fontSizeMenu.add(size8Item);
		fontSizeMenu.add(size10Item);
		fontSizeMenu.add(size12Item);
		fontSizeMenu.add(size14Item);
		fontSizeMenu.add(size16Item);
		fontSizeMenu.add(size20Item);
		fontSizeMenu.add(size24Item);
		fontSizeMenu.add(size32Item);
	}

	@Override
	public void arrangeComponents() {
		this.add(lineWidthMenu);
		this.add(lineStyleMenu);
		this.add(new JSeparator());
		this.add(fontStyleMenu);
		this.add(fontSizeMenu);
	}

	@Override
	public void setAttributes() {
		Font menuFont = new Font("맑은 고딕", Font.PLAIN, 12);

		lineWidthMenu.setFont(menuFont);
		lineStyleMenu.setFont(menuFont);
		fontStyleMenu.setFont(menuFont);
		fontSizeMenu.setFont(menuFont);

		for (int i = 0; i < lineWidthMenu.getItemCount(); i++)
			setMenuItemFont(lineWidthMenu.getItem(i), menuFont);
		for (int i = 0; i < lineStyleMenu.getItemCount(); i++)
			setMenuItemFont(lineStyleMenu.getItem(i), menuFont);
		for (int i = 0; i < fontStyleMenu.getItemCount(); i++)
			setMenuItemFont(fontStyleMenu.getItem(i), menuFont);
		for (int i = 0; i < fontSizeMenu.getItemCount(); i++)
			setMenuItemFont(fontSizeMenu.getItem(i), menuFont);
	}

	private void setMenuItemFont(JMenuItem item, Font font) {
		item.setFont(font);
	}

	@Override
	public void addEventHandler() {
		ActionListener dummyAction = e -> System.out.println("메뉴 선택: " + e.getActionCommand());

		for (int i = 0; i < lineWidthMenu.getItemCount(); i++)
			lineWidthMenu.getItem(i).addActionListener(dummyAction);
		for (int i = 0; i < lineStyleMenu.getItemCount(); i++)
			lineStyleMenu.getItem(i).addActionListener(dummyAction);
		for (int i = 0; i < fontStyleMenu.getItemCount(); i++)
			fontStyleMenu.getItem(i).addActionListener(dummyAction);
		for (int i = 0; i < fontSizeMenu.getItemCount(); i++)
			fontSizeMenu.getItem(i).addActionListener(dummyAction);
	}
}