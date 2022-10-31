package com.demo.framework.general.core.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;

public class DateTimeUtil
{

	public static Date TO_DATE(String date, String format)
	{
		java.util.Date utilDate = null;
		try
		{
			utilDate = new SimpleDateFormat(format).parse(date);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		return new java.sql.Date(utilDate.getTime());
	}

	
	public static Timestamp TO_TIMESTAMP(String timestamp)
	{
		return Timestamp.from(OffsetDateTime.parse(timestamp ).toInstant());
	}

}
