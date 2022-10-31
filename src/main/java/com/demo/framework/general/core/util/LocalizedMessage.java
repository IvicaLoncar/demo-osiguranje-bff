package com.demo.framework.general.core.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LocalizedMessage
{
	private String locale;
	private String message;
	
}
