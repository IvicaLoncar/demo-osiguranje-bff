package com.demo.framework.general.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UnpivotedValue
{
	private String columnName;
	private String dataType;
	private Object value;
	
	
	public boolean match(String columnName, String dataType)
	{
		return (this.columnName.trim().equalsIgnoreCase(columnName.trim()) &&
						this.dataType.trim().equalsIgnoreCase(dataType.trim()) &&
						(this.value == null || 
						 ((this.dataType.trim().equalsIgnoreCase("Long") && this.value instanceof Long) ||
						  (this.dataType.trim().equalsIgnoreCase("Integer") && this.value instanceof Integer) ||
						  (this.dataType.trim().equalsIgnoreCase("String") && this.value instanceof String) ||
						  (this.dataType.trim().equalsIgnoreCase("Boolean") && this.value instanceof Boolean) ||
						  (this.dataType.trim().equalsIgnoreCase("BigInteger") && this.value instanceof BigInteger) ||
						  (this.dataType.trim().equalsIgnoreCase("BigDecimal") && this.value instanceof BigDecimal) ||
						  (this.dataType.trim().equalsIgnoreCase("Date") && this.value instanceof Date) ||
						  (this.dataType.trim().equalsIgnoreCase("Timestamp") && this.value instanceof Timestamp) ||
						  (this.dataType.trim().equalsIgnoreCase("Instant") && this.value instanceof Instant))));
	}

	public Object getValue()
	{
		return this.value == null ? null : this.value;
	}

	public Long getValueAsLong()
	{
		return this.value == null || !(this.value instanceof Long) ? null : (Long)this.value;
	}

	public Integer getValueAsInteger()
	{
		return this.value == null || !(this.value instanceof Integer) ? null : (Integer)this.value;
	}

	public String getValueAsString()
	{
		return this.value == null || !(this.value instanceof String) ? null : (String)this.value;
	}
	
	public Boolean getValueAsBoolean()
	{
		return this.value == null || !(this.value instanceof Boolean) ? null : (Boolean)this.value;
	}
	
	public BigInteger getValueAsBigInteger()
	{
		return this.value == null || !(this.value instanceof BigInteger) ? null : (BigInteger)this.value;
	}
	
	public BigDecimal getValueAsBigDecimal()
	{
		return this.value == null || !(this.value instanceof BigDecimal) ? null : (BigDecimal)this.value;
	}
	
	public Date getValueAsDate()
	{
		return this.value == null || !(this.value instanceof Date) ? null : (Date)this.value;
	}

	public Timestamp getValueAsTimestamp()
	{
		return this.value == null || !(this.value instanceof Timestamp) ? null : (Timestamp)this.value;
	}

	public Instant getValueAsInstant()
	{
		return this.value == null || !(this.value instanceof Instant) ? null : (Instant)this.value;
	}


}
