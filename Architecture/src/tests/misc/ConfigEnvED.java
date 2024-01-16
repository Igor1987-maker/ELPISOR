package tests.misc;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import Enums.FeaturesList;
import services.PageHelperService;
import tests.edo.newux.BasicNewUxTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigEnvED extends BasicNewUxTest {
	
	Boolean useAppSet;
	String [] institutions;
	
	@Before
	public void setup() throws Exception {
				
		super.setup();
		// set Features List values in app_set.jst of Institution folder
		useAppSet = Boolean.valueOf(configuration.getAutomationParam("useAppSet", "useAppSet"));
		institutions = new String [] {};
		//autoInstitution.getInstitutionId(), aeonInstId, coursesInstId, localInstId, argInstId

	}
	
	
	
	// must run first in this class so other settings apply to QA builds !!!
	@Test
	public void applyEdTmsLabelQA() throws Exception {

		// move to QA label the latest CI builds of TMS & ED
		
		String parameter = configuration.getAutomationParam("setQA", "setQA");
		
		String EdsourceBrnach_local="";
		String TmssourceBrnach_local="";
		
		parameter = "true";
		
		if (parameter.equalsIgnoreCase("true")) {
			

			if (PageHelperService.branchCI.equalsIgnoreCase("EDUI_CI_dev")) {		
				pageHelper.buildDefinitionED = "EDUI_CI_dev";
				pageHelper.buildDefinitionTMS = "TMS_CI_dev";
				
				EdsourceBrnach_local = PageHelperService.sourceBranch + "dev";
				TmssourceBrnach_local = PageHelperService.sourceBranch + "dev";
				
				}

			if (PageHelperService.branchCI.equalsIgnoreCase("EDUI_CI_RC_2023_13")) {
				pageHelper.buildDefinitionED = "EDUI_CI_RC_2023_13";
				pageHelper.buildDefinitionTMS = "TMS_CI_RC_2023_12";

				EdsourceBrnach_local = PageHelperService.sourceBranch + "RC_2023_13";
				TmssourceBrnach_local = PageHelperService.sourceBranch + "RC_2023_12";

			}

			if (PageHelperService.branchCI.equalsIgnoreCase("EDUI_CI_RC_2023_12")) {
				pageHelper.buildDefinitionED = "EDUI_CI_RC_2023_12";
				pageHelper.buildDefinitionTMS = "TMS_CI_RC_2023_12";

				EdsourceBrnach_local = PageHelperService.sourceBranch + "RC_2023_12";
				TmssourceBrnach_local = PageHelperService.sourceBranch + "RC_2023_12";

			}

			String ed_buildId = pageHelper.getBuildId(pageHelper.buildDefinitionED,EdsourceBrnach_local);
			sleep(2);
			String tms_buildId = pageHelper.getBuildId(pageHelper.buildDefinitionTMS,TmssourceBrnach_local);
			
			
			pageHelper.SetBuildQAInCI_CMD("lock", ed_buildId);
			sleep(2);
			pageHelper.SetBuildQAInCI_CMD("lock", tms_buildId);
		}
	}
	
	@Test
	public void learningAreaConfig() throws Exception {
		
		// set learning area mode - old or new angular

		String param = configuration.getAutomationParam("angLA", "angLA");
		if (param != null && !param.isEmpty()) setFeature(FeaturesList.learningAsAng, param);
		
	}

	
	//@Test
	public void timeOnLessonConfig() throws Exception {
		
		// set if to report time on lesson data to data gathering service
		
		String param = configuration.getAutomationParam("tol", "tol");
		if (param != null && !param.isEmpty())
			setFeature(FeaturesList.timeOnLesson, param);
	}
	
	@Test
	public void linkQaBuildsEdTms() throws Exception {
		
		// change web.config of latest QA CI builds TMS & ED to be associated
		//pageHelper.linkEdTmsQaBuildsInCIBy_CI_WebSite();
		
		String parameter = configuration.getAutomationParam("linkEdTms", "linkEdTms");
		//parameter = "true";
		if (parameter.equalsIgnoreCase("true")) {
			//pageHelper.linkEdTmsQaBuildsInCI();
			pageHelper.linkEdTmsQaBuildsInCI_CMD();
//			pageHelper.linkEdTmsQaBuildsInCIBy_CI_WebSite();
		
		}
		
	}
	
	//@Test
	public void speachRecEngineConfig() throws Exception {
		
		// set SR engine Verto or SPEAKING PAL
		
		String param = configuration.getAutomationParam("sr", "sr");
		//param = "true";
		
		if (param != null && !param.isEmpty())
			setFeature(FeaturesList.SR_ENGINE, param.toUpperCase());
		
	}
	
	
	private void setFeature(FeaturesList feature, String param) throws Exception {
		
		if (useAppSet){
			
			setFeatureInAppSetForInstitutions(feature, param);
			
		} else {
			
			pageHelper.setFeaturesListPerInstallation(feature, param);
			removeFeatureInAppSetForInstitutions(feature, param);
			
		}
	}
	
	private void setFeatureInAppSetForInstitutions(FeaturesList feature, String param) throws Exception {
		
		for (int i = 0; i < institutions.length; i++) {
			pageHelper.setFeaturesListInAppSetByInstitution(institutions[i], feature, param, false);
		}
		
	}
	
	private void removeFeatureInAppSetForInstitutions(FeaturesList feature, String param) throws Exception {
		
		for (int i = 0; i < institutions.length; i++) {
			
			if (institutions[i] != null)
				pageHelper.setFeaturesListInAppSetByInstitution(institutions[i], feature, param, true);
		}
		
	}
	
	
}
