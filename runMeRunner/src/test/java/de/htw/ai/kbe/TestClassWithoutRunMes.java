package de.htw.ai.kbe;

import java.text.DecimalFormat;

import de.htw.ai.kbe.runmerunner.RunMe;

public class TestClassWithoutRunMes {

    private String fieldPrivate;
     
    protected Integer integerFieldProtected;
    
    public DecimalFormat dfFieldPublic;

    static Integer integerFieldStatic;
    
    Integer integerFieldDontRunMe;
    
    public static String method() {
        System.out.println("In method");
        return "method".toUpperCase();
    }
    
    @Deprecated
	String method1() {
		System.out.println("In method1: package-private");
		return "method1".toUpperCase();
	}

	public boolean method2(){
		System.out.println("In method2: public");
		throw new NullPointerException();
	}
	
	
	private boolean method3(int b) {
		System.out.println("In method3: private");
		return true;
	}
	
    boolean method4(String input, Integer inti) {
        return true;
    }
	
	public void method5() {
		System.out.println("Ich werde nicht gefunden!");
	}
}
