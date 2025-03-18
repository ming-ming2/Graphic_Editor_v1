package view;

public interface GContainerInterface {
	default void initialize() {
		setProperties();
		addEventHandler();
	}

	void createComponents();

	void arrangeComponents();

	void setProperties();

	void addEventHandler();
}
