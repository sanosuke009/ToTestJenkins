package BaseClasses;

import AtomicMethods.ElementActs;
import io.cucumber.java.Scenario;

public class DataInjection extends ElementActs{
	
	public DataInjection(Scenario sc) {
		System.out.println("Scenario = "+sc.getName());
		scenario = sc;
	}
	 public void setKeyWord(String keyword)
	 {
		 keyWord = keyword;
	 }
}
