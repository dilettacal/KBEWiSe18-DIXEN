package de.htw.ai.kbe.runmerunner;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AnnotationUtilTest {
	
		private String classWithRunMes;
		private String classWithoutRunMes;
		private String notExistingClass;
		private List[] results;
		private String file = "testreport.txt";
		private String fileNoRunMe ="testreportNo.txt";

		@Before
		public void setUp() throws Exception {
			classWithRunMes = "de.htw.ai.kbe.TestClassWithRunMes";
			classWithoutRunMes = "de.htw.ai.kbe.TestClassWithoutRunMes";
			notExistingClass = "de.htw.ai.kbe.KeineAhnung";
			results = new ArrayList[4];
		}
		
		@Test
		public void testOpenExistingClassShouldReturnTrue() throws InstantiationException, IllegalAccessException {
			boolean result1 = AnnotationUtil.analyzeClass(classWithRunMes, file);
			boolean result2 = AnnotationUtil.analyzeClass(classWithoutRunMes, fileNoRunMe);
			Assert.assertTrue((result1 == true) && (result2 == true));
		}

		@Test 
		public void testOpenNotExistingClassShouldReturnFalse() throws InstantiationException, IllegalAccessException {
			boolean result = AnnotationUtil.analyzeClass(notExistingClass, file);
			Assert.assertFalse(result);
		}
		
	/*	
		@Test
		public void testCountMethodsWithRunMesInTestClassWithRunMesShouldReturn2() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
			
			Method[] methods = null;
			Class clazz = null;
			Object obj = null;
			clazz = Class.forName(classWithRunMes);
			obj = clazz.newInstance(); 
			System.out.println(clazz.getName());
			methods = clazz.getDeclaredMethods();
			results = AnnotationUtil.getAnnotatedMethods(methods, obj);
			int result = results[0].size();
			//annotated method that are runnable
			Assert.assertEquals(2,result);
		}*/
		
		
		@Test
		public void testCountAnnotatedMethodsWithoutRunMesInTestClassWithRunMesShouldReturn1() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
			
			Method[] methods = null;
			Class clazz = null;
			Object obj = null;
			clazz = Class.forName(classWithRunMes);
			obj = clazz.newInstance(); 
			System.out.println(clazz.getName());
			methods = clazz.getDeclaredMethods();
			results = AnnotationUtil.getAnnotatedMethods(methods, obj);		
		
			//method with other annotations than RunMe
			Assert.assertTrue(results[1].size() == 1); //1 Deprecated
		}
		
		@Test
		public void testCountMethodsWithoutAnnotationsInTestClassWithRunMesShouldReturn2() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
			
			Method[] methods = null;
			Class clazz = null;
			Object obj = null;
			clazz = Class.forName(classWithRunMes);
			obj = clazz.newInstance(); 
			System.out.println(clazz.getName());
			methods = clazz.getDeclaredMethods();
			results = AnnotationUtil.getAnnotatedMethods(methods, obj);
			
			//methods that are not annotated 
			Assert.assertTrue(results[2].size() == 2);			
		}
		
		@Test
		public void testCountNotRunnableMethodsInTestClassWithRunMesShouldReturnXX() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
			
			Method[] methods = null;
			Class clazz = null;
			Object obj = null;
			clazz = Class.forName(classWithRunMes);
			obj = clazz.newInstance(); 
			System.out.println(clazz.getName());
			methods = clazz.getDeclaredMethods();
			results = AnnotationUtil.getAnnotatedMethods(methods, obj);
			//methods with RunMe that are not runnable
			
			//TODO: Das muss noch geklaert werden
			//Was wird als nicht ausfuehrbare Methode betrachtet?
			int notrunnableMethods = results[3].size();
			Assert.assertEquals(2, notrunnableMethods);			
		}
		
		/*
		 * Tests fuer Klassen ohne RunMes
		 */
		@Test
		public void testCountAnnotationsWithRunMesInTestClassWithoutRunMesShouldReturnZero() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
			Method[] methods = null;
			Class clazz = null;
			Object obj = null;
			clazz = Class.forName(classWithoutRunMes);
			obj = clazz.newInstance(); 
			System.out.println(clazz.getName());
			methods = clazz.getDeclaredMethods();
			results = AnnotationUtil.getAnnotatedMethods(methods, obj);
			
			Assert.assertTrue(results[0].size() == 0);
		}
		
		/*
		@Test
		public void testCountAnnotatedMethodsWithoutRunMesInClassWithoutRunMesShouldReturn3() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
			Method[] methods = null;
			Class clazz = null;
			Object obj = null;
			clazz = Class.forName(classWithoutRunMes);
			obj = clazz.newInstance(); 
			System.out.println(clazz.getName());
			methods = clazz.getDeclaredMethods();
			results = AnnotationUtil.getAnnotatedMethods(methods, obj);
			int annotatedMethodsNoRunMe = results[1].size();
			Assert.assertEquals(annotatedMethodsNoRunMe, 3);
		}*/
		
		/*
		@Test
		public void testCountNotAnnotatedMethodsInClassWithoutRunMesShouldReturn3() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
			Method[] methods = null;
			Class clazz = null;
			Object obj = null;
			clazz = Class.forName(classWithoutRunMes);
			obj = clazz.newInstance(); 
			System.out.println(clazz.getName());
			methods = clazz.getDeclaredMethods();
			results = AnnotationUtil.getAnnotatedMethods(methods, obj);
			int notAnnotatedMethods = results[2].size();
			//TODO: Die Methoden mit Override und SuppressWarnings werden als nicht-annotierte Methoden gefangen!
			Assert.assertEquals(notAnnotatedMethods, 2);
		}*/
		
		

}
