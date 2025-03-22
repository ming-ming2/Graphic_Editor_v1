package file;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import shapes.GShape;
import state.GDrawingStateManager;

public class GFileManager {

	public static void saveToFile(File file) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
			List<GShape> shapes = GDrawingStateManager.getInstance().getShapes();
			out.writeObject(shapes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void loadFromFile(File file) {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
			List<GShape> shapes = (List<GShape>) in.readObject();
			GDrawingStateManager.getInstance().setShapes(shapes);
			GDrawingStateManager.getInstance().setCurrentFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveAsImage(File file, JComponent component) {
		try {
			BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = image.createGraphics();
			component.paint(g2d);
			g2d.dispose();
			ImageIO.write(image, "png", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage loadImage(File file) {
		try {
			return ImageIO.read(file);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}