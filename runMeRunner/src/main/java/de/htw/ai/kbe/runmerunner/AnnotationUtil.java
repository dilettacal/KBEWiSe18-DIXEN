package de.htw.ai.kbe.runmerunner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to analyze annotations
 * @author Dixen
 *
 */
public class AnnotationUtil {	
	/**
	 * Analyses annotations of the given class
	 * @param classToOpen - class with annos
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
			System.out.println("Klasse nicht gefunden. Pfad: " + classToOpen);
			return false;
		} catch(NullPointerException e) {
			System.out.println("Ein Fehler ist aufgetreten!");
			return false;
		}		
		
		ArrayList[] countMethods = getAnnotatedMethods(methods, obj);
		writeMethodsToFile(countMethods);
		return true;
	}
	
	public static void writeMethodsToFile(ArrayList[] allMethods) {
		//Liste der Methoden mit Annotation RunMe
		ArrayList<String> methodsWithRunMesAnnos = new ArrayList<String>();
		//Liste der Methoden Mit Annotation aber kein RunMe
		ArrayList<String> methodsWithoutRunMesAnnos = new ArrayList<String>();
		//Liste der Methoden ohne Annotationen ueberhaupt
		ArrayList<String> methodsWithOutAnnos = new ArrayList<String>();
		//Liste der Methoden mit Annotation RunMe, welche aber nicht ausfuehrbar sind
		ArrayList<String> methodsWithRunMesAnnosNotRunnable = new ArrayList<String>();
		
		methodsWithRunMesAnnos = allMethods[0];
		methodsWithoutRunMesAnnos = allMethods[1];
		methodsWithOutAnnos = allMethods[2];
		methodsWithRunMesAnnosNotRunnable = allMethods[3];
		
		
		File f = new File("report.txt");
		
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(f);
			bw = new BufferedWriter(fw);
			bw.write("Methodennamen ohne @RunMe: ");
			bw.newLine();
			for(String m: methodsWithoutRunMesAnnos) {
				bw.write(m);
				bw.newLine();
			}
			for(String m: methodsWithOutAnnos) {
				bw.write(m);
				bw.newLine();
			}
			bw.newLine();
			
			bw.write("Methodennamen mit @RunMe: ");
			bw.newLine();
			for(String m: methodsWithRunMesAnnos) {
				bw.write(m);
				bw.newLine();
			}
			bw.newLine();
			
			bw.write("Nicht-invokierbare Methoden mit @RunMe: ");
			bw.newLine();
			for(String m: methodsWithRunMesAnnosNotRunnable) {
				bw.write(m);
				bw.newLine();
			}
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	    try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
								//System.out.println(m.getName());
								if(params.length > 0)
									throw new InvocationTargetException(null, "nicht invokierbar");
								m.invoke(obj,objs);
								methodsWithRunMesAnnos.add(m.getName());
								System.out.println("Keine Exception fuer annotierte Methode mit RunMe: " + m.getName());
							}  catch (InvocationTargetException e) {
								System.out.println("Invocation Problem mit " + m.getName());
								
								methodsWithRunMesAnnosNotRunnable.add(m.getName());
								//System.out.println(e.getTargetException());
								e.printStackTrace();
							}
							catch (IllegalAccessException e) {
								System.out.println("IllegalAccess Problem mit " + m.getName());
								methodsWithRunMesAnnos.add(m.getName());
								System.out.println(e.getMessage());
							} catch (IllegalArgumentException e) {
								System.out.println("IllegalArgument Problem mit " + m.getName());
								methodsWithRunMesAnnos.add(m.getName());
								System.out.println(e.getMessage());
							}
							
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
