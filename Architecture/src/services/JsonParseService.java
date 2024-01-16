package services;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class JsonParseService {
	
	public JsonParseService(){};
	
	public List<String[]>getArrayListFromJson(JSONObject object,String arrayObjName,String[]names)throws Exception{
		JSONArray array=new JSONArray(object);
		List<String[]>list;
		for(int i=0;i<array.length();i++){
//			list.add(array.getJSONObject(i).get);
//			System.out.println(array.getJSONObject(i).getString(names[0])); 
			
		}
		return null;
	}

}
