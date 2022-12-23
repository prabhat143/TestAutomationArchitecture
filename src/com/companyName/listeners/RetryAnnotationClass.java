package com.companyName.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class RetryAnnotationClass implements IAnnotationTransformer{

	/**
	 * Set retry analyser on suit level
	 * @param annotation
	 * @param testClass
	 * @param testConstructor
	 * @param testMethod
	 */
	@SuppressWarnings("rawtypes")
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor,Method testMethod) {
			annotation.setRetryAnalyzer(RetryTestClass.class);
	}

}
