package command;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import shapes.GShape;
import state.GClipboard;
import state.GDrawingStateManager;

public class GPasteCommand implements GCommand {
	private List<GShape> pastedShapes = new ArrayList<>();
	private Point pasteOffset;

	@Override
	public void execute() {
		GDrawingStateManager stateManager = GDrawingStateManager.getInstance();
		GClipboard clipboard = GClipboard.getInstance();

		if (!clipboard.hasContents()) {
			return;
		}

		stateManager.clearSelection();

		if (pasteOffset != null && !pastedShapes.isEmpty()) {
			for (GShape shape : pastedShapes) {
				stateManager.addShape(shape);
				stateManager.addToSelection(shape);
			}
			System.out.println("붙여넣기 다시 실행: " + pastedShapes.size() + "개 도형");
			return;
		}

		pasteOffset = clipboard.getCurrentOffset();
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
