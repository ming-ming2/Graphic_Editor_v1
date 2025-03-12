package view;

import javax.swing.JFrame;

import command.CommandManager;

public class MainFrame extends JFrame implements ContainerInterface{
	private static final long serialVersionUID = 1L;
	
	public MainFrame() {
		//
	}
	
	public MainFrame(CommandManager commandManager) {
		createComponents();
		arrangeComponents();
	}

	@Override
	public void createComponents() {
		
	}

	@Override
	public void arrangeComponents() {
			
	}
	
	

}
