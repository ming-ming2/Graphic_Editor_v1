import command.CommandManager;
import command.DefaultCommand;
import command.ShapeCommand;
import global.Mode;
import view.MainFrame;

public class Main {

	public static void main(String[] args) {
		// commandManager 생성
		CommandManager commandManager = new CommandManager();
		commandManager.add(Mode.DEFAULT, new DefaultCommand());
		commandManager.add(Mode.SHAPE, new ShapeCommand());
		// mainFrame 생성 및 commandManager 주입
		MainFrame mainFrame = new MainFrame(commandManager);
		mainFrame.setVisible(true);

	}

}
