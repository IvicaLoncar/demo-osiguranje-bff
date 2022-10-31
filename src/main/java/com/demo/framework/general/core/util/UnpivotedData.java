package com.demo.framework.general.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;

import lombok.ToString;

@ToString
public class UnpivotedData
{
	private List<UnpivotedValue> unpivotedValues = new ArrayList<UnpivotedValue>();
	

	public UnpivotedData()
	{		
	}
	
	
	public UnpivotedData(Long id, JsonPatch jsonPatch, List<DataColumnDefinition> dataColumnDefinitions)
	{
    ObjectMapper objectMapper = new ObjectMapper();

    JsonNode jsonPatchList = objectMapper.convertValue(jsonPatch, JsonNode.class);
    
    this.add(dataColumnDefinitions.get(0).getJavaFieldName(), dataColumnDefinitions.get(0).getJavaDataType(), id);
    
    for(int i = 0; i < jsonPatchList.size(); i++) 
    {
    	boolean exists = false;
    	for (int j = 0; j < this.unpivotedValues.size(); j++)
    	{
    		if (this.unpivotedValues.get(j).getColumnName().equalsIgnoreCase(jsonPatchList.get(i).get("path").asText().substring(1)))
    		{
    			exists = true;
    			break;
    		}
    	}
    	if (!exists)
    	{
	    	for (int j = 1; j < dataColumnDefinitions.size(); j++) // preskače se prvi element jer je id
	    	{
	    		if (dataColumnDefinitions.get(j).getJavaFieldName().equalsIgnoreCase(jsonPatchList.get(i).get("path").asText().substring(1)))
	    		{
	    			Object value = null;
	    			if (!jsonPatchList.get(i).get("op").asText().equals("remove"))
	    			{
		    			if (dataColumnDefinitions.get(j).getJavaDataType().equalsIgnoreCase("Long"))
		    			{
		    				value = Long.parseLong(jsonPatchList.get(i).get("value").asText());
		    			}
		    			if (dataColumnDefinitions.get(j).getJavaDataType().equalsIgnoreCase("Integer"))
		    			{
		    				value = Integer.parseInt(jsonPatchList.get(i).get("value").asText());
		    			}
		    			if (dataColumnDefinitions.get(j).getJavaDataType().equalsIgnoreCase("String"))
		    			{
		    				value = jsonPatchList.get(i).get("value").asText();
		    			}
		    			if (dataColumnDefinitions.get(j).getJavaDataType().equalsIgnoreCase("Boolean"))
		    			{
		    				value = jsonPatchList.get(i).get("value").asBoolean();
		    			}
		    			if (dataColumnDefinitions.get(j).getJavaDataType().equalsIgnoreCase("BigInteger"))
		    			{
		    				value = new BigInteger(jsonPatchList.get(i).get("value").asText());
		    			}
		    			if (dataColumnDefinitions.get(j).getJavaDataType().equalsIgnoreCase("BigDecimal"))
		    			{
		    				value = new BigDecimal(jsonPatchList.get(i).get("value").asText());
		    			}
		    			if (dataColumnDefinitions.get(j).getJavaDataType().equalsIgnoreCase("Date"))
		    			{
		    				value = DateTimeUtil.TO_DATE(jsonPatchList.get(i).get("value").asText(), "yyyy-MM-dd");
		    			}
		    			if (dataColumnDefinitions.get(j).getJavaDataType().equalsIgnoreCase("Timestamp"))
		    			{
		    				value = DateTimeUtil.TO_TIMESTAMP(jsonPatchList.get(i).get("value").asText());
		    			}
	    			}
	    			this.add(dataColumnDefinitions.get(j).getJavaFieldName(), dataColumnDefinitions.get(j).getJavaDataType(), value);
	    			break;
	    		}
	    	}
    	}
    }
	}	
	
	
	public void add(UnpivotedValue unpivotedValue)
	{
		UnpivotedValue tmpUnpivotedValue = null;
		
		for (int i = 0; i < this.unpivotedValues.size(); i++)
		{
			if (this.unpivotedValues.get(i).getColumnName().trim().equalsIgnoreCase(unpivotedValue.getColumnName().trim()))
			{
				tmpUnpivotedValue = this.unpivotedValues.get(i);
				break;
			}
		}
		if (tmpUnpivotedValue == null)
		{
			tmpUnpivotedValue = new UnpivotedValue();
			this.unpivotedValues.add(tmpUnpivotedValue);
		}
		if (tmpUnpivotedValue != null)
		{
			tmpUnpivotedValue.setColumnName(unpivotedValue.getColumnName());
			tmpUnpivotedValue.setDataType(unpivotedValue.getDataType());
			tmpUnpivotedValue.setValue(unpivotedValue.getValue());
		}		
	}
	
	
	public void add(String columnName, String dataType, Object value)
	{
		UnpivotedValue unpivotedValue = new UnpivotedValue();		
		unpivotedValue.setColumnName(columnName);
		unpivotedValue.setDataType(dataType);
		unpivotedValue.setValue(value);		
		this.add(unpivotedValue);
	}
	
	
	public int size()
	{
		return this.unpivotedValues.size();
	}
	
	
	public List<UnpivotedValue> getValues()
	{
		return this.unpivotedValues;
	}
	
	
	public UnpivotedValue get(String columnName)
	{
		UnpivotedValue unpivotedValue = null;
		
		for (int i = 0; i < this.unpivotedValues.size(); i++)
		{
			if (this.unpivotedValues.get(i).getColumnName().trim().equalsIgnoreCase(columnName.trim()))
			{
				unpivotedValue = this.unpivotedValues.get(i);
				break;
			}
		}		
		return unpivotedValue;
	}
	
	
	public boolean exists(String columnName)
	{
		return this.get(columnName) != null;
	}
	
	
	public Object getValue(String columnName)
	{
		UnpivotedValue unpivotedValue = this.get(columnName);
		return unpivotedValue == null ? null : unpivotedValue.getValue();
	}
	
	
	public Long getValueAsLong(String columnName)
	{
		UnpivotedValue unpivotedValue = this.get(columnName);
		return unpivotedValue == null || !(unpivotedValue.getValue() instanceof Long) ? null : unpivotedValue.getValueAsLong();
	}
	

	public Integer getValueAsInteger(String columnName)
	{
		UnpivotedValue unpivotedValue = this.get(columnName);
		return unpivotedValue == null || !(unpivotedValue.getValue() instanceof Integer) ? null : unpivotedValue.getValueAsInteger();
	}

	
	public String getValueAsString(String columnName)
	{
		UnpivotedValue unpivotedValue = this.get(columnName);
		return unpivotedValue == null || !(unpivotedValue.getValue() instanceof String) ? null : unpivotedValue.getValueAsString();
	}

	public Boolean getValueAsBoolean(String columnName)
	{
		UnpivotedValue unpivotedValue = this.get(columnName);
		return unpivotedValue == null || !(unpivotedValue.getValue() instanceof Boolean) ? null : unpivotedValue.getValueAsBoolean();
	}

	public BigInteger getValueAsBigInteger(String columnName)
	{
		UnpivotedValue unpivotedValue = this.get(columnName);
		return unpivotedValue == null || !(unpivotedValue.getValue() instanceof BigInteger) ? null : unpivotedValue.getValueAsBigInteger();
	}

	public BigDecimal getValueAsBigDecimal(String columnName)
	{
		UnpivotedValue unpivotedValue = this.get(columnName);
		return unpivotedValue == null || !(unpivotedValue.getValue() instanceof BigDecimal) ? null : unpivotedValue.getValueAsBigDecimal();
	}

	public Instant getValueAsInstant(String columnName)
	{
		UnpivotedValue unpivotedValue = this.get(columnName);
		return unpivotedValue == null || !(unpivotedValue.getValue() instanceof Instant) ? null : unpivotedValue.getValueAsInstant();
	}
	
}
