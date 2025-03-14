package command;

import java.util.HashMap;
import java.util.Map;

import global.Mode;

public class CommandManager {
	private final Map<Mode, Command> commands = new HashMap<>();

	public void add(Mode mode, Command command) {
		commands.put(mode, command);
	}

	public void execute(Mode mode, CommandContext commandContext) {
		commands.getOrDefault(mode, new DefaultCommand()).execute(commandContext);
	}
}
