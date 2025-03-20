package command;

import state.GDrawingStateManager;

public class GGroupSelectionCommand implements GCommand {
	@Override
	public void execute() {
		GDrawingStateManager manager = GDrawingStateManager.getInstance();

		// 선택 영역 내의 도형들을 선택 상태로 변경
		manager.selectShapesInArea();

		// 선택 영역 초기화 (선택 완료 후에는 선택 영역 표시 제거)
		manager.setSelectionArea(null);
	}
}