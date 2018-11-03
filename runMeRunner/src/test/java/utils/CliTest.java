package utils;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class CliTest {
	
	private String className;
	private String reportName;
	private Cli testCli;

	@Before
	public void setUp() throws Exception {
		className = "EineKlasse.java";
		reportName= "EinReport";
		String[] args = {className,reportName};
		testCli = new Cli(args);		
	}

	@Test
	public void testParseMethodShouldReturnSetUpValues() {
		String [] results = testCli.parse();
		Assert.assertEquals(className, results[0]);
//		Assert.assertEquals(className, results[1]);
//		Assert.assertEquals("o", results[2]);
//		Assert.assertEquals(reportName, results[3]);
	}

}
