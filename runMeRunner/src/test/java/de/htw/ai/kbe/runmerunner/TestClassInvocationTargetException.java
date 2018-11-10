package de.htw.ai.kbe.runmerunner;

import java.text.DecimalFormat;

import de.htw.ai.kbe.runmerunner.RunMe;

public class TestClassInvocationTargetException {

	@RunMe
	public void myMethod() {
		throw new NullPointerException();
	}
}
    
