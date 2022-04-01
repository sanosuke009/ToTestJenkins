package Runner;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
		features = "src/test/resources/Features/TestFeature1.feature",
		glue = {"StepDefs"},
		tags = "",
		plugin = {"json:Results/cucumberjson/cucumber.json"}
		)
public class TestngRunner extends AbstractTestNGCucumberTests{
	
	@Override
	@DataProvider(parallel=false)
	public Object[][] scenarios()
	{
		return super.scenarios();
	}
	
}
