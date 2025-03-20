package state;

import java.util.ArrayList;
import java.util.List;

public abstract class GStateManager {
	protected List<GObserver> observers = new ArrayList<>();

	public void addObserver(GObserver observer) {
		observers.add(observer);
	}

	public void notifyObservers() {
		for (GObserver observer : observers) {
			observer.update();
		}
	}

}
