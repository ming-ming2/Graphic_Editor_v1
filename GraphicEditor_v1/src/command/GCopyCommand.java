package command;

import java.util.ArrayList;
import java.util.List;

import shapes.GShape;
import state.GClipboard;
import state.GDrawingStateManager;

public class GCopyCommand implements GCommand {
	private List<GShape> selectedShapes = new ArrayList<>();

	@Override
	public void execute() {
		GDrawingStateManager stateManager = GDrawingStateManager.getInstance();
		selectedShapes = new ArrayList<>(stateManager.getSelectedShapes());

		GClipboard.getInstance().copy(selectedShapes);

		System.out.println("복사 완료: " + selectedShapes.size() + "개 도형");
	}

	@Override
	public void unexecute() {
	}
}