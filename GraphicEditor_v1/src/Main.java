import command.*;
import command.ShapeCommand;
import global.Context;
import view.MainFrame;

public class Main {
	
	public static void main(String[] args) {
		//commandManager 생성
		CommandManager commandManager = new CommandManager();
		commandManager.add(Context.DEFAULT, new DefaultCommand());
		commandManager.add(Context.SHAPE, new ShapeCommand());
		//mainFrame 생성 및 commandManager 주입
		MainFrame mainFrame = new MainFrame(commandManager);
		mainFrame.initialize();
		mainFrame.setVisible(true);

	}

}
