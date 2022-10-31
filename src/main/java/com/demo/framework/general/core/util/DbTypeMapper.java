package com.demo.framework.general.core.util;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class DbTypeMapper
{
	private String functionName;
	private String toJavaType;
	private List<SqlToJavaMapping> sqlToJavaMappings;

}
