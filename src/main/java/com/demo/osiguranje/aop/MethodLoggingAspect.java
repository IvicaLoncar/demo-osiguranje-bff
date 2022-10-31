package com.demo.osiguranje.aop;


import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodLoggingAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodLoggingAspect.class);
	
    @Value("${aspect.logging.packageNameEntriesRemoved}")
    private int packageNameEntriesRemoved;
	
	@Before(value = "execution(* com.example.demo.general.*.*.*(..))") // and args(name,empId)")
	public void beforeAdvice(JoinPoint joinPoint) //, String name, String empId) 
	{
		String path = MDC.get("path");
		path = path == null ? "" : path;
		String current = MDC.get("current");
		current = current == null ? "" : current;
		String next = MDC.get("next");
		next = next == null ? "" : next;

		//System.out.println("MDC GET: path: " + path +  "   current: " + current + "   next: " +  next);
		if (current == "")
		{
			MDC.put("path", "");
			MDC.put("current", "1-1");
			MDC.put("next", "2-1");
			MDC.put("indent", "");
			MDC.put("userInfo.username", "user09");
			MDC.put("sessionId", UUID.randomUUID().toString());
			//System.out.println("MDC SET: path: " +   "   current: 1-1" +  "   next: 2-1" );
		}
		else
		{
			path = path + (path == "" ? "" : ",") + current;
			int indentCount = path.split(",").length;
			MDC.put("indent", indentCount > 0 ? "  ".repeat(indentCount) : "");
			MDC.put("path", path);
			MDC.put("current", next);
			MDC.put("next", Integer.toString(Integer.parseInt(next.split("-")[0]) + 1) + "-1");
			//System.out.println("MDC SET: path: " + (path + (path == "" ? "" : ",") + current) +  "   current: " + next + "   next: " +  Integer.toString(Integer.parseInt(next.split("-")[0]) + 1) + "-1");

		}
		
		//if (razina == null) MDC.put("razina", "1");
		//else MDC.put("razina",  Integer.toString(Integer.parseInt(razina) + 1));

		StringBuilder formattedMethodName = new StringBuilder();
		String[] joinPointParts = joinPoint.getSignature().toString().split("\\.");
		for (int i = this.packageNameEntriesRemoved; i < joinPointParts.length; i++)
		{
			formattedMethodName.append((formattedMethodName.toString().equals("") ? "" : ".") + joinPointParts[i]);
		}
	
		LOGGER.info("BEGIN: " + formattedMethodName.toString());
		
//		LOGGER.error("BEFORE:" + joinPointParts.length + "  " + joinPoint.getSignature());

		//LOGGER.error("Creating Employee with name ");
		
	}

	@After(value = "execution(* com.example.demo.general.*.*.*(..))") // and args(name,empId)")
	public void afterAdvice(JoinPoint joinPoint) //, String name, String empId) 
	{
		StringBuilder formattedMethodName = new StringBuilder();
		String[] joinPointParts = joinPoint.getSignature().toString().split("\\.");
		for (int i = this.packageNameEntriesRemoved; i < joinPointParts.length; i++)
		{
			formattedMethodName.append((formattedMethodName.toString().equals("") ? "" : ".") + joinPointParts[i]);
		}

		LOGGER.info("AFTER:" + formattedMethodName.toString());

		//LOGGER.error("Successfully created Employee with name - ");
		
		String path = MDC.get("path");
		path = path == null ? "" : path;
		String current = MDC.get("current");
		current = current == null ? "" : current;
		String next = MDC.get("next");
		next = next == null ? "" : next;
		
		String[] pathParts = path.split(",");
		String[] currentParts = current.split("-");

		//System.out.println("MDC GET: path: " + path +  "   current: " + current + "   next: " +  next);

		if (path == null || "".equals(path.trim()))
		{
			//System.out.println("path je null ili prazan, briše se MDC");
			MDC.clear();
			//MDC.put("path", "");
			//MDC.put("current", "");
			//MDC.put("next", "");
			//MDC.put("indent", "");
			//System.out.println("MDC GET: path: " + "   current: " + "   next: ");
		}
		else
		{
			MDC.put("next", currentParts[0] + "-" + Integer.toString(Integer.parseInt(currentParts[1]) + 1));
			MDC.put("current", pathParts[pathParts.length - 1]);
			path = "";
			for (int i = 0; i < pathParts.length - 1 ; i++)
			{
				path = path + (path == "" ? "" : ",") + pathParts[i];
				//System.out.println("path loop " + i + ":  " + path );
			}
			MDC.put("path", path);
			MDC.put("indent", pathParts.length - 1 > 0 ? "  ".repeat(pathParts.length - 1) : "");
			//System.out.println("MDC SET: path: " + path +  "   current: " + pathParts[pathParts.length - 1] + "   next: " +  (currentParts[0] + "-" + Integer.toString(Integer.parseInt(currentParts[1]) + 1)));
		}


		/*String razina = MDC.get("razina");
		if (razina == null) MDC.put("razina", "");
		else 
		{
			Integer tmpRazina = Integer.parseInt(razina) - 1;
			MDC.put("razina",  tmpRazina > 0 ? Integer.toString(tmpRazina) : "");
		}*/

	}
}