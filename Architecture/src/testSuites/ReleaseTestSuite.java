package testSuites;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testCategories.AngularLearningArea;
import testCategories.edoNewUX.HomePage;
import testCategories.edoNewUX.LoginPage;
import testCategories.edoNewUX.ReleaseTests;
import testCategories.inProgressTests;
import tests.edo.newux.*;
import tests.edo.newux.REST_API.*;
import tests.tms.NewUxAssessmentsTests;

@RunWith(Categories.class)
@Categories.IncludeCategory({ReleaseTests.class})
@Categories.ExcludeCategory({LoginPage.class, AngularLearningArea.class, inProgressTests.class, HomePage.class})
@Suite.SuiteClasses({AERaterMyWritingsTests2.class, AssessmentsTests.class, EDToiecTests.class,
        MyProfile.class, MessagesPageTests.class, NewUxAssessmentsTests.class, testsInLearningAreaTests2.class,
        TestEnvironmentTests.class, AssessmentsController.class, AssessmentsDescriptorController.class, AssignmentController.class,
        ProgressController.class, PromotionController.class,UserProfileController.class,UserTestV1_Controller.class})


public class ReleaseTestSuite {
}
