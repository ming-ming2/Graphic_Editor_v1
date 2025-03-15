package eventhandler;

import java.util.HashMap;
import java.util.Map;

import command.CommandManager;
import global.Mode;
import view.DrawingPanel;

public class MouseEventHandlerRepository {
	private Map<Mode, MouseEventHandler> handlers = new HashMap<>();

	public MouseEventHandlerRepository(CommandManager commandManager, DrawingPanel panel) {
		handlers.put(Mode.DEFAULT, new DefaultHandler());
		handlers.put(Mode.SHAPE, new ShapeDrawingHandler(commandManager, panel));
		// 추가 예정..
	}

	public MouseEventHandler get(Mode mode) {
		return handlers.get(mode);
	}

}
