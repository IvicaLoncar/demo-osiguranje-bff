package com.demo.framework.general.core.util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


public class UpdateParameters
{
	private StringBuilder columnField = new StringBuilder();
	private List<Object> columnValues = new ArrayList<Object>();
	private List<Integer> sqlTypes = new ArrayList<Integer>();

	
	
	public UpdateParameters(UnpivotedData unpivotedData, List<DataColumnDefinition> dataColumnDefinitions, String username)
	{
  	List<UnpivotedValue> unpivotedValues = unpivotedData.getValues();
  	
  	for (int i = 1; i < unpivotedValues.size(); i++)
  	{
			for (int j = 0; j < dataColumnDefinitions.size(); j++)
			{
				DataColumnDefinition dataColumnDefinition = dataColumnDefinitions.get(j);
				if (dataColumnDefinition.getJavaFieldName().equalsIgnoreCase(unpivotedValues.get(i).getColumnName()))
				{
		  		if (!(unpivotedValues.get(i).getColumnName().equalsIgnoreCase("createUser") ||
	  				  unpivotedValues.get(i).getColumnName().equalsIgnoreCase("createDate") ||
	  			  	unpivotedValues.get(i).getColumnName().equalsIgnoreCase("updateUser") ||
	  			  	unpivotedValues.get(i).getColumnName().equalsIgnoreCase("updateDate")))
		  		{		  			
		  			if (dataColumnDefinition.getDbTypeMapper() != null)
		  			{
		  				while (true)
		  				{
		  					if (dataColumnDefinition.getDbTypeMapper().getFunctionName().trim().equalsIgnoreCase("CONVERT_STRING_TO_BOOLEAN"))
		  					{
		  						sqlTypes.add(Types.VARCHAR);
	  							for (int k = 0; k < dataColumnDefinition.getDbTypeMapper().getSqlToJavaMappings().size(); k++)
	  							{
	  								if (dataColumnDefinition.getDbTypeMapper().getSqlToJavaMappings().get(k).getJavaValue().equalsIgnoreCase((Boolean)unpivotedValues.get(i).getValue() ? "TRUE" : "FALSE")) 
	  								{
	  									columnValues.add(dataColumnDefinition.getDbTypeMapper().getSqlToJavaMappings().get(k).getSqlValue());	
	  								}
	  							}
		  						break;
		  					}
		  					break;
		  				}
		  			}
		  			else
		  			{
			  			columnValues.add(unpivotedValues.get(i).getValue());
			  			if (unpivotedValues.get(i).getDataType().equalsIgnoreCase("Long"))
			  			{
			  				sqlTypes.add(Types.BIGINT);
			  			}
			  			if (unpivotedValues.get(i).getDataType().equalsIgnoreCase("Integer"))
			  			{
			  				sqlTypes.add(Types.INTEGER);
			  			}
			  			if (unpivotedValues.get(i).getDataType().equalsIgnoreCase("String"))
			  			{
			  				sqlTypes.add(Types.VARCHAR);
			  			}
			  			if (unpivotedValues.get(i).getDataType().equalsIgnoreCase("Date"))
			  			{
			  				sqlTypes.add(Types.DATE);
			  			}
			  			if (unpivotedValues.get(i).getDataType().equalsIgnoreCase("Timestamp"))
			  			{
			  				sqlTypes.add(Types.TIMESTAMP);
			  			}
		  			}
		  			columnField.append(dataColumnDefinition.getDbColumnName() + " = ?, ");
		  		}
				}
			}

  	}
  	columnValues.add(username);
  	sqlTypes.add(Types.VARCHAR);
  	columnValues.add(unpivotedValues.get(0).getValue());
  	sqlTypes.add(Types.BIGINT);
	}
	
	
	public String getColumnFields()
	{
		return this.columnField.toString();
	}
	
	
	public Object[] getColumnValues()
	{
		return this.columnValues.toArray();
	}
	
	
	public int[] getSqlTypes()
	{
  	int[] sqlTypeArray = new int[this.sqlTypes.size()];
  	for (int i = 0; i < this.sqlTypes.size(); i++)
  	{
  		sqlTypeArray[i] = this.sqlTypes.get(i);
  	}
  	return sqlTypeArray;
	}
	
}
