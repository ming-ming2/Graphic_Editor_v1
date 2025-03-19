package eventhandler;

import java.util.HashMap;
import java.util.Map;

import command.GCommandManager;
import type.GMode;
import view.GDrawingPanel;

public class GMouseEventHandlerRepository {
	private Map<GMode, GMouseEventHandler> handlers = new HashMap<>();

	public GMouseEventHandlerRepository(GCommandManager commandManager, GDrawingPanel panel) {
		handlers.put(GMode.DEFAULT, new DefaultHandler());
		handlers.put(GMode.SHAPE, new GShapeDrawingHandler(commandManager, panel));
		// 추가 예정..
	}

	public GMouseEventHandler get(GMode mode) {
		return handlers.get(mode);
	}

}
