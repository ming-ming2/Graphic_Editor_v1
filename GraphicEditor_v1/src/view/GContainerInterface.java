package view;

public interface GContainerInterface {
	default void initialize() {
		createComponents();
		arrangeComponents();
		setAttributes();
		addEventHandler();
	}

	void createComponents();

	void arrangeComponents();

	void setAttributes();

	void addEventHandler();
}
