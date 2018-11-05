package utils;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class CliTest {
	
	private String validClassname, notValidClassname;
	private String validReportname, notValidReportname;
	private Cli testValidCli, testNotValidCli;
	


	@Before
	public void setUp() throws Exception {
		//Valid data
		String validInput = "-c de.htw.ai.runMeRunner -o report.txt";
		String[] inputParameters = validInput.split(" ");
		validClassname = inputParameters[1];
		validReportname= inputParameters[3];
		testValidCli = new Cli(inputParameters);	
		
		//Not valid data
		String notvalidInput = "-c -o report.txt";
		String[] notValidInputParams = notvalidInput.split(" ");
		testNotValidCli = new Cli(notValidInputParams);
	}

	@Test
	public void testParseMethodShouldReturnSetUpValues() {
		String [] results = testValidCli.parse();
		Assert.assertEquals(validClassname, results[0]);
		Assert.assertEquals(validReportname, results[1]);
	}
	

}
