package esa.ffhs.ch;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.YahooWeatherService.LimitDeclaration;
import com.github.fedy2.weather.data.unit.DegreeUnit;

public class YahooWeatherData implements Runnable {

	private List<String> cityList;
	
	public YahooWeatherData(List<String> cList) {
		cityList = cList;
	}
	
	@Override
	public void run() {
		for(String c:cityList) {
			System.out.println(c);
			System.out.println("--------------------------------------");
			YahooWeatherService service = null;
			try {
				service = new YahooWeatherService();
			} catch (JAXBException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			LimitDeclaration result = service.getForecastForLocation(c, DegreeUnit.CELSIUS);
			try {
				System.out.println(result.all().toString());
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("--------------------------------------");
		}

	}

}
