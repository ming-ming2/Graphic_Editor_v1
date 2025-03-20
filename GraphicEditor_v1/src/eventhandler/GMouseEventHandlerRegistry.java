package eventhandler;

import java.util.HashMap;
import java.util.Map;

import command.GCommandManager;
import type.GMode;

public class GMouseEventHandlerRegistry {
	private Map<GMode, GMouseEventHandler> handlers = new HashMap<>();

	public GMouseEventHandlerRegistry(GCommandManager commandManager) {
		handlers.put(GMode.DEFAULT, new DefaultHandler());
		handlers.put(GMode.SHAPE, new GShapeDrawingHandler(commandManager));
		// 추가 예정..
	}

	public GMouseEventHandler get(GMode mode) {
		return handlers.get(mode);
	}

}
