package edu.kh.fin.common.aop;

import org.aspectj.lang.annotation.Pointcut;

// Pointcut을 모아둘 클래스
public class PointcutCollection {
	
	@Pointcut("execution(* edu.kh.fin..*Controller.*(..))")
	public void controllerPointcut() {}
	
	
	@Pointcut("execution(* edu.kh.fin..*Controller.*(..))")
	public void serviceImplPointcut() {}
}
