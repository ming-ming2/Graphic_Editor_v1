import command.*;
import command.ShapeCommand;
import global.Context;
import view.MainFrame;

public class Main {
	
	public static void main(String[] args) {
		CommandManager commandManager = new CommandManager();
		commandManager.add(Context.SHAPE, new ShapeCommand());
		MainFrame mainFrame = new MainFrame(commandManager);
		mainFrame.setVisible(true);

	}

}
