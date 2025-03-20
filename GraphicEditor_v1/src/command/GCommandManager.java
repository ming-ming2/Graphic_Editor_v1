package command;

import java.util.HashMap;
import java.util.Map;

import type.GMode;

public class GCommandManager {
	private final Map<GMode, GCommand> commands = new HashMap<>();

	public void add(GMode mode, GCommand command) {
		commands.put(mode, command);
	}

	public void execute(GMode mode) {
		commands.getOrDefault(mode, new DefaultCommand()).execute();
	}
}
