package com.demo.framework.general.core.util;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class PreparedMessage
{
	private String message;
	private List<Object> parameters = new ArrayList<Object>();
	
	
	public PreparedMessage unformattedMessage(String message)
	{
		this.message = message;
		return this;
	}
	
	
	public PreparedMessage parameter(Object parameter)
	{
		this.parameters.add(parameter);
		return this;
	}
	
	
	public String getPreparedMessage()
	{
		return this.message;
	}
	
}
