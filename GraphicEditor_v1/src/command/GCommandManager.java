package command;

import java.util.HashMap;
import java.util.Map;

import type.GMode;

public class GCommandManager {
	private final Map<GMode, GCommand> commands = new HashMap<>();

	public GCommandManager() {
		commands.put(GMode.DEFAULT, new DefaultCommand());
		commands.put(GMode.SHAPE, new GShapeCommand());
		commands.put(GMode.SELECTION, new GGroupSelectionCommand());
		commands.put(GMode.GROUP_MOVE, new GGroupMoveCommand());

	}

	public void execute(GMode mode) {
		commands.getOrDefault(mode, new DefaultCommand()).execute();
	}
}
