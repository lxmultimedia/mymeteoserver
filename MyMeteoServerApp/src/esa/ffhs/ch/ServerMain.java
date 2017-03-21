package esa.ffhs.ch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import javax.xml.bind.JAXBException;


public class ServerMain {

	private static DBConnection dbconnection = null;
	static Timestamp DateCheck = new Timestamp(System.currentTimeMillis());

	public static void main(String[] args)
			throws FileNotFoundException, IOException, JAXBException, URISyntaxException {

		dbconnection = new DBConnection();
		dbconnection.createConnection();

		esa.ffhs.ch.CityLoader loader = new esa.ffhs.ch.CityLoader();
		YahooWeatherData WeatherData = new YahooWeatherData(loader.loadAll());

		Thread t1 = new Thread(WeatherData);
		t1.start();

		try {
			t1.join();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
}
