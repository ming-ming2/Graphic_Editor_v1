package eventhandler;

import java.util.HashMap;
import java.util.Map;

import command.GCommandManager;
import type.GMode;

public class GMouseEventHandlerRegistry {
	private Map<GMode, GMouseEventHandler> handlers = new HashMap<>();
	private GCommandManager commandManager;

	public GMouseEventHandlerRegistry(GCommandManager commandManager) {
		this.commandManager = commandManager;
		// 기본 핸들러만 초기화
		handlers.put(GMode.DEFAULT, new DefaultHandler(commandManager));
	}

	public GMouseEventHandler get(GMode mode) {
		// 요청 시 초기화 (Lazy initialization)
		if (!handlers.containsKey(mode)) {
			switch (mode) {
			case SHAPE:
				handlers.put(GMode.SHAPE, new GShapeDrawingHandler(commandManager));
				break;
			case SELECTION:
				handlers.put(GMode.SELECTION, new GSelectionHandler(commandManager));
				break;
			case GROUP_MOVE:
				handlers.put(GMode.GROUP_MOVE, new GGroupMoveHandler(commandManager));
				break;
			}
		}
		return handlers.get(mode);
	}
}