package state;

import java.awt.event.MouseEvent;
import java.util.List;

public class GEventStateMananger extends GStateManager {
	private static GEventStateMananger eventStateManager;
	private List<MouseEvent> mouseEvents;

	private GEventStateMananger() {
		mouseEvents = null;
	}

	public static GEventStateMananger getInstance() {
		if (eventStateManager == null) {
			eventStateManager = new GEventStateMananger();
		}
		return eventStateManager;
	}

	public void setMouseEvents(List<MouseEvent> mouseEvents) {
		this.mouseEvents = mouseEvents;
	}

	public List<MouseEvent> getMouseEvents() {
		return mouseEvents;
	}
}
