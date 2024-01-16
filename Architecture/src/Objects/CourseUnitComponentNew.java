package Objects;

import java.util.ArrayList;
import java.util.List;

public class CourseUnitComponentNew extends BasicObject {
	
	List<String[]> componentSteps = new ArrayList<String[]>();
	String componentSubject = null;	
	String componentSkillId = null;	
	String componentSkillName = null;	
	
	
	public String getComponentSkillName() {
		return componentSkillName;
	}

	public void setComponentSkillName(String componentSkillName) {
		this.componentSkillName = componentSkillName;
	}

	public String getComponentSkill() {
		return componentSkillId;
	}

	public void setComponentSkill(String componentSkill) {
		this.componentSkillId = componentSkill;
	}

	public String getComponentSubject() {
		return componentSubject;
	}

	public void setComponentSubject(String componentSubject) {
		this.componentSubject = componentSubject;
	}

	public List<String[]> getComponentSteps() {
		return componentSteps;
	}

	public void setComponentSteps(List<String[]> componentSteps) {
		this.componentSteps = componentSteps;
	}
	
	public void addUnitComponent(String[] componentStep){
		componentSteps.add(componentStep);
	}

}

