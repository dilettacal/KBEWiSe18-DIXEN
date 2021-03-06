package de.htw.ai.kbe.runmerunner;

import de.htw.ai.kbe.runmerunner.RunMe;

public class MyClassWithRunMes {

    @RunMe
    public static String method0() {
        System.out.println("In method0: ich bin public und static!");
        return "method".toUpperCase();
    }
    
	@RunMe
	String method1() {
		System.out.println("In method1: ich bin package-private!");
		return "method1".toUpperCase();
	}

	@RunMe
	@Deprecated
    protected String method2() {
        System.out.println("In method2: ich bin protected und deprecated!");
        return "method1".toUpperCase();
    }
	
	@RunMe
	public boolean method3() {
		System.out.println("In method3: ich bin public!");
		return true;
	}
	
	@RunMe
	private boolean method4() {
		//Diese Methode soll durch Reflection IllegalAccessException werfen, da private
		System.out.println("In method4: ich bin privat!");
		return true;
	}
	
	@RunMe
    public boolean method4(String input) {
		//Diese Methode soll durch Reflection IllegalArgumentException werfen
        System.out.println("In method4: ich bekomme einen Parameter!");
        return true;
    }
	
	@Override
	@RunMe
    public String toString() {
        System.out.println("In toString: ");
        return MyClassWithRunMes.class.getSimpleName();
    }
	
	public void noRmR1() {
		System.out.println("Ich werde nicht aufgerufen!");
	}
	
	void noRmR2() {
        System.out.println("Ich werde nicht aufgerufen!");
    }
	
	private void noRmR3() {
        System.out.println("Ich werde nicht aufgerufen!");
    }
	
	//=== Von mir hinzugefuegt zum Testen
	
	@RunMe
	public void myMethod() {
		throw new NullPointerException();
	}
	
	@Deprecated
	protected void noRmR4() {
		System.out.println("Ich werde nicht aufgerufen!");
	}

	@Override
	public boolean equals(Object arg0) {
		System.out.println("Ich werde nicht aufgerufen!");
		return super.equals(arg0);
	}
	
	
	
}
