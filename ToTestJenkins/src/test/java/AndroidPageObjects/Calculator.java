package AndroidPageObjects;

import org.openqa.selenium.By;

import BaseClasses.DataInjection;

public class Calculator {
	
	DataInjection di;
	
	private By idKeyPadNum(String key) { return By.id("com.android.calculator2:id/digit_"+key);}
	private By idKeyPadOperator(String key) { return By.id("com.android.calculator2:id/op_"+key);}
	private By idKeyPadEqual = By.id("com.android.calculator2:id/eq");
	private By idResultField = By.className("android.widget.EditText");
	
	public Calculator(DataInjection di)
	{
		this.di = di;
	}
	
	public boolean validateCalc()
	{
		boolean res = true;
		res = di.isExisting(idKeyPadEqual);
		di.takeScreenShot();
		if(res) di.report("The Calculator is opened successfully.");
		else di.report("The Calculator is NOT opened.");
		return res;
	}
	
	public boolean validateClickKeyNum(String key)
	{
		boolean res = true;
		res = di.click(idKeyPadNum(key));
		if(res) di.report("The "+key+" is tapped successfully.");
		else di.report("The "+key+" is NOT tapped.");
		di.takeScreenShot();
		return res;
	}
	
	public boolean validateClickKeyOp(String op)
	{
		boolean res = true;String key="";
		By loc = null ;
		if(op.contains("add") || op.contains("+")) {key="add";loc = idKeyPadOperator(key);}
		else if(op.contains("subtract")||op.contains("minus")||op.contains("-")) {key="sub";loc = idKeyPadOperator(key);}
		else if(op.contains("ult")||op.contains("*")||op.contains("x")) {key="mul";loc = idKeyPadOperator(key);}
		else if(op.contains("ide")||op.contains("/")) {key="div";loc = idKeyPadOperator(key);}
		else if(op.contains("qua")||op.contains("=")) {loc =idKeyPadEqual;}
		res = di.click(loc);
		if(res) di.report("The "+key+" is tapped successfully.");
		else di.report("The "+key+" is NOT tapped.");
		di.takeScreenShot();
		return res;
	}
	
	public boolean validateCalcResult(String key1, String op, String key2)
	{
		boolean res = true;
		int r=0;
		int k1 = Integer.valueOf(key1);
		int k2 = Integer.valueOf(key2);
		if(op.contains("add") || op.contains("+")) {r=k1+k2;}
		else if(op.contains("subtract")||op.contains("minus")||op.contains("-")) {r=k1-k2;}
		else if(op.contains("ult")||op.contains("*")||op.contains("x")) {r=k1*k2;}
		else if(op.contains("ide")||op.contains("/")) {r=k1/k2;}
		String txt = di.getText(idResultField);
		di.takeScreenShot();
		res = Integer.valueOf(txt).equals(r);
		if(res) di.report("The result of "+key1+" "+op+" "+key2+" is displayed as "+txt+" which is expected.");
		else di.report("The result of "+key1+" "+op+" "+key2+" is displayed as "+txt+" which is NOT expected. The expected result was "+r);
		return res;
	}

}
