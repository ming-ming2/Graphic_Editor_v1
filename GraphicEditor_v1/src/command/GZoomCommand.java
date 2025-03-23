package command;

import state.GZoomManager;
import type.GZoomType;

public class GZoomCommand implements GCommand {
	private GZoomType zoomType;
	private double oldZoomFactor;
	private double newZoomFactor;

	public GZoomCommand(GZoomType zoomType) {
		this.zoomType = zoomType;
		this.oldZoomFactor = GZoomManager.getInstance().getZoomFactor();
	}

	@Override
	public void execute() {
		GZoomManager zoomManager = GZoomManager.getInstance();

		switch (zoomType) {
		case ZOOM_IN:
			zoomManager.zoomIn();
			break;
		case ZOOM_OUT:
			zoomManager.zoomOut();
			break;
		case ZOOM_RESET:
			zoomManager.resetZoom();
			break;
		}

		this.newZoomFactor = zoomManager.getZoomFactor();
	}

	@Override
	public void unexecute() {
		GZoomManager.getInstance().setCustomZoom(oldZoomFactor);
	}
}