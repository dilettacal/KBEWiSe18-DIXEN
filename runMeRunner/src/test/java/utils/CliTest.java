package utils;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CliTest {

	private String validClassname, notValidClassname;
	private String validReportname, notValidReportname;
	private Cli testValidCli, testNotValidCli, testNotValidCli2;

	@Before
	public void setUp() throws Exception {
		// Valid data
		String validInput = "-c TestClassWithoutRunMes -o report.txt"; // -c de.htw.ai.runMeRunner -o report.txt
		String[] inputParameters = validInput.split(" ");
		validClassname = "de.htw.ai.kbe." + inputParameters[1];
		validReportname = inputParameters[3];
		testValidCli = new Cli(inputParameters);

		// Not valid data
		String notvalidInput = "-c -o report.txt";
		String[] notValidInputParams = notvalidInput.split(" ");
		testNotValidCli = new Cli(notValidInputParams);

		// Not valid data
		String notvalidInput2 = "-c NotExistingClass -o report.txt";
		String[] notValidInputParams2 = notvalidInput.split(" ");
		notValidClassname = notValidInputParams2[1];
		testNotValidCli2 = new Cli(notValidInputParams2);
	}

	@Test
	public void testParseMethodShouldReturnSetUpValues() {
		String[] results = testValidCli.parse();
		Assert.assertEquals(validClassname, results[0]);
		Assert.assertEquals(validReportname, results[1]);
	}

	@Test
	public void testPaseMethodShouldReturnNull() {
		String[] results = testNotValidCli.parse();
		Assert.assertNull(results);
		String[] results2 = testNotValidCli2.parse();
		Assert.assertNull(results2);
	}

}
