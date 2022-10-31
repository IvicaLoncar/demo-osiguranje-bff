package com.demo.framework.general.core.util;

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
public class SqlToJavaMapping
{
	private String sqlValue;
	private String javaValue;
}
