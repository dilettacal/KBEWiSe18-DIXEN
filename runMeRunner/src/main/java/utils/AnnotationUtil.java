package utils;

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

import de.htw.ai.kbe.runmerunner.RunMe;

/**
 * Class to analyze annotations
 * @author Dixen
 *
 */
public class AnnotationUtil {	
	/**
	 * Analyisiert die als Parameter uebergebene Klasse
	 * @param classToOpen - die zu analysierende Klasse 
	 * @throws IllegalAccessException wenn gefundene Klasse nicht zugreifbar ist
	 * @throws InstantiationException wenn gefundene Klasse nicht instanzierbar ist
	 * @return true wenn Klasse analysierbar ist, false wenn ein Problem auftritt
	 */
	public static boolean analyzeClass(String classToOpen, String filename) throws InstantiationException, IllegalAccessException {
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
			System.out.println("Klasse nicht gefunden im Pfad: " + classToOpen);
			return false;
		} catch(NullPointerException e) {
			System.out.println("Ein Fehler ist aufgetreten!");
			return false;
		}		
		
		ArrayList[] countMethods = getAnnotatedMethods(methods, obj);
		writeMethodsToFile(countMethods, filename);
		return true;
	}
	
	/**
	 * Schreibt Informationen ueber die Klasse in das Report
	 * @param allMethods die zu analysierenden Methoden
	 * @param filename der Filename
	 */
	public static void writeMethodsToFile(ArrayList[] allMethods, String filename) {
		//Liste der Methoden mit Annotation RunMe
		ArrayList<String> methodsWithRunMesAnnos = new ArrayList<String>();
		//Liste der Methoden Mit Annotation aber kein RunMe
		ArrayList<String> methodsWithoutRunMesAnnos = new ArrayList<String>();
		//Liste der Methoden ohne Annotationen ueberhaupt
		ArrayList<String> methodsWithOutAnnos = new ArrayList<String>();
		//Liste der Methoden mit Annotation RunMe, welche aber nicht ausfuehrbar sind
		//Grund: Exception
		ArrayList<String> methodsWithRunMesAnnosNotRunnable = new ArrayList<String>();
		//Grund: Parameter in Methodendeklaration
		ArrayList<String> methodsWithRunMesAnnosNotRunnable2 = new ArrayList<String>();
		
		methodsWithRunMesAnnos = allMethods[0];
		methodsWithoutRunMesAnnos = allMethods[1];
		methodsWithOutAnnos = allMethods[2];
		methodsWithRunMesAnnosNotRunnable = allMethods[3];
	//	methodsWithRunMesAnnosNotRunnable2 = allMethods[4]; nicht mehr notwendig
		
		
		File f = new File(filename);
		
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
			//bw.write("(Grund: eine Exception wurde geworfen)");
			bw.newLine();
			for(String m: methodsWithRunMesAnnosNotRunnable) {
				bw.write(m);
				bw.newLine();
			}
			bw.newLine();
			/*
			 * Nicht mehr notwendig
			//bw.write("(Grund: Parameter in der Methodendeklaration)");
			bw.newLine();
			for(String m: methodsWithRunMesAnnosNotRunnable2) {
				bw.write(m);
				bw.newLine();
			}*/
			bw.newLine();
		} catch (IOException e) {
			System.out.println("Ein Problem ist beim Zugriff auf die Datei aufgetreten: " + e.getMessage());
		}		
		
	    try {
			bw.close();
		} catch (IOException e) {
			System.out.println("Ein Problem ist beim Zugriff auf die Datei aufgetreten: " + e.getMessage());
		}
	}
	
	public static ArrayList[] getAnnotatedMethods(Method[] methods, Object obj) {
		int runMeCounter = 0;
		int withoutRunMeCounter = 0;
		int methodWithoutAnnos = 0;
		//Char ist entweder e (Exception) oder p (Params) als Ermittlung der Ursache der nicht invozierbaren Methode 
		//-> Methode der jeweiligen Liste hinzugefügt
		
		//char reasonNotIvoke = 'e'; //nicht mehr notwendig
		
		//Liste der Methoden mit Annotation RunMe
		ArrayList<String> methodsWithRunMesAnnos = new ArrayList<String>();
		//Liste der Methoden Mit Annotation aber kein RunMe
		ArrayList<String> methodsWithoutRunMesAnnos = new ArrayList<String>();
		//Liste der Methoden ohne Annotationen ueberhaupt
		ArrayList<String> methodsWithOutAnnos = new ArrayList<String>();
		
		//Liste der Methoden mit Annotation RunMe, welche aber nicht ausfuehrbar sind
		//Grund: Exception innerhalb der Methode aufgerufen
		ArrayList<String> methodsNotRunnableException = new ArrayList<String>();
		
		//Grund: Parameter in Methodendeklaration //nicht mehr notwendig
		//ArrayList<String> methodsNotRunnableParameterInMethodDeclaration = new ArrayList<String>();
		
		
		ArrayList[] group = new ArrayList[4];
		
		// Methoden
		if (methods.length != 0) {
			for (Method m : methods) {
				//reasonNotIvoke = 'e'; //nicht mehr notwendig
				
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
								/*
								 * Soll nicht von uns geworfen werden!
								 * if(params.length > 0) {
									reasonNotIvoke = 'p';
									throw new InvocationTargetException(null, "nicht invokierbar");
								}*/				
								m.invoke(obj,objs);
								methodsWithRunMesAnnos.add(m.getName());
							}  catch (InvocationTargetException e) {
								/* Passiert automatisch, soll nicht von uns gesteuert werden
								 * 
								 * if(reasonNotIvoke == 'p')
									methodsNotRunnableParameterInMethodDeclaration.add(m.getName() + ": " + e.getClass().getSimpleName());
								else if(reasonNotIvoke == 'e')
									methodsNotRunnableException.add(m.getName()+ ": " + e.getClass().getSimpleName());*/
								
								//Ausgabe in der Form: 'mehtodX: IllegalArgumentException'
								methodsNotRunnableException.add(m.getName()+ ": " + e.getClass().getSimpleName());
								methodsWithRunMesAnnos.add(m.getName());
							}
							
							//IllegalAccessException passiert bei private und package private Methoden
							catch (IllegalAccessException e) {	
								methodsNotRunnableException.add(m.getName()+ ": " + e.getClass().getSimpleName());
								methodsWithRunMesAnnos.add(m.getName());
							//IllegalArgumentException passiert bei Problemen mit Parametern
							//Quelle: https://www.tutorialspoint.com/javareflect/javareflect_method_invoke.htm
							} catch (IllegalArgumentException e) {
								methodsNotRunnableException.add(m.getName()+ ": " + e.getClass().getSimpleName());
								methodsWithRunMesAnnos.add(m.getName());
							}
						}
						//Deprecated oder andere Annos
						else {
							methodsWithoutRunMesAnnos.add(m.getName());
						}
					}
				}
			}
		}
		
		group[0] = methodsWithRunMesAnnos;
		group[1] = methodsWithoutRunMesAnnos;
		group[2] = methodsWithOutAnnos;
		group[3] = methodsNotRunnableException;
		//group[4] = methodsNotRunnableParameterInMethodDeclaration; //nicht mehr notwendig
		
		return group;
		
	}

}
