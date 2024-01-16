package tests.edo.newux;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jcifs.smb.SmbFile;
import services.PageHelperService;
import services.TextService;

public class testDataValidation extends BasicNewUxTest {

	String jsonFilesRoot = "smb://"+PageHelperService.physicalPath+"/Runtime/Metadata//Courses/";
	
	String lessonFilesPath = "files/lessonFiles/";
	SmbFile[] coursesFiles;
	SmbFile[] lessons;
	   
	public void verifyMultiResTabOrderFromLessonJS(int stepNum, int taskNum, String [] [] typesInOrder, String lessonJS) throws Exception {

    	
    	//lessons = getJsonFiles(lessonFilesRoot);
    	//String lessonJS = textService.readFileContent(lessons[0]);
				
		JSONObject lessonJson = getJsonObjectFromLessonJS(lessonJS);
		JSONObject taskJson = getTaskObjectByStepLessonTaskNum(lessonJson, stepNum, taskNum);
		JSONArray resSetsArray = taskJson.getJSONArray("resLink");
		String [] resSet = new String [3];
		
		for (int i = 0; i < resSetsArray.length(); i++) {
			resSet[i] = resSetsArray.get(i).toString();
		}
								
		String resSetType = "";
		
		for (int i = 0; i < typesInOrder.length; i++) {
		
			String setIndex = resSet[i];
			resSetType = lessonJson.getJSONObject("resSets").getJSONObject(setIndex).getString("typeId");
			resSetType = convertResSetTypeId(resSetType);
			
			testResultService.assertEquals(typesInOrder[i] [0], resSetType, "Resource set type appears not as set in lesson.js");
			
		}
		
		
	}
	
	public boolean isShowToolsEnabledInLessonJS(int stepNum, int taskNum, String lessonJS) throws Exception {

    	boolean isEnabled = false;
		
		JSONObject lessonJson = getJsonObjectFromLessonJS(lessonJS);
		JSONObject taskJson = getTaskObjectByStepLessonTaskNum(lessonJson, stepNum, taskNum);
		String keyValue = taskJson.getString("showTools");
		int value = Integer.valueOf(keyValue);
		
		if (value == 0) isEnabled = false;
		else if (value == 1) isEnabled = true;
		else testResultService.addFailTest("Show Tools key in JSON not valid");
				
		return isEnabled;
			
		
	}
	
	public void testCompareJsonFilessAndDB() throws Exception {

		coursesFiles = getJsonFiles(jsonFilesRoot);
		
		String[] courses = BasicNewUxTest.courses;
		
		
		testResultService.assertEquals(coursesFiles.length, courses.length,
				"Coursed and courseFiles are not in the same size");

		for (int i = 0; i < coursesFiles.length; i++) {
			try {
				System.out
						.println("reading file: " + coursesFiles[i].getPath());
				// JsonObject jsonObject = new JsonParser().parse(
				// textService.readFileContent(coursesFiles[i]))
				// .getAsJsonObject();

				JSONObject jsonObject = new JSONObject(
						textService.readFileContent(coursesFiles[i]));
				checkJsonFile(courses[i], jsonObject);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				testResultService.addFailTest(e.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	public void verifyJsonValueByKey(JSONObject json, String key, String expectedValue) throws Exception {

		String actualValue = json.getString(key);
		testResultService.assertEquals(expectedValue, actualValue,"Value of key "+ key +" does not match to expected");
		

	}
	
	public JSONObject setJsonValueByKeyInFeaturesList(JSONObject json, String key, String newValue) throws Exception {
		
		if (newValue.equalsIgnoreCase("true") || newValue.equalsIgnoreCase("false")) return json.put(key, Boolean.valueOf(newValue));
		else return json.put(key, newValue);
		

	}
	
	public JSONObject setJsonValueByKeyInAppSet(JSONObject json, String key, String newValue) throws Exception {
		
		JSONObject instObj = json.getJSONObject("institutions");
		
		if (newValue.equalsIgnoreCase("true") || newValue.equalsIgnoreCase("false")) instObj.put(key, Boolean.valueOf(newValue));
		else instObj.put(key, newValue);
		
		return json.put("institutions", instObj);
		

	}
	
	public JSONObject removeJsonKeyInAppSet(JSONObject json, String key) throws Exception {
		
		JSONObject instObj = json.getJSONObject("institutions");
		
		instObj.remove(key);
		
		return json.put("institutions", instObj);
		

	}
		
	private void checkJsonFile(String courseId, JSONObject jsonObject)
			throws Exception {

		report.startStep("Testing course id: " + courseId);

		// check all units in course
		List<String[]> units = dbService.getCourseUnitDetils(courseId);

		JSONArray unitJsonArray = jsonObject.getJSONArray("Children");
		if (testResultService.assertEquals(units.size(),
				unitJsonArray.length(),
				"Units in json are not the same size as actual course units") == true) {

			for (int i = 0; i < units.size(); i++) {
				// check all compnents in units

				startStep("Testing unit id: " + units.get(i)[0]);

//				System.out.println("Unit: " + unitJsonArray.get(i).toString());
				JSONObject unitObject = unitJsonArray.getJSONObject(i);
				String unitName = unitObject.getString("Name");
				int unitId = unitObject.getInt("NodeId");
				int unitCourseId = unitObject.getInt("CourseId");
				int unitParentId = unitObject.getInt("ParentNodeId");
				String nodeType = unitObject.getString("CourseNodeType");

				testResultService.assertEquals(unitName, units.get(i)[1],
						"Unit not does not match");
				testResultService.assertEquals(unitId,
						Integer.parseInt(units.get(i)[0]),
						"Unit id does not match");
				testResultService.assertEquals(unitCourseId,
						Integer.parseInt(courseId),
						"Unit course id does not match");

				testResultService.assertEquals(unitParentId,
						Integer.parseInt(courseId), "Parent id does not match");
				testResultService.assertEquals(nodeType, "Unit",
						"Node type does not match");

				List<String[]> components = dbService
						.getComponentDetailsByUnitId(units.get(i)[0]);

				JSONArray compJsonArray = unitJsonArray.getJSONObject(i)
						.getJSONArray("Children");

				testResultService
						.assertEquals(components.size(),
								compJsonArray.length(),
								"DB components and json components are not in the same size");

				for (int j = 0; j < compJsonArray.length(); j++) {
					JSONObject compObject = compJsonArray.getJSONObject(j);

					int compCourseId = compObject.getInt("CourseId");
					int compId = compObject.getInt("NodeId");
					String compName = compObject.getString("Name");
					int compParentId = compObject.getInt("ParentNodeId");
					String compNodeType = compObject
							.getString("CourseNodeType");

					testResultService.assertEquals(Integer.parseInt(courseId),
							compCourseId, "Course id is not the same");
					testResultService.assertEquals(compId,
							Integer.parseInt(components.get(j)[0]),
							"Component id is not the same");
					testResultService
							.assertEquals(compName, components.get(j)[1],
									"Lesson name is not the same");
					testResultService.assertEquals(compParentId,
							Integer.parseInt(units.get(i)[0]),
							"Component parent id is not the same");
					testResultService.assertEquals(compNodeType, "Lesson");

				}

			}

		}

	}

	private JSONObject getJsonObjectFromLessonJS (String lessonJS) throws Exception {
		
		lessonJS = lessonJS.replace("var lesson = ", "").replace(";", "").replace(" ", "");
		
		JSONObject jsonObject = new JSONObject(lessonJS);
	
		return jsonObject;
	}
	
	public JSONArray getJsonArrayFromCurriculumJS (String curriculumJS) throws Exception {
		
		curriculumJS = curriculumJS.replace("var licensedCourses=", "");
				
		JSONArray jsonArray = new JSONArray(curriculumJS);
	
		return jsonArray;
	}
	
	public JSONObject getCefrMapAsJson() throws Exception {

		String textFile = "files/cefrMap/cefrMap.json";
		TextService textService = new TextService();  
		String cefrMap = textService.getTextFromFile(textFile, Charset.defaultCharset());
		
		return new JSONObject(cefrMap);
		
	}
	
	public int getJsonObjIndexByKeyValue (JSONArray jArray, String key, String value) throws Exception {
		
		int i;
		
		for (i=0; i < jArray.length(); i++) {
			JSONObject jObj = jArray.getJSONObject(i);
			String actValue = jObj.getString(key);
			if (actValue.equals(value)) break; 
		}
		
		return i;
	}
	
	private JSONObject getTaskObjectByStepLessonTaskNum (JSONObject lessonJson, int stepNum, int taskNum) throws Exception {
		
		JSONArray stepsJsonArray = lessonJson.getJSONArray("steps");
		JSONObject tasksJsonObj = stepsJsonArray.getJSONObject(stepNum-1);
		JSONArray taskJsonArray = tasksJsonObj.getJSONArray("tasks");
		JSONObject itemJsonObj = taskJsonArray.getJSONObject(taskNum-1);
	
		return itemJsonObj;
	}
		
	public SmbFile[] getJsonFiles(String filesRoot) throws IOException {

		return netService.getFilesInFolder(filesRoot);

	}

	private String convertResSetTypeId (String typeId) throws Exception {
		
		String type = "";
		
		switch (typeId) {
		
		case "2":
			type = "Reading";
			break;
		case "6": 
			type = "Listening";
			break;
		case "4": 
			type = "Listening";
			break;
		}
		
	return type;
	
	}
	
}
