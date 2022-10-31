package com.demo.framework.general.core.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DataFieldValue 
{
	private String fieldName;
	private String fieldValue;
	private String fieldDataType;
	private Boolean isKeyField;
}
