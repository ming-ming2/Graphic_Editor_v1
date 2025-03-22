package eventhandler;

import java.util.HashMap;
import java.util.Map;

import command.GCommandManager;
import type.GMode;

public class GMouseEventHandlerRegistry {
	private Map<GMode, GMouseEventHandler> handlers = new HashMap<>();

	public GMouseEventHandlerRegistry(GCommandManager commandManager) {
		handlers.put(GMode.DEFAULT, new DefaultHandler(commandManager));
		handlers.put(GMode.SHAPE, new GShapeDrawingHandler(commandManager));
		handlers.put(GMode.SELECTION, new GSelectionHandler(commandManager));
		handlers.put(GMode.GROUP_MOVE, new GGroupMoveHandler(commandManager));

	}

	public GMouseEventHandler get(GMode mode) {
		return handlers.get(mode);
	}
}