package eventhandler;

import java.util.HashMap;
import java.util.Map;

import command.GCommandManager;
import type.GMode;

public class GMouseEventHandlerRegistry {
	private Map<GMode, GMouseEventHandler> handlers = new HashMap<>();

	public GMouseEventHandlerRegistry(GCommandManager commandManager) {
		handlers.put(GMode.DEFAULT, new GDefaultHandler(commandManager));
		handlers.put(GMode.SHAPE, new GShapeDrawingHandler(commandManager));
		handlers.put(GMode.GROUP_MOVE, new GGroupMoveHandler(commandManager));
		handlers.put(GMode.RESIZE, new GResizeHandler(commandManager));
		handlers.put(GMode.ROTATE, new GRotateHandler(commandManager)); // 회전 핸들러 추가
	}

	public GMouseEventHandler get(GMode mode) {
		return handlers.get(mode);
	}
}