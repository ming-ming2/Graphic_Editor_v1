package shapes;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

import global.ShapeType;

public class ShapeRepository {
	public static Shape getShape(ShapeType type, List<MouseEvent> events){
		if (events == null || events.isEmpty()) {
            return null;
        }
		  switch(type) {
          case ShapeType.Rectangle:
        	  MouseEvent startEvent = events.get(0);
              MouseEvent endEvent = events.get(events.size() - 1);
              
              Point startPoint = startEvent.getPoint();
              Point endPoint = endEvent.getPoint();
              
              int width = Math.abs(endPoint.x - startPoint.x);
              int height = Math.abs(endPoint.y - startPoint.y);
              int x = Math.min(startPoint.x, endPoint.x);
              int y = Math.min(startPoint.y, endPoint.y);
              
              return new Rectangle(new Point(x, y), width, height);
          // 추가 예정
          default:
              return null;
      }
		
	}

}
