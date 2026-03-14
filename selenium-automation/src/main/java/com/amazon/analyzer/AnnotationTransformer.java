package com.amazon.analyzer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class AnnotationTransformer implements IAnnotationTransformer {

	private static final Logger log = LoggerFactory.getLogger(AnnotationTransformer.class);

	@Override
	@SuppressWarnings("rawtypes")
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

		if (testMethod != null) {
			log.info("Attaching RetryAnalyzer to : {}", testMethod.getName());
			annotation.setRetryAnalyzer(RetryAnalyzer.class);
		}

	}

}