package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import stock.Stock;
import stock.StockCaretaker;

public class Client {

	private Stock originator;
	private StockCaretaker caretaker;

	public Client() {
		originator = new Stock(0);
		caretaker = StockCaretaker.instance();
		caretaker.setOriginator(originator);
	}

	public void start() {
		new Thread(caretaker).start();
		waitForInput();
		caretaker.stop();
	}

	private void waitForInput() {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
			String line;
			line = in.readLine();
			while (!line.equals("stop")) {
				if (line.equals("")) {
					try {
						caretaker.stopOutput();
						System.out.println("Unesite momenat iz proslosti u formatu dd/MM/yyyy hh:mm:ss");
						caretaker.retrieve(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(in.readLine()));
						System.out.println("Vrednost akcije u zadatom momentu bila je: " + originator);
						// malo sacekamo pre ponovnog ispisivanja stanja
						Thread.sleep(2000);
					} catch (ParseException e) {
						System.out.println("Vreme nije uneto u zadatom formatu!");
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						caretaker.continueWithOutput();
					}

				}
				line = in.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException {
		new Client().start();
	}

}
