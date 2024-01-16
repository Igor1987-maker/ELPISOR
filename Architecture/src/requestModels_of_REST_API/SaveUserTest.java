package requestModels_of_REST_API;

import java.util.ArrayList;

public class SaveUserTest {

	private ArrayList<Answer> answers;
	private int testTime;
	
	public void setAnswers(ArrayList<Answer> answers) {
		this.answers = answers;
	}
	public void setTestTime(int testTime) {
		this.testTime = testTime;
	}
	//public SaveUserTest(ArrayList<Answer> answers, int testTime) {
		//super();
	//	this.answers = answers;
	//	this.testTime = testTime;
	//}

	
	
	public class Answer{
		
		private int itemId;
		private ArrayList<int[]> itemAnswers;
		private int mark;
		
		public void setItemId(int itemId) {
			this.itemId = itemId;
		}
		public void setItemAnswers(ArrayList<int[]> itemAnswers) {
			this.itemAnswers = itemAnswers;
		}
		public void setMark(int mark) {
			this.mark = mark;
		}
	//	public Answer(int itemId, ArrayList<ArrayList<Integer>> itemAnswers, int mark) {
		//	super();
	//		this.itemId = itemId;
	//		this.itemAnswers = itemAnswers;
	//		this.mark = mark;
		}
		
		
		
		
		
		
		
	}
	



