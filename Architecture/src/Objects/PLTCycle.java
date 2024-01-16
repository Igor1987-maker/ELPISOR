package Objects;

import java.util.ArrayList;
import java.util.List;

import Enums.PLTCycleType;
import Enums.PLTStartLevel;

public class PLTCycle extends BasicObject {
	
	private int cycleNumber;
	private String startLevelCode;
	private List<TestQuestion> cycleQuestions = new ArrayList<>();


	private PLTStartLevel pltStartLevel;
	public PLTStartLevel getPltStartLevel() {
		return pltStartLevel;
	}

	public int getCycleNumber() {
		return cycleNumber;
	}

	public String getStartLevelCode() {
		return startLevelCode;
	}

	public List<TestQuestion> getCycleQuestions() {
		return cycleQuestions;
	}

	public PLTCycleType getCycleType() {
		return cycleType;
	}

	
	public PLTCycle(PLTStartLevel pltStartLevel, int cycleNumber,
			String startLevelCode, List<TestQuestion> cycleQuestions,
			PLTCycleType cycleType) {
		super();
		this.pltStartLevel = pltStartLevel;
		this.cycleNumber = cycleNumber;
		this.startLevelCode = startLevelCode;
		this.cycleQuestions = cycleQuestions;
		this.cycleType = cycleType;
	}

	public PLTCycle() {
		// TODO Auto-generated constructor stub
	}

	private PLTCycleType cycleType;
	
	public int getNumberOfQuestions()throws Exception{
		return cycleQuestions.size();
	}
	
	public void setQuestions(List<TestQuestion>questions){
		this.cycleQuestions.addAll(questions);
	}

}
