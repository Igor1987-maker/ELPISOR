package services.AppServices;

import services.StudentService;

public class AppStudentService extends StudentService{
	
	AppDbService appDbService;
	
	public AppStudentService() throws Exception{
		appDbService=new AppDbService();
		this.dbService=appDbService;
	}

	

}
