package esa.ffhs.ch;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.YahooWeatherService.LimitDeclaration;
import com.github.fedy2.weather.data.unit.DegreeUnit;

import com.google.gson.*;

public class YahooWeatherData implements Runnable {

	private List<String> cityList;
	private ArrayList<String> JSONResult;

	public YahooWeatherData(List<String> cList) {
		cityList = cList;
		JSONResult = new ArrayList<String>();
	}

	@Override
	public void run() {

		while (true) {

			try {
				DBConnection.instance.DeleteOldRows();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			
			
			String json = "";
			for (String c : cityList) {
				
				// Sleep
				try {
					Thread.sleep(60000);;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
				
				System.out.println(c);
				System.out.println("--------------------------------------");
				YahooWeatherService service = null;
				try {
					service = new YahooWeatherService();
				} catch (JAXBException e1) {
					e1.printStackTrace();
				}
				LimitDeclaration result = service.getForecastForLocation(c, DegreeUnit.CELSIUS);
				try {
					json = new Gson().toJson(result.all());
					DBConnection.instance.writeJSONObject(json);
					JSONResult.add(json);
				} catch (JAXBException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			System.out.println(gson.toJson(getJSONResult()));

			JSONResult.clear();
		}
	}


	public ArrayList<String> getJSONResult() {
		return JSONResult;
	}

	public void setJSONResult(ArrayList<String> jSONResult) {
		JSONResult = jSONResult;
	}
}
