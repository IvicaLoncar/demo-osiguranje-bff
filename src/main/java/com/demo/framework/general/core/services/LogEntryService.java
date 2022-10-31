package com.demo.framework.general.core.services;

import java.util.List;

import com.demo.framework.general.core.model.LogEntry;
import com.demo.framework.general.core.util.CustomData;

public interface LogEntryService 
{
	public CustomData<LogEntry> getLogEntry(Integer logEntryId);
	
	public CustomData<List<LogEntry>> getLogEntries(Integer pageNumber, Integer rowPerPage);
	
	public CustomData<LogEntry> postLogEntry(LogEntry logEntry);
}
