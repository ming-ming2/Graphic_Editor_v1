package state;

public interface GStateManager {
	abstract void addObserver(GObserver observer);

	abstract void notifyObservers();

}
