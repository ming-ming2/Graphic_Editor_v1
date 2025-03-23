package state;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

public class GZoomManager extends GStateManager {
	private static GZoomManager instance;

	private double zoomFactor = 1.0;
	private int zoomLevelIndex = 5; // 기본값 (100%)

	private static final List<Double> ZOOM_LEVELS = Arrays.asList(0.1, // 10%
			0.25, // 25%
			0.5, // 50%
			0.75, // 75%
			0.9, // 90%
			1.0, // 100% (기본값)
			1.25, // 125%
			1.5, // 150%
			2.0, // 200%
			3.0, // 300%
			4.0, // 400%
			5.0 // 500%
	);

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
		zoomLevelIndex = 5; // 100% 인덱스
		zoomFactor = ZOOM_LEVELS.get(zoomLevelIndex);
		notifyObservers();
	}

	public void setCustomZoom(double factor) {
		if (factor >= ZOOM_LEVELS.get(0) && factor <= ZOOM_LEVELS.get(ZOOM_LEVELS.size() - 1)) {
			zoomFactor = factor;

			// 가장 가까운 줌 레벨 인덱스 찾기
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

	// 모델 좌표를 화면 좌표로 변환
	public Point modelToView(Point modelPoint) {
		return new Point((int) (modelPoint.x * zoomFactor), (int) (modelPoint.y * zoomFactor));
	}

	// 화면 좌표를 모델 좌표로 변환
	public Point viewToModel(Point viewPoint) {
		return new Point((int) (viewPoint.x / zoomFactor), (int) (viewPoint.y / zoomFactor));
	}

	// 모델 사각형을 화면 사각형으로 변환
	public Rectangle modelToView(Rectangle modelRect) {
		return new Rectangle((int) (modelRect.x * zoomFactor), (int) (modelRect.y * zoomFactor),
				(int) (modelRect.width * zoomFactor), (int) (modelRect.height * zoomFactor));
	}

	// 화면 사각형을 모델 사각형으로 변환
	public Rectangle viewToModel(Rectangle viewRect) {
		return new Rectangle((int) (viewRect.x / zoomFactor), (int) (viewRect.y / zoomFactor),
				(int) (viewRect.width / zoomFactor), (int) (viewRect.height / zoomFactor));
	}
}