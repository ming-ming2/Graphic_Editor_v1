package command;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import global.Context;


public class CommandManager {
	 private final Map<Context, Command> commandMap = new HashMap<>();
	 
	 public void add(Context context, Command command) {
		commandMap.put(context, command);
	 }
	 
	 public void execute(Context context, CommandContext commandContext) {
		 commandMap.getOrDefault(context, new DefaultCommand()).execute(commandContext);
	 }
}
