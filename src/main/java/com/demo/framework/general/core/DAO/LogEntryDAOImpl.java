package com.demo.framework.general.core.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.demo.framework.general.core.enums.CustomMessageLevel;
import com.demo.framework.general.core.model.LogEntry;
import com.demo.framework.general.core.util.Context;
import com.demo.framework.general.core.util.CustomData;
import com.demo.framework.general.core.util.CustomDummy;



@Service
public class LogEntryDAOImpl implements LogEntryDAO 
{
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public LogEntryDAOImpl(DataSource dataSource)
	{
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	}

		
	@Override
	public CustomData<List<LogEntry>> selectLogEntries(Integer id, Integer pageNumber, Integer rowsPerPage)
	{
		CustomData<List<LogEntry>> customData = new CustomData<List<LogEntry>>();
		
		List<LogEntry> logEntries = null;
		
		try
		{			
		    logEntries =  
		        this.jdbcTemplate.query(
		            "select log_id, "
		          + "       session_id, "
		          + "       create_time, "
		          + "       create_user, "
		          + "       level, "
		          + "       paket, "
		          + "       metoda, "
		          + "       linija, "
		          + "       poruka, "
		          + "       stack_trace "
		          + "  from logovi"
		          + " where 1 = 1 "
		          + (id != null ? " and log_id = ? " : "")
		          + "limit ? "
		          + "offset ? ",
		          	new PreparedStatementSetter() {		        	   
		        	   public void setValues(PreparedStatement preparedStatement) throws SQLException {
		        		   int paramOrder = 1;
		        		   if (id != null) preparedStatement.setInt(paramOrder++, id);
		        		   preparedStatement.setInt(paramOrder++, rowsPerPage == null ? 100 : rowsPerPage);
		        		   preparedStatement.setInt(paramOrder++, pageNumber == null ? 0 : (pageNumber - 1) * (rowsPerPage == null ? 100 : rowsPerPage));
		        	   }
		        	},
			        new RowMapper<LogEntry>() {
			            public LogEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
			            	LogEntry logEntry = new LogEntry();
			            	logEntry.setLogEntryId(rs.getInt("log_id"));
		                logEntry.setSessionId(rs.getString("session_id"));
		                logEntry.setCreateTime(rs.getTimestamp("create_time").toInstant());
		                logEntry.setCreateUser(rs.getString("create_user"));
				    				logEntry.setLevel(rs.getString("level"));
				    				logEntry.setClassPackage(rs.getString("paket"));
				    				logEntry.setMethod(rs.getString("metoda"));
				    				logEntry.setLineNumber(rs.getInt("linija"));
				    				logEntry.setMessage(rs.getString("poruka"));
				    				logEntry.setStackTrace(rs.getString("stack_trace"));
		                return logEntry;
			            }
		        });		    
		}
		catch (Exception e)
		{		
		      customData.addMessage(CustomMessageLevel.Fatal, e);
		}	

		customData.setData(logEntries != null ? logEntries : new ArrayList<LogEntry>());
		
		return customData;
	}
	
	
	@Override
	public CustomData<CustomDummy> insertLogEntry(LogEntry logEntry)
	{
		CustomData<CustomDummy> customData = new CustomData<CustomDummy>();
		
		int rowsAffected = 0;				
		
		try
		{
		    rowsAffected =  
		        this.jdbcTemplate.update
		        (
		            "insert into logovi "
		          + "( "
		          + "  session_id, "
		          + "  create_time, "
		          + "  create_user, "
		          + "  level, "
		          + "  paket, "
		          + "  metoda, "
		          + "  linija, "
		          + "  poruka, "
		          + "  stack_trace "
		          + ") "
		          + "values "
		          + "( "
		          + "  ?, "
		          + "  localtimestamp, "
		          + "  ?, ?, ?, ?, ?, ?, ? "
		          + ") ",
		          MDC.get("sessionId"),
		          MDC.get("userInfo.username"),
		          logEntry.getLevel(),
		          logEntry.getClassPackage(),
		          logEntry.getMethod(),
		          logEntry.getLineNumber(),
		          logEntry.getMessage(),
		          logEntry.getStackTrace()
		        );		
		}
		catch (Exception e)
		{	
			// insert u log ima ovaj format kako ne bi ušao u beskonačnu petlju
			System.out.println(e.getMessage());
			e.printStackTrace();
		}		
		
		return customData;
	}
		
	
}
