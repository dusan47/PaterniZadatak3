package stock;

public class Stock implements Originator<Double> {

	private double value;
	
	public Stock(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}

	@Override
	public void setMemento(Double state) {
		value = state;
	}

	@Override
	public Memento<Double> createMemento() {
		return new StockMemento(value);
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
