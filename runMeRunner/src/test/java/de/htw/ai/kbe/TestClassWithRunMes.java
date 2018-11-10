package de.htw.ai.kbe;

import java.text.DecimalFormat;

import de.htw.ai.kbe.runmerunner.RunMe;

/**
 * Test class with RunMes
 * Total annotations- RunMes: 3
 * Total other annotations: 1
 * @author dixen
 *
 */
public class TestClassWithRunMes {

    private String fieldPrivate;
     
    protected Integer integerFieldProtected;
    
    public DecimalFormat dfFieldPublic;

    static Integer integerFieldStatic;
    
    Integer integerFieldDontRunMe;
    
    @RunMe
    public static String method() {
        System.out.println("In method");
        return "method".toUpperCase();
    }
    
    @Deprecated
	String method1() {
		System.out.println("In method1: package-private");
		return "method1".toUpperCase();
	}

	@RunMe
	public boolean method2(){
		//Diese Methode soll als nicht invokierbare Methode gelten
		System.out.println("In method2: public");
		throw new NullPointerException();
	}
	
	@RunMe
	private boolean method3(int b) {
		//Diese methode soll als nicht invokierbare Methode gelten
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