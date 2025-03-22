package command;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import type.GMode;

public class GCommandManager {
	private final Map<GMode, GCommand> commands = new HashMap<>();
	private Stack<GCommand> undoStack = new Stack<>();
	private Stack<GCommand> redoStack = new Stack<>();

	public GCommandManager() {
		commands.put(GMode.DEFAULT, new DefaultCommand());
		commands.put(GMode.SHAPE, new GShapeCommand());
		commands.put(GMode.GROUP_MOVE, new GGroupMoveCommand());
	}

	public void execute(GMode mode) {
		GCommand command = commands.get(mode);
		command.execute();
	}

	public void executeAndStore(GMode mode) {
		GCommand command = commands.get(mode);
		command.execute();
		undoStack.push(command);
		redoStack.clear();
	}

	public boolean canUndo() {
		return !undoStack.isEmpty();
	}

	public boolean canRedo() {
		return !redoStack.isEmpty();
	}

	public void undo() {
		if (canUndo()) {
			GCommand command = undoStack.pop();
			command.unexecute();
			redoStack.push(command);
		}
	}

	public void redo() {
		if (canRedo()) {
			GCommand command = redoStack.pop();
			command.execute();
			undoStack.push(command);
		}
	}
}