package state;

import type.GMode;
import type.GShapeType;

public class GDrawingStateManager {
	// 단일 객체
	private static GDrawingStateManager drawingStateManager;

	// DrawingPanel에 적용되는 state
	private GMode currentMode = GMode.DEFAULT;
	private GShapeType currentShapeType = null;
	// ..추가예정

	private GDrawingStateManager() {
		// 외부 생성 금지. 모든 컴포넌트가 동일한 상태를 공유한다
	}

	public static GDrawingStateManager getInstance() {
		if (drawingStateManager == null) {
			drawingStateManager = new GDrawingStateManager();
		}
		return drawingStateManager;
	}
}
