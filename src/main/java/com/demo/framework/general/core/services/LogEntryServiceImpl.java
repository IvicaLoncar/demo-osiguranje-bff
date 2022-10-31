package com.demo.framework.general.core.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.framework.general.core.DAO.LogEntryDAO;
import com.demo.framework.general.core.model.LogEntry;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;

@Service
public class LogEntryServiceImpl implements LogEntryService
{
	@Autowired
	private LogEntryDAO logEntryDAO;
	
	
	@Override
	public CustomData<LogEntry> getLogEntry(Integer logEntryId) 
	{		
		CustomData<List<LogEntry>> logEntryList = this.logEntryDAO.selectLogEntries(logEntryId, 0, 1); 
		CustomData<LogEntry> logEntry = new CustomData<LogEntry>();
		if (logEntryList.getData() != null && logEntryList.getData().size() > 0)
		{
			logEntry.setData(logEntryList.getData().get(0));
		}
		return logEntry;
	}	
	
	
	@Override
	public CustomData<List<LogEntry>> getLogEntries(Integer pageNumber, Integer rowPerPage)
	{
		return this.logEntryDAO.selectLogEntries(null, pageNumber, rowPerPage);
	}
	
	
	@Override
	public CustomData<LogEntry> postLogEntry(LogEntry logEntry) 
	{	
		CustomData<CustomDummy> insertStatus = this.logEntryDAO.insertLogEntry(logEntry);
		
		CustomData<LogEntry> insertedRowStatus = new CustomData<LogEntry>();
		insertedRowStatus.setData(logEntry);
		insertedRowStatus.setMessages(insertStatus.getMessages());
		
		return insertedRowStatus;
	}
	
}
