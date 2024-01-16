package Enums;

public enum AutoParams {
	remoteMachine("remote.machine"),
	machine("machine"),
	sutUrl("sut.url"),
	envFile("envFile"),
	srdebug("srdebug"),
	slaveName("slaveName"),
	scenarioFile("scenarioFile"),
	timeout("timeout")
	
	;
	
	private final String text;
	
	private AutoParams(final String text){
		this.text=text;
	}
	
	@Override 
	public String toString(){
		return text;
	}

}
