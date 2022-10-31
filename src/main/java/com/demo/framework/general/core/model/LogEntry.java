package com.demo.framework.general.core.model;


import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LogEntry 
{
	private Integer logEntryId;
	private String sessionId;
	private Instant createTime;
	private String createUser;
	private String level;
	private String classPackage;
	private String method;
	private Integer lineNumber;
	private String message;
	private String stackTrace;
}