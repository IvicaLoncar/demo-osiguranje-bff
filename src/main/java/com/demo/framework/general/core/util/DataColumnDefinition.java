package com.demo.framework.general.core.util;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class DataColumnDefinition
{
	private Integer id;
	private String javaFieldName;
	private String javaDataType;
	private String dbTableTLA;
	private String dbColumnName;
	private String description;
	private Boolean filter;
	private Boolean sort;
	private DbTypeMapper dbTypeMapper;
	

	
	public static DataColumnDefinition create(int columnOrder, String dataColumnDefinitionSpec)
	{
		return DataColumnDefinition.create(columnOrder, dataColumnDefinitionSpec, null);
	}
	
	
	public static DataColumnDefinition create(int columnOrder, String dataColumnDefinitionSpec, String javaToSQLMappings)
	{
		DataColumnDefinition dataColumnDefinition = new DataColumnDefinition();

		String[] dataColumnDefinitionSpecParts = dataColumnDefinitionSpec.split("#");
		
    dataColumnDefinition = new DataColumnDefinition();
    dataColumnDefinition.setId(columnOrder);
    dataColumnDefinition.setJavaFieldName(dataColumnDefinitionSpecParts.length > 0 ? dataColumnDefinitionSpecParts[0].trim() : "unknown_" +  Integer.toString(columnOrder));
    dataColumnDefinition.setJavaDataType(dataColumnDefinitionSpecParts.length > 1 ? dataColumnDefinitionSpecParts[1].trim() : "String");
    dataColumnDefinition.setDbTableTLA(dataColumnDefinitionSpecParts.length > 2 ? dataColumnDefinitionSpecParts[2].trim() : null);
    dataColumnDefinition.setDbColumnName(dataColumnDefinitionSpecParts.length > 3 ? dataColumnDefinitionSpecParts[3].trim() : "unknown_" +  Integer.toString(columnOrder));
    dataColumnDefinition.setDescription(dataColumnDefinitionSpecParts.length > 4 ? dataColumnDefinitionSpecParts[4].trim() : "unknown_" +  Integer.toString(columnOrder));
    dataColumnDefinition.setFilter(dataColumnDefinitionSpecParts[4].length() > 5 ? Boolean.parseBoolean(dataColumnDefinitionSpecParts[5]) : false);
    dataColumnDefinition.setSort(dataColumnDefinitionSpecParts[5].length() > 6 ? Boolean.parseBoolean(dataColumnDefinitionSpecParts[6]) : false);
    

    if (javaToSQLMappings != null && !("".equals(javaToSQLMappings.trim())))
    {
    	String[] mappingSpecificationParts = javaToSQLMappings.split("#");

    	if (mappingSpecificationParts != null && mappingSpecificationParts.length > 1)
    	{
      	DbTypeMapper dbTypeMapper = new DbTypeMapper();
    		dbTypeMapper.setFunctionName(mappingSpecificationParts[0].trim());

    		String[] dataTypes = mappingSpecificationParts[1].trim().split("\\|\\|");
    		if (dataTypes != null && dataTypes.length > 1)
    		{
    			dbTypeMapper.setToJavaType(dataTypes[1]);    			
    		}

    		dbTypeMapper.setSqlToJavaMappings(new ArrayList<SqlToJavaMapping>());
      	for (int i = 2; i < mappingSpecificationParts.length; i++)
      	{
      		String[] dataValues = mappingSpecificationParts[i].trim().split("\\|\\|");
      		if (dataValues != null && dataValues.length > 1)
      		{
      			dbTypeMapper.getSqlToJavaMappings()
      				.add(SqlToJavaMapping.builder()
			      				.javaValue(dataValues[0].trim())
			      				.sqlValue(dataValues[1].trim())
			      				.build());
      		}
      	}
      	dataColumnDefinition.setDbTypeMapper(dbTypeMapper);
    	}
    }
    
		return dataColumnDefinition;
	}
	
}
