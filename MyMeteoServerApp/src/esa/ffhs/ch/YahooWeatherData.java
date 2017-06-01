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
	public synchronized void run() {

		while (true) {

			try {
				DBConnection.instance.DeleteOldRows();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}


			String json = "";
			for (String c : cityList) {

				String location = c.substring(0,c.indexOf(","));
				String locationCode = c.substring(c.indexOf(",")+1);

				// Sleepfor  1min
				try {
					Thread.sleep(60000);
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
					StringBuilder jsonstr = new StringBuilder(json);
					//System.out.println(jsonstr);
					json = jsonstr.delete(jsonstr.indexOf("![CDATA[")-6, jsonstr.indexOf("]]")+8).toString();

					if(json.indexOf("![CDATA[")>0) {
						jsonstr = new StringBuilder(json);
						json = jsonstr.delete(jsonstr.indexOf("![CDATA[")-6, jsonstr.indexOf("]]")+8).toString();
					}
					DBConnection.instance.writeJSONObject(jsonstr.toString(),location,locationCode);
					JSONResult.add(json);
				} catch (JAXBException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

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
