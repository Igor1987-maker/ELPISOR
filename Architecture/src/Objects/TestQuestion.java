package Objects;

import Enums.TestQuestionType;

public class TestQuestion extends BasicObject {

	String[] correctAnswers;
	

	String[]answersDestinations;
	String[] incoreectAnswers;
	int[]blankAnswers;
	boolean booleanAnswer;

	public TestQuestion(String[] correctAnswers, String[] answersDestinations,
			String[] incoreectAnswers, int[] blankAnswers,
			 TestQuestionType questionType) {
		super();
		this.correctAnswers = correctAnswers;
		this.answersDestinations = answersDestinations;
		this.incoreectAnswers = incoreectAnswers;
		this.blankAnswers = blankAnswers;
		this.booleanAnswer = booleanAnswer;
		this.questionType = questionType;
	}

	TestQuestionType questionType;

	public TestQuestionType getQuestionType() {
		return questionType;
	}

	public String[] getCorrectAnswers() {
		return correctAnswers;
	}

	public String[] getIncoreectAnswers() {
		return incoreectAnswers;
	}
	
	public boolean getBooleanAnswer(){
		return this.booleanAnswer;
	}
	public String[] getAnswersDestinations() {
		return answersDestinations;
	}

	public int[] getBlankAnswers() {
		return blankAnswers;
	}

	public void setIncoreectAnswers(String[] incoreectAnswers) {
		this.incoreectAnswers = incoreectAnswers;
	}

	public void setQuestionType(TestQuestionType questionType) {
		this.questionType = questionType;
	}
	public void setCorrectAnswers(String[] correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	
}
