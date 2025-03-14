package view;

public interface ContainerInterface {
	default void initialize() {
		createComponents();
		arrangeComponents();
		setProperties();
		addEventHandler();
	}

	void createComponents();

	void arrangeComponents();

	void setProperties();

	void addEventHandler();
}
