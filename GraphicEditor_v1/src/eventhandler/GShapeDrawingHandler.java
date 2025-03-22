package eventhandler;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import command.GCommandManager;
import state.GEventStateMananger;
import type.GMode;

public class GShapeDrawingHandler implements GMouseEventHandler {
	private GCommandManager commandManager;
	private MouseEvent startEvent;

	public GShapeDrawingHandler(GCommandManager commandManager) {
		this.commandManager = commandManager;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		startEvent = e;
		// 추가: 이벤트 상태 관리자에 드래그 시작점 저장
		GEventStateMananger.getInstance().setCurrentPoint(e.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (startEvent != null) {
			// 이벤트 상태 관리자에 정보 저장
			List<MouseEvent> events = createMouseEvents(e);
			GEventStateMananger.getInstance().setMouseEvents(events);
			GEventStateMananger.getInstance().setCurrentPoint(e.getPoint());

			// 커맨드에 작업 위임
			commandManager.execute(GMode.SHAPE);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (startEvent != null) {
			// 이벤트 상태 관리자에 정보 저장
			List<MouseEvent> events = createMouseEvents(e);
			GEventStateMananger.getInstance().setMouseEvents(events);
			GEventStateMananger.getInstance().setCurrentPoint(e.getPoint());

			// 커맨드에 작업 위임
			commandManager.execute(GMode.SHAPE);
		}
	}

	private List<MouseEvent> createMouseEvents(MouseEvent e) {
		List<MouseEvent> events = new ArrayList<>();
		events.add(startEvent);
		events.add(e);
		return events;
	}
}