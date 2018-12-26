package utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import utils.AnnotationUtil;

public class AnnotationUtilTest {
	
		private String classWithRunMes, classWithInvocatioTargetEx, classWithIllegalArgExc, classWithIllegalAccessExc;
		private String notExistingClass;
		private List[] results;
		private String file = "testreport.txt";
		private String fileNoRunMe ="testreportNo.txt";
		private final String path = "de.htw.ai.kbe.runmerunner.";
		Method[] methods;
		Class clazz;
		Object obj;
		

		@Before
		public void setUp() throws Exception {
			classWithRunMes =  path + "TestClassWithRunMes";
			classWithInvocatioTargetEx = path +"TestClassInvocationTargetException";
			classWithIllegalArgExc = path +"TestClassIllegalArgumentException";
			classWithIllegalAccessExc = path +"TestClassIllegalAccessException";
			notExistingClass = path +"KeineAhnung";
			results = new ArrayList[4];
		}
		
		//=== Test: Zugriff auf Klasse === //
		
		@Test
		public void testOpenExistingClassShouldReturnTrue() throws InstantiationException, IllegalAccessException {
			boolean result1 = AnnotationUtil.analyzeClass(classWithRunMes, file);
			boolean result2 = AnnotationUtil.analyzeClass(classWithInvocatioTargetEx, fileNoRunMe);
			Assert.assertTrue((result1 == true) && (result2 == true));
		}

		@Test 
		public void testOpenNotExistingClassShouldReturnFalse() throws InstantiationException, IllegalAccessException {
			boolean result = AnnotationUtil.analyzeClass(notExistingClass, file);
			Assert.assertFalse(result);
		}
		
		//=== Test: Anzahl der Methoden mit, ohne RM und nicht invozierbar ===//
		@Test
		public void testCountMethodsWithRunMesInTestClassWithRunMesShouldReturn8() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
			clazz = Class.forName(classWithRunMes);
			obj = clazz.newInstance(); 
			System.out.println(clazz.getName());
			methods = clazz.getDeclaredMethods();
			results = AnnotationUtil.getAnnotatedMethods(methods, obj);
			System.out.println("Test:");
			results[0].forEach(e -> System.out.println(e));
			int result = results[0].size();
			Assert.assertEquals(8,result);
		}
		
		
		@Test
		public void testCountMethdosWithOtherAnnosThanRunMeShouldReturn1() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
			clazz = Class.forName(classWithRunMes);
			obj = clazz.newInstance(); 
			System.out.println(clazz.getName());
			methods = clazz.getDeclaredMethods();
			results = AnnotationUtil.getAnnotatedMethods(methods, obj);	
			//nur eine Methode mit @Deprecated und ohne RM
			Assert.assertTrue(results[1].size() == 1);
		}
		
		@Test
		public void testCountMethodsWithoutAnyAnnotationShouldReturn4() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
			clazz = Class.forName(classWithRunMes);
			obj = clazz.newInstance(); 
			System.out.println(clazz.getName());
			methods = clazz.getDeclaredMethods();
			results = AnnotationUtil.getAnnotatedMethods(methods, obj);
			//methods that are not annotated 
			Assert.assertTrue(results[2].size() == 4);			
		}
		
		@Test
		public void testCountNotRunnableMethodsInTestClassWithRunMesShouldReturn5() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
			clazz = Class.forName(classWithRunMes);
			obj = clazz.newInstance(); 
			methods = clazz.getDeclaredMethods();
			results = AnnotationUtil.getAnnotatedMethods(methods, obj);
			//methods with RunMe that are not runnable
			int notrunnableMethods = results[3].size();
			Assert.assertEquals(5, notrunnableMethods);			
		}
		
		//=== Test: Exceptions === //
		
		@Test
		public void testMethodWithParametersShouldReturnIllegalArgumentException() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
			clazz = Class.forName(classWithIllegalArgExc);
			obj = clazz.newInstance(); 
			methods = clazz.getDeclaredMethods();
			boolean invocationTargetException = false;
			boolean illegalArgsException = false;
			boolean illegalAccessException =false;
			for(Method m: methods) {
				System.out.println(m.getName());
				Parameter[] params = m.getParameters();
				Object[] objs = params;
				try {
					m.invoke(obj, objs);
				} catch (IllegalArgumentException e) {
					illegalArgsException = true;
				} catch (InvocationTargetException e) {
					invocationTargetException = true;
				}  catch (IllegalAccessException e) {
					illegalAccessException = true;
				}
				
			}
			Assert.assertFalse(invocationTargetException);
			Assert.assertTrue(illegalArgsException);
			Assert.assertFalse(illegalAccessException);
		}
		
		@Test
		public void testMethodThrowingExceptionShouldReturnInvocationTargetException() throws ClassNotFoundException, InstantiationException {
			clazz = Class.forName(classWithInvocatioTargetEx);
			try {
				obj = clazz.newInstance();
			} catch (IllegalAccessException e1) {
				
			} 
			methods = clazz.getDeclaredMethods();
			boolean invocationTargetException = false;
			boolean illegalArgsException = false;
			boolean illegalAccessException =false;
			for(Method m: methods) {
				System.out.println(m.getName());
				Parameter[] params = m.getParameters();
				Object[] objs = params;
				try {
					m.invoke(obj, objs);
				} catch (IllegalArgumentException e) {
					illegalArgsException = true;
				} catch (InvocationTargetException e) {
					invocationTargetException = true;
				}  catch (IllegalAccessException e) {
					illegalAccessException = true;
				}
				
			}
			Assert.assertTrue(invocationTargetException);
			Assert.assertFalse(illegalArgsException);
			Assert.assertFalse(illegalAccessException);
			
		}
		
		@Test
		public void testPrivateMethodShouldReturnIllegalAccessException() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
			clazz = Class.forName(classWithIllegalAccessExc);
			obj = clazz.newInstance(); 
			methods = clazz.getDeclaredMethods();
			boolean invocationTargetException = false;
			boolean illegalArgsException = false;
			List<Boolean> illegalAccessExc = new ArrayList<Boolean>();
			for(Method m: methods) {
				System.out.println(m.getName());
				Parameter[] params = m.getParameters();
				Object[] objs = params;
				try {
					m.invoke(obj, objs);
				} catch (IllegalArgumentException e) {
					illegalArgsException = true;
				} catch (InvocationTargetException e) {
					invocationTargetException = true;
				} catch (IllegalAccessException e) {
					illegalAccessExc.add(true);
				}
			}
			Assert.assertFalse(invocationTargetException);
			Assert.assertFalse(illegalArgsException);
			int count = (int) illegalAccessExc.stream().filter(el -> el==true).count();
			Assert.assertTrue(count == 3);
			
		}
		
		@Test
		public void testPackagePrivateMethodShouldReturnIllegalAccessException() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
			clazz = Class.forName(classWithIllegalAccessExc);
			obj = clazz.newInstance(); 
			methods = clazz.getDeclaredMethods();
			boolean invocationTargetException = false;
			boolean illegalArgsException = false;
			boolean illegalAccessException =false;
			for(Method m: methods) {
				System.out.println(m.getName());
				Parameter[] params = m.getParameters();
				Object[] objs = params;
				try {
					m.invoke(obj, objs);
				} catch (IllegalArgumentException e) {
					illegalArgsException = true;
				} catch (InvocationTargetException e) {
					invocationTargetException = true;
				}  catch (IllegalAccessException e) {
					illegalAccessException = true;
				}
				
			}
			Assert.assertFalse(invocationTargetException);
			Assert.assertFalse(illegalArgsException);
			Assert.assertTrue(illegalAccessException);
			
		}
		
		

}
