package stock;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

public class StockCaretaker implements Runnable {

	// generise se vrednost na svakih 5 sekundi
	private static final int INTERVAL = 5000;

	private static StockCaretaker instance = new StockCaretaker();

	public static StockCaretaker instance() {
		return instance;
	}

	private Stock originator;
	private Db stockDb;
	private boolean log;
	private boolean run;

	private StockCaretaker() {
		stockDb = new Db();
		log = true;
		run = true;
	}
	
	public void stop() {
		run = false;
	}

	public void setOriginator(Stock originator) {
		this.originator = originator;
	}
	
	public void stopOutput() {
		log = false;
	}
	
	public void continueWithOutput() {
		log = true;
	}
	
	public void retrieve(Date momentInPast) {
		originator.setMemento(stockDb.get(momentInPast).getValue().getState());
	}

	@Override
	public void run() {
		while (run) {
			try {
				stockDb.save(originator.createMemento());
				originator.setMemento(randomStockValue());
				if (log) {
					System.out.println(originator);
				}
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private double randomStockValue() {
		return new Random().nextDouble() * 100;
	}

	private static class Db {

		// pamtimo vrednosti 12 sati unazad
		private static final int HOURS = 12;
		private static final int HISTORY_SIZE = calculateHistorySize();

		private static int calculateHistorySize() {
			int seconds = HOURS * 60 * 60;
			return seconds / INTERVAL * 1000;
		}

		private TreeMap<Date, Memento<Double>> history;

		Db() {
			history = new TreeMap<>();
		}

		void save(Memento<Double> memento) {
			if (history.size() == HISTORY_SIZE) {
				Iterator<Date> keySet = history.keySet().iterator();
				keySet.next();
				keySet.remove();
			}
			history.put(Calendar.getInstance().getTime(), memento);
		}

		Entry<Date, Memento<Double>> get(Date date) {
			return history.floorEntry(date);
		}
	}

}
