package esa.ffhs.ch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class CityLoader {

	// working path
	private String currentDirectory;
	// City List
	private String _inputfile = "citylist.txt";
	// List for city list
	private List<String> cityList;

	public CityLoader() throws IOException, URISyntaxException {
		// debug
		// currentDirectory = System.getProperty("user.dir") + "/bin/";
		// deploy
		File filePath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		currentDirectory = filePath.getParentFile().toString() + "/";
		cityList = new ArrayList<String>();
	}

	List<String> loadAll() throws FileNotFoundException, IOException {

		try (BufferedReader br = new BufferedReader(new FileReader(currentDirectory + this._inputfile))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				this.cityList.add(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
		}
		return cityList;
	}

	public List<String> getCityList() {
		return cityList;
	}

	public void setCityList(List<String> cityList) {
		this.cityList = cityList;
	}

}
