package services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Objects.AutoInstitution;

@Service("InstitutionService")
public class InstitutionService extends GenericService {
	
	@Autowired
	Configuration config;

	ArrayList<AutoInstitution> autoInstitutions = new ArrayList<AutoInstitution>();
	
	private AutoInstitution institution;

	

	public ArrayList<AutoInstitution> getAutoInstitutions() {
		return autoInstitutions;
	}
	
	public void init(){
		//init form properties file
		institution=new AutoInstitution();
		institution.setInstitutionId(config.getProperty("institution.id"));
		institution.setInstitutionName(config.getProperty("institution.name"));
		institution.setStudentUserName(config.getProperty("student.user.name"));
		institution.setStudentPassword(config.getProperty("student.user.password"));
		institution.setTeacherUserName(config.getProperty("teacher.username"));
		institution.setTeacherPassword(config.getProperty("teacher.password"));
		
		
		
	}
	
	public void init (int id){
		switch(id){
			case 1:
			institution=new AutoInstitution("5231363", "automation", "student1", "autoTeacher", "12345", "12345", "automation.aspx");
				break;
			case 2:
				institution	=new AutoInstitution("5231363", "automation", "student2", "autoTeacher", "12345", "12345", "automation.aspx");
				break;
			
		}
	}
	

//	public void init() throws Exception {
//		AutoInstitution autoInstitution1=new AutoInstitution("5231363", "automation", "student1", "autoTeacher", "12345", "12345", "automation.aspx");
//		AutoInstitution autoInstitution2=new AutoInstitution("5231363", "automation", "student2", "autoTeacher", "12345", "12345", "automation.aspx");
//		AutoInstitution autoInstitution3=new AutoInstitution("5231363", "automation", "student3", "autoTeacher", "12345", "12345", "automation.aspx");
//		AutoInstitution autoInstitution4=new AutoInstitution("5231363", "automation", "student4", "autoTeacher", "12345", "12345", "automation.aspx");
//		AutoInstitution autoInstitution5=new AutoInstitution("5231363", "automation", "student5", "autoTeacher", "12345", "12345", "automation.aspx");
//		autoInstitutions.add(autoInstitution1);
//		autoInstitutions.add(autoInstitution2);
//		autoInstitutions.add(autoInstitution3);
//		autoInstitutions.add(autoInstitution4);
//		autoInstitutions.add(autoInstitution5);
//		

//	}

	public AutoInstitution getInstitution() {
		return institution;
	}

	public void setInstitution(AutoInstitution institution) {
		this.institution = institution;
	}

}
