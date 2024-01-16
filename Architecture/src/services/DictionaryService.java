package services;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.springframework.stereotype.Service;

@Service("DictionaryService")
public class DictionaryService extends GenericService {

	protected Properties properties = new Properties();

	public void loadDictionaryFile(String path) throws IOException {
		
		FileInputStream input = new FileInputStream(path);
		InputStreamReader in=new InputStreamReader(input,"UTF-8");
		
//		FileInputStream input = new FileInputStream(path);
		properties.load(input);
	}

	public String getProperty(String key, String defaultValue) {

		return properties.getProperty(key, defaultValue);
	}

	public String getProperty(String key) {
		return getProperty(key, null);
	}

}
