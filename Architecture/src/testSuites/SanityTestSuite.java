package testSuites;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import testCategories.AngularLearningArea;
import testCategories.edoNewUX.HomePage;
import testCategories.edoNewUX.LoginPage;
import testCategories.edoNewUX.SanityTests;
import testCategories.inProgressTests;
import tests.edo.newux.*;
import tests.tms.NewUxAssessmentsTests;
import tests.tms.NewUxCourseReports;
import tests.tms.NewUxTmsSettings;

@RunWith(Categories.class)
@IncludeCategory({SanityTests.class})
@ExcludeCategory({LoginPage.class, AngularLearningArea.class,inProgressTests.class,HomePage.class})
@SuiteClasses({ExternalLoginAndIntegration.class,CourseUnitContinueBtnTests2.class,LearningAreaStepsTests2.class,
	PracticesTestsByTypes2.class,NewUxCourseReports.class,NewUxAssessmentsTests.class,WebApi.class, NewUxTmsSettings.class, NewUxSelfRegistrationTests.class,AERaterMyWritingsTests2.class})
public class SanityTestSuite extends BasicNewUxTest{

	

}
