package dto;

import java.awt.event.MouseEvent;
import java.util.List;

import type.GShapeType;
import view.GDrawingPanel;

public class GShapeCommandDTO implements GCommandDTO {
	private final List<MouseEvent> mouseEvents;
	private final GShapeType shapeType;
	private final GDrawingPanel drawingPanel;

	public GShapeCommandDTO(List<MouseEvent> mouseEvents, GShapeType shapeType, GDrawingPanel drawingPanel) {
		this.mouseEvents = mouseEvents;
		this.shapeType = shapeType;
		this.drawingPanel = drawingPanel;
	}

	public List<MouseEvent> getMouseEvents() {
		return mouseEvents;
	}

	public GShapeType getShapeType() {
		return shapeType;
	}

	public GDrawingPanel getDrawingPanel() {
		return drawingPanel;
	}
	
	

}
