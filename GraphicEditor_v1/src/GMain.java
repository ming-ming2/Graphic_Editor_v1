import command.DefaultCommand;
import command.GCommandManager;
import command.GShapeCommand;
import type.GMode;
import view.GMainFrame;

public class GMain {

	public static void main(String[] args) {
		// commandManager 생성
		GCommandManager commandManager = new GCommandManager();
		commandManager.add(GMode.DEFAULT, new DefaultCommand());
		commandManager.add(GMode.SHAPE, new GShapeCommand());
		// mainFrame 생성 및 commandManager 주입
		GMainFrame mainFrame = new GMainFrame(commandManager);
		mainFrame.setVisible(true);
		mainFrame.initialize();

	}

}
