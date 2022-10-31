package com.demo.framework.general.core.util;

import java.util.List;
import java.util.Map;

public class DBUtil
{
	public static String prepareWhereCondition(String whereCondition, List<DataColumnDefinition> dataColumnDefinitions)
	{
		return "";
	}
	
	public static void prepareColumnList(StringBuilder columnList, List<DataColumnDefinition> dataColumnDefinitions, List<String> includeColumns, List<String> excludeColumns, Map<String, Boolean> selectedColumns)
	{		
		System.out.println("incl cols: " + includeColumns.toString());
		System.out.println("excl cols: " + excludeColumns.toString());
		
		dataColumnDefinitions.forEach((column) -> 
		{
				boolean include = false;
				if (includeColumns.size() > 0)
				{
					for (int i = 0; i < includeColumns.size(); i++)
					{
						//System.out.println("uspredba: " + includeColumns.get(i).trim() + "   " + column.getDbColumnName().trim());
						if (includeColumns.get(i).trim().equalsIgnoreCase(column.getJavaFieldName().trim()))
						{
							if (excludeColumns.size() == 0)
							{								
								include = true;
							}
							else
							{
								boolean columnExcluded = false; 
								for (int j = 0; j < excludeColumns.size(); j++)
								{
									if (excludeColumns.get(j).trim().equalsIgnoreCase(column.getJavaFieldName().trim()))
									{
										columnExcluded = true;
										break;
									}
								}
								if (!columnExcluded)
								{
									include = true;
								}
							}
						}
					}
				}
				else
				{
					boolean columnExcluded = false; 
					for (int j = 0; j < excludeColumns.size(); j++)
					{
						if (excludeColumns.get(j).trim().equalsIgnoreCase(column.getJavaFieldName().trim()))
						{
							columnExcluded = true;
							break;
						}
					}
					if (!columnExcluded)
					{
						include = true;
					}
				}	
				if (include)
				{
					columnList.append((column.getDbTableTLA() != null && !"".equals(column.getDbTableTLA()) ? column.getDbTableTLA() + "." : "") + column.getDbColumnName() + ", ");
					selectedColumns.put(column.getDbColumnName(), true);
				}
				else
				{
					selectedColumns.put(column.getDbColumnName(), false);
				}
		});
		if (columnList.length() > 0) columnList.deleteCharAt(columnList.lastIndexOf(","));
		//System.out.println(" selected columns " + selectedColumns.toString());
		//return sb.toString().substring(0, sb.length() - 2);
	}
}
