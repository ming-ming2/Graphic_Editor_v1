package dto;

import java.awt.event.MouseEvent;
import java.util.List;

public class GShapeCommandDTO implements GCommandDTO {
	private final List<MouseEvent> mouseEvents;

	public GShapeCommandDTO(List<MouseEvent> mouseEvents) {
		this.mouseEvents = mouseEvents;
	}

	public List<MouseEvent> getMouseEvents() {
		return mouseEvents;
	}

}
