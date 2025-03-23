package command;

import java.util.ArrayList;
import java.util.List;

import shapes.GShape;
import state.GClipboard;
import state.GDrawingStateManager;

public class GCutCommand implements GCommand {
	private List<GShape> cutShapes = new ArrayList<>();
	private int[] originalIndices;

	@Override
	public void execute() {
		GDrawingStateManager stateManager = GDrawingStateManager.getInstance();
		List<GShape> selectedShapes = stateManager.getSelectedShapes();

		if (selectedShapes.isEmpty()) {
			return;
		}

		cutShapes = new ArrayList<>(selectedShapes);
		originalIndices = new int[cutShapes.size()];

		List<GShape> allShapes = stateManager.getShapes();
		for (int i = 0; i < cutShapes.size(); i++) {
			GShape shape = cutShapes.get(i);
			originalIndices[i] = allShapes.indexOf(shape);
		}

		GClipboard.getInstance().copy(cutShapes);

		for (GShape shape : cutShapes) {
			stateManager.removeShape(shape);
		}

		stateManager.clearSelection();

		System.out.println("자르기 완료: " + cutShapes.size() + "개 도형");
	}

	@Override
	public void unexecute() {
		if (cutShapes.isEmpty()) {
			return;
		}

		GDrawingStateManager stateManager = GDrawingStateManager.getInstance();
		List<GShape> allShapes = stateManager.getShapes();

		for (int i = 0; i < cutShapes.size(); i++) {
			GShape shape = cutShapes.get(i);

			int index = originalIndices[i];
			if (index >= allShapes.size()) {
				stateManager.addShape(shape);
			} else {
				allShapes.add(index, shape);
			}

			stateManager.addToSelection(shape);
		}

		stateManager.notifyObservers();
	}
}