package state;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

public class GZoomManager extends GStateManager {
	private static GZoomManager instance;

	private double zoomFactor = 1.0;
	private int zoomLevelIndex = 5;

	private static final List<Double> ZOOM_LEVELS = Arrays.asList(0.1, 0.25, 0.5, 0.75, 0.9, 1.0, 1.25, 1.5, 2.0, 3.0,
			4.0, 5.0);

	private GZoomManager() {
	}

	public static GZoomManager getInstance() {
		if (instance == null) {
			instance = new GZoomManager();
		}
		return instance;
	}

	public double getZoomFactor() {
		return zoomFactor;
	}

	public String getZoomPercentage() {
		return Math.round(zoomFactor * 100) + "%";
	}

	public void zoomIn() {
		if (zoomLevelIndex < ZOOM_LEVELS.size() - 1) {
			zoomLevelIndex++;
			zoomFactor = ZOOM_LEVELS.get(zoomLevelIndex);
			notifyObservers();
		}
	}

	public void zoomOut() {
		if (zoomLevelIndex > 0) {
			zoomLevelIndex--;
			zoomFactor = ZOOM_LEVELS.get(zoomLevelIndex);
			notifyObservers();
		}
	}

	public void resetZoom() {
		zoomLevelIndex = 5;
		zoomFactor = ZOOM_LEVELS.get(zoomLevelIndex);
		notifyObservers();
	}

	public void setCustomZoom(double factor) {
		if (factor >= ZOOM_LEVELS.get(0) && factor <= ZOOM_LEVELS.get(ZOOM_LEVELS.size() - 1)) {
			zoomFactor = factor;

			int closestIndex = 0;
			double minDiff = Math.abs(ZOOM_LEVELS.get(0) - factor);

			for (int i = 1; i < ZOOM_LEVELS.size(); i++) {
				double diff = Math.abs(ZOOM_LEVELS.get(i) - factor);
				if (diff < minDiff) {
					minDiff = diff;
					closestIndex = i;
				}
			}

			zoomLevelIndex = closestIndex;
			notifyObservers();
		}
	}

	public Point modelToView(Point modelPoint) {
		return new Point((int) (modelPoint.x * zoomFactor), (int) (modelPoint.y * zoomFactor));
	}

	public Point viewToModel(Point viewPoint) {
		return new Point((int) (viewPoint.x / zoomFactor), (int) (viewPoint.y / zoomFactor));
	}

	public Rectangle modelToView(Rectangle modelRect) {
		return new Rectangle((int) (modelRect.x * zoomFactor), (int) (modelRect.y * zoomFactor),
				(int) (modelRect.width * zoomFactor), (int) (modelRect.height * zoomFactor));
	}

	public Rectangle viewToModel(Rectangle viewRect) {
		return new Rectangle((int) (viewRect.x / zoomFactor), (int) (viewRect.y / zoomFactor),
				(int) (viewRect.width / zoomFactor), (int) (viewRect.height / zoomFactor));
	}
}
