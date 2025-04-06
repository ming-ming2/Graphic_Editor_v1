package command;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import state.GStateManager;
import type.GMode;
import type.GZoomType;

public class GCommandManager extends GStateManager {
	private final Map<GMode, CommandFactory> commandFactories = new HashMap<>();
	private final Map<GZoomType, CommandFactory> zoomCommandFactories = new HashMap<>();
	private Stack<GCommand> undoStack = new Stack<>();
	private Stack<GCommand> redoStack = new Stack<>();
	private Map<GMode, GCommand> activeCommands = new HashMap<>();

	private interface CommandFactory {
		GCommand createCommand();
	}

	public GCommandManager() {
		commandFactories.put(GMode.DEFAULT, () -> new DefaultCommand());
		commandFactories.put(GMode.SHAPE, () -> new GShapeCommand());

		commandFactories.put(GMode.GROUP_MOVE, () -> {
			GCommand existingCommand = activeCommands.get(GMode.GROUP_MOVE);
			if (existingCommand != null)
				return existingCommand;
			GCommand newCommand = new GGroupMoveCommand();
			activeCommands.put(GMode.GROUP_MOVE, newCommand);
			return newCommand;
		});

		commandFactories.put(GMode.RESIZE, () -> {
			GCommand existingCommand = activeCommands.get(GMode.RESIZE);
			if (existingCommand != null)
				return existingCommand;
			GCommand newCommand = new GResizeCommand();
			activeCommands.put(GMode.RESIZE, newCommand);
			return newCommand;
		});

		commandFactories.put(GMode.ROTATE, () -> {
			GCommand existingCommand = activeCommands.get(GMode.ROTATE);
			if (existingCommand != null)
				return existingCommand;
			GCommand newCommand = new GRotateCommand();
			activeCommands.put(GMode.ROTATE, newCommand);
			return newCommand;
		});

		commandFactories.put(GMode.CUT, () -> new GCutCommand());
		commandFactories.put(GMode.COPY, () -> new GCopyCommand());
		commandFactories.put(GMode.PASTE, () -> new GPasteCommand());

		zoomCommandFactories.put(GZoomType.ZOOM_IN, () -> new GZoomCommand(GZoomType.ZOOM_IN));
		zoomCommandFactories.put(GZoomType.ZOOM_OUT, () -> new GZoomCommand(GZoomType.ZOOM_OUT));
		zoomCommandFactories.put(GZoomType.ZOOM_RESET, () -> new GZoomCommand(GZoomType.ZOOM_RESET));
	}

	public GCommand createCommand(GMode mode) {
		CommandFactory factory = commandFactories.get(mode);
		if (factory != null)
			return factory.createCommand();
		System.err.println("알 수 없는 모드: " + mode);
		return null;
	}

	public GCommand createZoomCommand(GZoomType zoomType) {
		CommandFactory factory = zoomCommandFactories.get(zoomType);
		if (factory != null)
			return factory.createCommand();
		System.err.println("알 수 없는 줌 타입: " + zoomType);
		return null;
	}

	public void execute(GMode mode) {
		GCommand command = null;

		if (activeCommands.containsKey(mode)) {
			command = activeCommands.get(mode);
		} else {
			command = createCommand(mode);
			if (mode == GMode.SHAPE || mode == GMode.GROUP_MOVE || mode == GMode.RESIZE || mode == GMode.ROTATE) {
				activeCommands.put(mode, command);
			}
		}

		if (command != null) {
			command.execute();
		}
	}

	public void execute(GZoomType zoomType) {
		GCommand command = createZoomCommand(zoomType);
		if (command != null)
			command.execute();
	}

	public void executeAndStore(GMode mode) {
		GCommand command = null;

		if (mode == GMode.GROUP_MOVE || mode == GMode.RESIZE || mode == GMode.ROTATE) {
			command = activeCommands.get(mode);
		}

		if (command == null) {
			command = createCommand(mode);
		}

		if (command != null) {
			command.execute();

			boolean shouldStore = true;

			if (mode == GMode.DEFAULT) {
				shouldStore = ((DefaultCommand) command).hasChanges();
			} else if (mode == GMode.COPY) {
				shouldStore = false;
			}

			if (shouldStore) {
				undoStack.push(command);
				redoStack.clear();
				notifyObservers();
				System.out.println("명령 스택에 추가: " + command.getClass().getSimpleName());
				activeCommands.remove(mode);
			}
		}
	}

	public void executeAndStore(GZoomType zoomType) {
		GCommand command = createZoomCommand(zoomType);
		if (command != null) {
			command.execute();
			undoStack.push(command);
			redoStack.clear();
			notifyObservers();
			System.out.println("줌 명령 스택에 추가: " + zoomType);
		}
	}

	public void pushCommand(GCommand command) {
		if (command != null) {
			undoStack.push(command);
			redoStack.clear();
			notifyObservers();
			System.out.println("명령 스택에 추가: " + command.getClass().getSimpleName());
		}
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
			notifyObservers();
			System.out.println("실행 취소: " + command.getClass().getSimpleName());
		}
	}

	public void redo() {
		if (canRedo()) {
			GCommand command = redoStack.pop();
			command.execute();
			undoStack.push(command);
			notifyObservers();
			System.out.println("다시 실행: " + command.getClass().getSimpleName());
		}
	}

	public void resetActiveCommands() {
		activeCommands.clear();
	}

	public GCommand getCurrentCommand(GMode mode) {
		return activeCommands.get(mode);
	}

	public GCommand getActiveCommand(GMode mode) {
		return activeCommands.get(mode);
	}

}
