package state;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import shapes.GShape;

public class GClipboard extends GStateManager {
	private static GClipboard instance;
	private List<GShape> shapes = new ArrayList<>();
	private Point pasteOffset = new Point(20, 20);

	private GClipboard() {
	}

	public static GClipboard getInstance() {
		if (instance == null) {
			instance = new GClipboard();
		}
		return instance;
	}

	public void copy(List<GShape> shapes) {
		this.shapes.clear();

		for (GShape shape : shapes) {
			try {
				this.shapes.add(deepCopy(shape));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		notifyObservers();
	}

	public List<GShape> paste() {
		List<GShape> pastedShapes = new ArrayList<>();

		for (GShape shape : shapes) {
			try {
				GShape copiedShape = deepCopy(shape);

				copiedShape.move(pasteOffset.x, pasteOffset.y);

				pastedShapes.add(copiedShape);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		pasteOffset.x += 20;
		pasteOffset.y += 20;

		if (pasteOffset.x > 200 || pasteOffset.y > 200) {
			pasteOffset.x = 20;
			pasteOffset.y = 20;
		}

		return pastedShapes;
	}

	public boolean hasContents() {
		return !shapes.isEmpty();
	}

	@SuppressWarnings("unchecked")
	private <T> T deepCopy(T object) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(object);
		oos.flush();

		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bis);
		return (T) ois.readObject();
	}

	public Point getCurrentOffset() {
		return new Point(pasteOffset); // 현재 오프셋의 복사본 반환
	}

	public void clear() {
		shapes.clear();
		pasteOffset = new Point(20, 20);
		notifyObservers();
	}
}