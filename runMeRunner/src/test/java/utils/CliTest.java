package utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CliTest {

	private String validClassname, notValidClassname;
	private String validReportname;
	private Cli testValidCli, testNotValidCli, testNotValidCli2, testNotValidCli3, testNotValidCli4, testNotValidCli5;
	private final String path="de.htw.ai.kbe.runmerunner.";

	@Before
	public void setUp() throws Exception {
		// Valid data
		String validInput = "-c TestClassWithoutRunMes -o testreport.txt"; 
		String[] inputParameters = validInput.split(" ");
		validClassname = path + inputParameters[1];
		validReportname = inputParameters[3];
		testValidCli = new Cli(inputParameters);

		// Not valid data
		String notvalidInput = "-c -o report.txt";
		String[] notValidInputParams = notvalidInput.split(" ");
		testNotValidCli = new Cli(notValidInputParams);

		// Not valid data
		String notvalidInput2 = "-c SomeClassName -o testreport.txt";
		String[] notValidInputParams2 = notvalidInput2.split(" ");
		notValidClassname = notValidInputParams2[1];
		testNotValidCli2 = new Cli(notValidInputParams2);
		
		String notValidInputOnlyOptionC = "-c";
		testNotValidCli3 = new Cli(new String[]{notValidInputOnlyOptionC});
		
		String notValidInputNoOption = "";
		testNotValidCli4 = new Cli(new String[] {notValidInputNoOption});
		String notValidInputRandomChars="xyz";
		testNotValidCli5 = new Cli(new String[] {notValidInputRandomChars});
	}

	@Test
	public void testParseMethodShouldReturnSetUpValues() {
		String[] results = testValidCli.parse();
		Assert.assertTrue(results[0].contains(validClassname));
		Assert.assertEquals(validReportname, results[1]);
	}

	@Test
	public void testParseMethodWithoutClassNameShouldReturnNull() {
		String[] results = testNotValidCli.parse();
		Assert.assertNull(results);
	}
	
	@Test
	public void testParseMethodNotExistingClassShouldReturnValidResult() {
		String[] results2 = testNotValidCli2.parse();
		Assert.assertNotNull(results2);
		Assert.assertTrue(results2[0].contains(notValidClassname));
	}
	
	@Test
	public void testParseMissingClassnameOptionShouldReturnNull() {
		Assert.assertNull(testNotValidCli3.parse());
	}
	
	@Test
	public void testParseEmptyOptionsShouldReturnNull() {
		Assert.assertNull(testNotValidCli4.parse());
	}
	
	@Test
	public void testParseRandomCharsOptionsShouldReturnNull() {
		Assert.assertNull(testNotValidCli5.parse());
	}
	

}
