package Enums;

public enum MemoryGamePractice {
	A("2"),
	a("3"),
	B("4"),
	b("5"),
	C("6"),
	c("7"),
	D("8"),
	d("9"),
	E("10"),
	e("11"),
	F("12"),
	f("13");
	
	private final String id;
	
	private MemoryGamePractice(String ID) {
		id = ID;
	}
	
	public String getID() {
		return id;
	}
}
