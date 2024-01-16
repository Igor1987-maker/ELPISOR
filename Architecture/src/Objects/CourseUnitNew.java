package Objects;

import java.util.ArrayList;
import java.util.List;

public class CourseUnitNew extends BasicObject {
	
	List<CourseUnitComponentNew> unitComponents = new ArrayList<CourseUnitComponentNew>();
		
	public List<CourseUnitComponentNew> getUnitComponents() {
		return unitComponents;
	}

	public void setUnitComponents(List<CourseUnitComponentNew> unitComponents) {
		this.unitComponents = unitComponents;
	}

	public void addUnitComponent(CourseUnitComponentNew unitComponent){
		unitComponents.add(unitComponent);
	}
}
