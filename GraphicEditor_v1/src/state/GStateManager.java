package state;

public interface GStateManager<T> {
	void addObserver(T observer);

	void notifyObservers();

}
