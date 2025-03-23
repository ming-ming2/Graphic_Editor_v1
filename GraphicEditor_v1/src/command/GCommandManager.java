package command;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import state.GStateManager;
import type.GMode;

public class GCommandManager extends GStateManager {
	private final Map<GMode, CommandFactory> commandFactories = new HashMap<>();
	private Stack<GCommand> undoStack = new Stack<>();
	private Stack<GCommand> redoStack = new Stack<>();

	// 현재 활성화된 명령 참조
	private Map<GMode, GCommand> activeCommands = new HashMap<>();

	private interface CommandFactory {
		GCommand createCommand();
	}

	public GCommandManager() {
		commandFactories.put(GMode.DEFAULT, () -> new DefaultCommand());
		commandFactories.put(GMode.SHAPE, () -> new GShapeCommand());
		commandFactories.put(GMode.GROUP_MOVE, () -> {
			// 이미 생성된 이동 명령이 있다면 재사용
			GCommand existingCommand = activeCommands.get(GMode.GROUP_MOVE);
			if (existingCommand != null) {
				return existingCommand;
			}

			// 없으면 새로 생성
			GCommand newCommand = new GGroupMoveCommand();
			activeCommands.put(GMode.GROUP_MOVE, newCommand);
			return newCommand;
		});
		commandFactories.put(GMode.RESIZE, () -> {
			// 이미 생성된 리사이즈 명령이 있다면 재사용
			GCommand existingCommand = activeCommands.get(GMode.RESIZE);
			if (existingCommand != null) {
				return existingCommand;
			}

			// 없으면 새로 생성
			GCommand newCommand = new GResizeCommand();
			activeCommands.put(GMode.RESIZE, newCommand);
			return newCommand;
		});
		commandFactories.put(GMode.ROTATE, () -> {
			// 이미 생성된 회전 명령이 있다면 재사용
			GCommand existingCommand = activeCommands.get(GMode.ROTATE);
			if (existingCommand != null) {
				return existingCommand;
			}

			// 없으면 새로 생성
			GCommand newCommand = new GRotateCommand();
			activeCommands.put(GMode.ROTATE, newCommand);
			return newCommand;
		});
	}

	private GCommand createCommand(GMode mode) {
		CommandFactory factory = commandFactories.get(mode);
		if (factory != null) {
			return factory.createCommand();
		}
		System.err.println("알 수 없는 모드: " + mode);
		return null;
	}

	public void execute(GMode mode) {
		GCommand command = createCommand(mode);
		if (command != null) {
			command.execute();
		}
	}

	public void executeAndStore(GMode mode) {
		GCommand command = activeCommands.get(mode);

		// 해당 모드의 명령이 없으면 새로 생성
		if (command == null) {
			command = createCommand(mode);
		}

		if (command != null) {
			command.execute();

			// 변경 사항이 있는 경우에만 스택에 저장
			if ((mode == GMode.DEFAULT && ((DefaultCommand) command).hasChanges()) || mode != GMode.DEFAULT) {

				undoStack.push(command);
				redoStack.clear();
				notifyObservers();
				System.out.println("명령 스택에 추가: " + command.getClass().getSimpleName());

				// 스택에 저장된 명령은 더 이상 활성 명령이 아님
				activeCommands.remove(mode);
			}
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

	// 현재 활성화된 명령들 초기화
	public void resetActiveCommands() {
		activeCommands.clear();
	}
}