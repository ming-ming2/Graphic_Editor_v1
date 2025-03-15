package dto;

import java.awt.event.MouseEvent;
import java.util.List;

import global.ShapeType;
import view.DrawingPanel;

public class ShapeCommandDTO implements CommandDTO {
	private final List<MouseEvent> mouseEvents;
	private final ShapeType shapeType;
	private final DrawingPanel drawingPanel;

	public ShapeCommandDTO(List<MouseEvent> mouseEvents, ShapeType shapeType, DrawingPanel drawingPanel) {
		this.mouseEvents = mouseEvents;
		this.shapeType = shapeType;
		this.drawingPanel = drawingPanel;
	}

	public List<MouseEvent> getMouseEvents() {
		return mouseEvents;
	}

	public ShapeType getShapeType() {
		return shapeType;
	}

	public DrawingPanel getDrawingPanel() {
		return drawingPanel;
	}
	
	

}
