package com.demo.framework.general.core.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.framework.general.core.DAO.LogEntryDAO;
import com.demo.framework.general.core.enums.CustomMessageLevel;
import com.demo.framework.general.core.model.LogEntry;


@Component
public class LogUtil 
{
	public static boolean logStackTraceInConsole = true;
	public static boolean logActivated = true;
	public static CustomMessageLevel level = CustomMessageLevel.Debug;
	
	
	private static LogEntryDAO logEntryDAO;
	
	@Autowired
	private LogEntryDAO logEntryDAOTemp;

	@PostConstruct     
	private void initStaticDao () 
	{
	   logEntryDAO = this.logEntryDAOTemp;
	}
	
	
	public static void Log(CustomMessageLevel level, String message, Exception exception)
	{		
		StringBuilder stackTrace = new StringBuilder();
		
		StackTraceElement[] stackTraceElements = exception.getStackTrace();
		
		if (stackTraceElements != null && stackTraceElements.length > 0)
		{
			for (int i = 0; i < stackTraceElements.length; i++)
			{
				stackTrace.append(stackTraceElements[i].toString() + '\n');
			}
		}
		
		String[] classFullNameParts = Thread.currentThread().getStackTrace()[3].getClassName().split("\\.");
		StringBuilder packageName = new StringBuilder();
		String className = "";
		
		for (int i = 0; i < classFullNameParts.length; i++)
		{
			if (i == classFullNameParts.length - 1)
			{
				className = classFullNameParts[i];
			}
			else
			{
				packageName.append((packageName.length() > 0 ? '.' : "") + classFullNameParts[i]);	
			}
		}		

		//System.out.println(now() + "  DEMO APPLICATION => " + level.toString() + " " + message);

		if (LogUtil.logStackTraceInConsole)
		{
			//if (stackTrace.length() > 0) exception.printStackTrace();
		}
		
		LogEntry logEntry = new LogEntry();
		logEntry.setSessionId(MDC.get("sessionID"));
		logEntry.setLevel(level.toString());
		logEntry.setClassPackage(packageName.toString());
		logEntry.setMethod(className + "." + Thread.currentThread().getStackTrace()[3].getMethodName());
		logEntry.setLineNumber(Thread.currentThread().getStackTrace()[3].getLineNumber());
		logEntry.setMessage(exception != null ? exception.getMessage() : message);
		if (stackTrace.length() > 0) logEntry.setStackTrace(stackTrace.toString());
		LogUtil.logEntryDAO.insertLogEntry(logEntry);
	}
	
	
	public static List<String> extractStackTraceToList(Exception exception)
	{
		List<String> stackTrace = new ArrayList<String>();
		
		if (exception != null)
		{
			StackTraceElement[] stackTraceElements = exception.getStackTrace();
			
			if (stackTraceElements != null && stackTraceElements.length > 0)
			{
				for (int i = 0; i < stackTraceElements.length; i++)
				{
					stackTrace.add(stackTraceElements[i].toString());
				}
			}
		}
		return stackTrace;
	}
	
	
	private static String now()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");		
		return sdf.format(cal.getTime());
	}
	
}