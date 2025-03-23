package command;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import shapes.GShape;
import state.GDrawingStateManager;

public class GResizeCommand implements GCommand {
	private Map<GShape, Rectangle> originalBounds = new HashMap<>();
	private Map<GShape, Rectangle> newBounds = new HashMap<>();

	@Override
	public void execute() {
		GDrawingStateManager stateManager = GDrawingStateManager.getInstance();
		GShape targetShape = stateManager.getTargetShapeForResize();

		if (targetShape == null) {
			return;
		}

		// 명령 객체 초기화 (최초 실행 시)
		if (originalBounds.isEmpty()) {
			// 원래 경계 저장
			Rectangle origBounds = stateManager.getOriginalBounds(targetShape);
			if (origBounds != null) {
				originalBounds.put(targetShape, new Rectangle(origBounds));
			} else {
				originalBounds.put(targetShape, new Rectangle(targetShape.getBounds()));
			}

			// 현재 경계 저장 (리사이즈된 상태)
			newBounds.put(targetShape, new Rectangle(targetShape.getBounds()));
		}

		// 이미 초기화된 경우 (redo 등)
		for (GShape shape : newBounds.keySet()) {
			if (stateManager.getShapes().contains(shape)) {
				Rectangle bounds = newBounds.get(shape);
				shape.setFromBounds(bounds);
			}
		}

		// UI 갱신
		stateManager.notifyObservers();
	}

	@Override
	public void unexecute() {
		GDrawingStateManager stateManager = GDrawingStateManager.getInstance();

		// 원래 크기로 복원
		for (GShape shape : originalBounds.keySet()) {
			if (stateManager.getShapes().contains(shape)) {
				Rectangle bounds = originalBounds.get(shape);
				shape.setFromBounds(bounds);
			}
		}

		// UI 갱신
		stateManager.notifyObservers();
	}
}