package de.htw.ai.kbe.runmerunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Klasse zur Analyse der Annotationen
 * @author Dixen
 *
 */
public class AnnotationUtil {	
	/**
	 * Analysiert die Annotationen der übergebenen Klasse
	 * @param classToOpen
	 * @param javaClass
	 */
	public static boolean analyzeClass(String classToOpen) throws InstantiationException, IllegalAccessException{
		Method[] methods = null;
		Class clazz = null;
		Object obj = null;

		// Klassen aufrufen
		try {		
			clazz = Class.forName(classToOpen);
			obj = clazz.newInstance(); 
			System.out.println(clazz.getName());
			methods = clazz.getDeclaredMethods();
		} catch (ClassNotFoundException e) {
			System.out.println("Klasse nicht gefunden");
			return false;
		} catch(NullPointerException e) {
			System.out.println("Ein Fehler ist aufgetreten!");
			return false;
		}		
		
		ArrayList[] countMethods = getAnnotatedMethods(methods, obj);
		
		//System.out.println("Anzahl annotierter Methoden: " + countMethods.get(0));
		//System.out.println("Anzahl nicht annotierter Methoden " + countMethods.get(1));

		return true;
	}
	
	public static ArrayList[] getAnnotatedMethods(Method[] methods, Object obj) {
		int runMeCounter = 0;
		int withoutRunMeCounter = 0;
		int methodWithoutAnnos = 0;
		//Liste der Methoden mit Annotation RunMe
		ArrayList<String> methodsWithRunMesAnnos = new ArrayList<String>();
		//Liste der Methoden Mit Annotation aber kein RunMe
		ArrayList<String> methodsWithoutRunMesAnnos = new ArrayList<String>();
		//Liste der Methoden ohne Annotationen ueberhaupt
		ArrayList<String> methodsWithOutAnnos = new ArrayList<String>();
		//Liste der Methoden mit Annotation RunMe, welche aber nicht ausfuehrbar sind
		ArrayList<String> methodsWithRunMesAnnosNotRunnable = new ArrayList<String>();
		
		ArrayList[] group = new ArrayList[4];
		//group[0] = methodsWithRunMesAnnos;
		
		// Methoden
		if (methods.length != 0) {
			for (Method m : methods) {
				// ==== Annotationen der METHODEN ====//
				Annotation[] methodAnnos = m.getDeclaredAnnotations();
				if (methodAnnos.length == 0)
					methodsWithOutAnnos.add(m.getName());
				else {
					// Rueckgabe der Methoden mit Annotation
					for (Annotation a : methodAnnos) {
						if (a instanceof RunMe) {
							try {
								Parameter[] params = m.getParameters();
								Object [] objs = params;
								System.out.println(m.getName());
								m.invoke(obj,objs);
								methodsWithRunMesAnnos.add(m.getName());
							}  catch (InvocationTargetException e) {
								
								methodsWithRunMesAnnosNotRunnable.add(m.getName());
								System.out.println(e.getMessage());
							}
							catch (IllegalAccessException e) {
							
								methodsWithRunMesAnnos.add(m.getName());
								System.out.println(e.getMessage());
							} catch (IllegalArgumentException e) {
								
								methodsWithRunMesAnnos.add(m.getName());
								System.out.println(e.getMessage());
							}
							System.out.println("annotierte Methode mit RunMe: " + m.getName());
							//methodsWithRunMesAnnos.add(m.getName());
							//runMeCounter += 1;
						}
						//Deprecated oder andere Annos
						else {
							System.out.println("annotierte Methode ohne RunMe: " + m.getName());
							methodsWithoutRunMesAnnos.add(m.getName());
							//withoutRunMeCounter+=1;
						}
					}
				}
				
				//methodWithoutAnnos = methods.length - runMeCounter - withoutRunMeCounter;
			}
		}
		
		group[0] = methodsWithRunMesAnnos;
		group[1] = methodsWithoutRunMesAnnos;
		group[2] = methodsWithOutAnnos;
		group[3] = methodsWithRunMesAnnosNotRunnable;
		
		return group;
		
	}

}
