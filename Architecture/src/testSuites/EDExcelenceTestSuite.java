package testSuites;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import testCategories.AngularLearningArea;
import testCategories.inProgressTests;
import testCategories.edoNewUX.EDExcellenceArea;
import testCategories.edoNewUX.LoginPage;
import testCategories.edoNewUX.SanityTests;
import tests.edo.newux.BasicNewUxTest;
import tests.edo.newux.EDExcellenceTests;
import tests.edo.newux.ExternalLoginAndIntegration;

@RunWith(Categories.class)
@IncludeCategory({EDExcellenceArea.class})
@ExcludeCategory({LoginPage.class, AngularLearningArea.class,inProgressTests.class})
@SuiteClasses({EDExcellenceTests.class})
public class EDExcelenceTestSuite extends BasicNewUxTest{

}
