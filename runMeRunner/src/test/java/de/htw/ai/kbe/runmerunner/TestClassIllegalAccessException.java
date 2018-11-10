package de.htw.ai.kbe.runmerunner;

import java.text.DecimalFormat;

import de.htw.ai.kbe.runmerunner.RunMe;

public class TestClassIllegalAccessException {
	
	@RunMe
    protected String method1() {
        return "method1".toUpperCase();
    }
	
	@RunMe
	String method2() {
		return "method2".toUpperCase();
	}

	@RunMe
	private String method3() {
		return "method3".toUpperCase();
	}
}
    
