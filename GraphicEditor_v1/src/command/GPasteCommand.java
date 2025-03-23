package command;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import shapes.GShape;
import state.GClipboard;
import state.GDrawingStateManager;

public class GPasteCommand implements GCommand {
	private List<GShape> pastedShapes = new ArrayList<>();
	private Point pasteOffset; // 붙여넣기에 사용된 오프셋 기억

	@Override
	public void execute() {
		GDrawingStateManager stateManager = GDrawingStateManager.getInstance();
		GClipboard clipboard = GClipboard.getInstance();

		if (!clipboard.hasContents()) {
			return;
		}

		stateManager.clearSelection();

		// 이미 붙여넣기가 수행된 경우(다시 실행)
		if (pasteOffset != null && !pastedShapes.isEmpty()) {
			// 이미 저장된 도형들을 다시 추가
			for (GShape shape : pastedShapes) {
				stateManager.addShape(shape);
				stateManager.addToSelection(shape);
			}

			System.out.println("붙여넣기 다시 실행: " + pastedShapes.size() + "개 도형");
			return;
		}

		// 현재 클립보드의 오프셋 기억
		pasteOffset = clipboard.getCurrentOffset();

		// 새로운 붙여넣기 실행
		List<GShape> newShapes = clipboard.paste();

		for (GShape shape : newShapes) {
			stateManager.addShape(shape);
			stateManager.addToSelection(shape);
			pastedShapes.add(shape);
		}

		System.out.println("붙여넣기 완료: " + pastedShapes.size() + "개 도형");
	}

	@Override
	public void unexecute() {
		if (pastedShapes.isEmpty()) {
			return;
		}

		GDrawingStateManager stateManager = GDrawingStateManager.getInstance();

		for (GShape shape : pastedShapes) {
			stateManager.removeShape(shape);
		}

		stateManager.notifyObservers();
	}
}