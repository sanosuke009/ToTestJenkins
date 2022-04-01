package StepDefs;

import org.testng.Assert;

import AndroidPageObjects.Calculator;
import BaseClasses.DataInjection;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class StepsAndroidCalculator{
	
	DataInjection di;
	Calculator calc;
	/*
	@Before("@FirstScenario")
	public void setExtent()
	{
		em = new ExtentManager();
	}
	
	@After("@LastScenario")
	public void flushExtent()
	{
		b.terminateextent();
	}*/
	
	
	@Given("^I want to open the \"([^\"]*)\" in the \"([^\"]*)\" android device$")
	public void openAndroidDriver(String appname, String deviceid)
	{
		System.out.println("The background step");
		Assert.assertTrue(di.initDriver(deviceid+"_"+appname+".json"));
	}
	
	@Given("^I want to test the testcase for \"([^\"]*)\"$")
	public void setKeyW(String keyword)
	{
		System.out.println("I want to test the testcase for keyword="+keyword);
		di.setKeyWord(keyword);
		di.setMap();
		//b.startextent(b.getData("TestCaseNo"), b.getData("TestCaseDesc"));
	}

	@And("^I want the calculator to be opened in device$")
	public void waitForCalculator()
	{
		System.out.println("I want the calculator to be opened in device");
		calc = new Calculator(di);
		Assert.assertTrue(calc.validateCalc());
	}
	
	@And("^I tap number key \"([^\"]*)\" on keypad$")
	public void tapNumKey(String key)
	{
		System.out.println("I tap number key "+key+" on keypad");
		Assert.assertTrue(calc.validateClickKeyNum(di.getData(key)));
	}
	
	@And("^I tap operator key \"([^\"]*)\" on keypad$")
	public void tapOpKey(String key)
	{
		System.out.println("I tap operator key "+key+" on keypad");
		Assert.assertTrue(calc.validateClickKeyOp(di.getData(key)));
	}
	
	@Then("^I validate the result of \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
	public void validateResult(String key1, String op, String key2)
	{
		System.out.println("I validate the result of "+key1+op+key2);
		Assert.assertTrue(calc.validateCalcResult(di.getData(key1), di.getData(op), di.getData(key2)));
	}

}
