package esa.ffhs.ch;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.bind.JAXBException;

public class ServerMain {

	public static void main(String[] args) throws FileNotFoundException, IOException, JAXBException {
			
			esa.ffhs.ch.CityLoader loader = new esa.ffhs.ch.CityLoader();
			YahooWeatherData WeatherData = new YahooWeatherData(loader.loadAll());
			
			Thread t1 = new Thread(WeatherData);
			t1.start();
									
	}
}

	