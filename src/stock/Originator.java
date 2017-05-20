package stock;

public interface Originator<T> {

	void setMemento(T state);
	
	Memento<T> createMemento();
}
