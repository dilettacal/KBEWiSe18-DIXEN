package de.htw.ai.kbe.runmerunner;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AnnotationUtilTest {
	
	//classToLoad=de.htw.ai.wad.MyClassWithFindMesDixen
	
	private String classWithRunMes;
	private String classWithoutRunMes;
	private String notExistingClass;
	private List[] annotationsTracker;

	@Before
	public void setUp() throws Exception {
		classWithRunMes = "de.htw.ai.kbe.TestClassWithRunMes";
		classWithoutRunMes = "de.htw.ai.kbe.TestClassWithoutRunMes";
		notExistingClass = "de.htw.ai.kbe.KeineAhnung";
		annotationsTracker = new ArrayList[4];
	}
	
	@Test
	public void testOpenExistingClassShouldReturnTrue() throws InstantiationException, IllegalAccessException {
		boolean result = AnnotationUtil.analyzeClass(classWithRunMes);
		Assert.assertTrue(result);
	}

	@Test void testOpenNotExistingClassShouldReturnFalse() throws InstantiationException, IllegalAccessException {
		boolean result = AnnotationUtil.analyzeClass(notExistingClass);
		Assert.assertFalse(result);
	}
	
	
	@Test
	public void testCountAnnotationsWithRunMesInClassWithRunMesShouldReturn3() throws InstantiationException, IllegalAccessException {
		
		
		
	}
	
	@Test
	public void testCountAnnotationsWithoutRunMesInClassWithRunMesShouldReturn() {
		
	}
	
	@Test
	public void testCountAnnotationsWithRunMesInClassWithoutRunMesShouldReturnZero() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testCountAnnotationsWithoutRunMesInClassWithRunMesShouldReturn0() {
		
	}


}
