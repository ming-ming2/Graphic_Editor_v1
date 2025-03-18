package command;

import java.util.HashMap;
import java.util.Map;

import dto.GCommandDTO;
import global.GMode;

public class GCommandManager {
	private final Map<GMode, GCommand> commands = new HashMap<>();

	public void add(GMode mode, GCommand command) {
		commands.put(mode, command);
	}

	public void execute(GMode mode, GCommandDTO dto) {
		commands.getOrDefault(mode, new DefaultCommand()).execute(dto);
	}
}
