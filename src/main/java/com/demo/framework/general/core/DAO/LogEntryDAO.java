package com.demo.framework.general.core.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.demo.framework.general.core.model.LogEntry;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;



@Repository
public interface LogEntryDAO 
{
	public CustomData<List<LogEntry>> selectLogEntries(Integer id, Integer pageNumber, Integer rowsPerPage);
	
	public CustomData<CustomDummy> insertLogEntry(LogEntry logEntry);
	
}
