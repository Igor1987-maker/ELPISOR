package services.AppServices;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import services.Configuration;

public class AppConfig extends Configuration{
	
	
	
	
	
	public AppConfig() {
		
		// TODO Auto-generated constructor stub
	}

	public AppConfig(String str) throws Exception{
		System.out.println("Setup of appConfig");
		LoadConfig();
	}
	
	public void LoadConfig()  {
		// TODO Auto-generated method stub
		try {
			InputStream input = null;
			String path = "C:\\automation\\progressApp.properties";
			input = new FileInputStream(path);
			properties.load(input);
			System.out.println();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	
	
	
	
	

}
