package Objects;

import java.util.ArrayList;
import java.util.List;

public class CourseUnit extends BasicObject {
	List<UnitComponent>unitComponenst=new ArrayList<UnitComponent>();

	public List<UnitComponent> getUnitComponent() {
		return unitComponenst;
	}

	public void setUnitComponent(List<UnitComponent> unitComponents) {
		this.unitComponenst = unitComponents;
	}
	public void addUnitComponent(UnitComponent unitComponent){
		unitComponenst.add(unitComponent);
	}
}
