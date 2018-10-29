package de.htw.ai.kbe;

import java.text.DecimalFormat;

import de.htw.ai.kbe.runmerunner.RunMe;

public class ClassWithRunMes {

    private String fieldPrivate;
     
    protected Integer integerFieldProtected;
    
    public DecimalFormat dfFieldPublic;

    static Integer integerFieldStatic;
    
    Integer integerFieldDontRunMe;
    
    @RunMe
    public static String method() {
        System.out.println("In method: ich bin public und static!");
        return "method".toUpperCase();
    }
    
    @Deprecated
	String method1() {
		System.out.println("In method1: ich bin package-private!");
		return "method1".toUpperCase();
	}

	@RunMe
	public boolean method2(int a) {
		System.out.println("In method2: ich bin public!");
		return true;
	}
	
	@RunMe
	public boolean method3() throws NullPointerException {
		System.out.println("In method3: ich bin privat!");
		return true;
	}
	
    boolean method4(String input, Integer inti) {
        return true;
    }
	
	public void method5() {
		System.out.println("Ich werde nicht gefunden!");
	}
}
