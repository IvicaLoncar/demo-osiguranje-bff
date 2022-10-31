package com.demo.framework.general.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.framework.general.core.model.LogEntry;
import com.demo.framework.general.core.services.LogEntryService;
import com.demo.framework.general.core.util.CustomData;


@RestController
public class LogEntryController 
{
	@Autowired
	private LogEntryService logEntryService;
	
	
	
    @RequestMapping(value = "/api/log-entries/{id}", method = RequestMethod.GET)
    public ResponseEntity<CustomData<LogEntry>> getLogEntry(
    	@PathVariable("id") Integer id) 
    {
    	CustomData<LogEntry> logEntry = this.logEntryService.getLogEntry(id);
    	    	
        return new ResponseEntity<CustomData<LogEntry>>(logEntry, HttpStatus.OK);
    }
	
	
    @RequestMapping(value = "/api/log-entries", method = RequestMethod.GET)
    public ResponseEntity<CustomData<List<LogEntry>>> getLogEntries(
    	@RequestParam(name = "page", required = false) Integer pageNumber,
    	@RequestParam(name = "rows", required = false) Integer rowPerPage) 
    {
    	CustomData<List<LogEntry>> logEntry = this.logEntryService.getLogEntries(pageNumber, rowPerPage);
    	    	
        return new ResponseEntity<CustomData<List<LogEntry>>>(logEntry, HttpStatus.OK);
    }

    
    @RequestMapping(value="/api/log-entries", method = RequestMethod.POST)
    public ResponseEntity<CustomData<LogEntry>> postLogEntry(@RequestBody LogEntry requestData) 
    {
    	CustomData<LogEntry> logEntry = this.logEntryService.postLogEntry(requestData);
    	
        return new ResponseEntity<CustomData<LogEntry>>(logEntry, HttpStatus.OK);
    }

    
	
	
}
